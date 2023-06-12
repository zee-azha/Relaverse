package com.bangkit.relaverse.ui.create_event

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bangkit.relaverse.R
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.data.utils.isValidEmail
import com.bangkit.relaverse.data.utils.isValidPassword
import com.bangkit.relaverse.data.utils.isValidPhoneNumber
import com.bangkit.relaverse.data.utils.reduceFileImage
import com.bangkit.relaverse.data.utils.uriToFile
import com.bangkit.relaverse.databinding.ActivityCreateEventBinding
import com.bangkit.relaverse.viewmodel.CreateEventViewModel
import com.bangkit.relaverse.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEventBinding
    private var location: String = ""
    private var lat: String = ""
    private var lng: String = ""
    private var id: String = ""
    private var token: String = ""
    private var name: String = ""
    private var dateFormatter: SimpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var getFile: File? = null
    private val viewModel by viewModels<CreateEventViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this, resources.getString(R.string.error_permission), Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            launch {
                viewModel.getLoc().collect {
                    if (!it.isNullOrEmpty()) location = it
                }
            }

            launch {
                viewModel.getLng().collect {
                    if (!it.isNullOrEmpty()) lng = it
                }
            }

            launch {
                viewModel.getLat().collect {
                    if (!it.isNullOrEmpty()) lat = it
                }
            }
        }
        binding.eventLocationEditText.setText(location)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            launch {
                viewModel.getId().collect { UID ->
                    if (!UID.isNullOrEmpty()) id = UID
                }
            }
            launch {
                viewModel.getToken().collect {
                    if (!it.isNullOrEmpty()) token = it
                }
            }

            launch {
                viewModel.getUserById(token, id.toInt()).collect {
                    when (it) {
                        is Resource.Loading -> {
                            showLoading(true)
                        }

                        is Resource.Error -> {
                            showLoading(false)
                        }

                        is Resource.Success -> {
                            showLoading(false)
                            name = it.data.user.name
                        }
                    }
                }
            }
        }


        binding.apply {
            eventDateEditText.keyListener = null
            eventLocationEditText.keyListener = null
            eventDateEditText.setOnClickListener {
                showDialog()
            }
            pointLocation.setOnClickListener {
                startActivity(Intent(this@CreateEventActivity, LocationActivity::class.java))
            }

            btnAddPhoto.setOnClickListener {
                startGallery()
            }
            btnCreate.setOnClickListener {
                createCampaign()
            }
        }
        inputValidation()
        setButtonEnable()

    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@CreateEventActivity)
            getFile = myFile
            binding.btnAddPhoto.setImageURI(selectedImg)
        }
    }


    private fun inputValidation() {
        binding.apply {
            eventNameEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty()) {
                        eventNameEditTextLayout.error = getString(R.string.fill_blank)
                    } else {
                        eventNameEditTextLayout.error = null
                        setButtonEnable()
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            eventDescriptionEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        setButtonEnable()
                        eventDescriptionEditText.error = null
                    } else {
                        eventDescriptionEditText.error = getString(R.string.fill_blank)
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
            eventContactEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().trim().isValidPhoneNumber()) {
                        setButtonEnable()
                        eventContactEditTextLayout.error = null
                    } else {
                        eventContactEditTextLayout.error =
                            getString(R.string.error_phone)
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
            eventLocationEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        setButtonEnable()
                        eventLocationEditTextLayout.error = null
                    } else {
                        eventLocationEditTextLayout.error =
                            getString(R.string.fill_blank)
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            eventWAEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        eventWAEditTextLayout.error = null
                        setButtonEnable()
                    } else {
                        eventWAEditTextLayout.error =
                            getString(R.string.fill_blank)
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
        }
    }

    private fun createCampaign() {
        binding.apply {
            val file = reduceFileImage(getFile as File)
            val title = eventNameEditText.text.toString().toRequestBody("text/plain".toMediaType())
            val contact =
                eventContactEditText.text.toString().toRequestBody("text/plain".toMediaType())
            val description =
                eventDescriptionEditText.text.toString().toRequestBody("text/plain".toMediaType())
            val date = eventDateEditText.text.toString().toRequestBody("text/plain".toMediaType())
            val location =
                eventLocationEditText.text.toString().toRequestBody("text/plain".toMediaType())
            val link = eventWAEditText.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photoEvent", file.name, requestImageFile
            )

                    lifecycleScope.launch {
                        launch {
                            viewModel.createEvent(
                                token,
                                title,
                                name.toRequestBody("text/plain".toMediaType()),
                                id.toRequestBody("text/plain".toMediaType()),
                                lat.toRequestBody("text/plain".toMediaType()),
                                lng.toRequestBody("text/plain".toMediaType()),
                                contact,
                                description,
                                date,
                                location,
                                link,
                                imageMultipart
                            ).collect {
                                when (it) {
                                    is Resource.Success -> {
                                        showLoading(false)
                                        Toast.makeText(
                                            this@CreateEventActivity,
                                            it.data.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                        finish()
                                        viewModel.delete()
                                    }

                                    is Resource.Error -> {
                                        showLoading(false)
                                        Toast.makeText(
                                            this@CreateEventActivity,
                                            resources.getString(R.string.create_event_failed),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                    is Resource.Loading -> {
                                        showLoading(true)
                                    }
                                }
                            }
                        }
                    }
        }
    }

    private fun setButtonEnable(){
        binding.apply {
            btnCreate.isEnabled = name.isNotEmpty() && title.toString().isNotEmpty() && location.isNotEmpty() && getFile != null&& eventNameEditText.error == null && eventContactEditTextLayout.error == null && eventDescriptionEditTextLayout.error == null && eventLocationEditTextLayout.error == null && eventWAEditTextLayout.error == null && eventContactEditTextLayout.error == null
        }
    }

    private fun showDialog() {
        val newCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                binding.eventDateEditText.setText(dateFormatter.format(newDate.time))
            }, newCalendar[Calendar.YEAR], newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            else -> {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}