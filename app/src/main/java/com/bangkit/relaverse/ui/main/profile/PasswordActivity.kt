package com.bangkit.relaverse.ui.main.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.relaverse.R
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.data.utils.isValidPassword
import com.bangkit.relaverse.databinding.ActivityPasswordBinding
import com.bangkit.relaverse.viewmodel.ProfileViewModel
import com.bangkit.relaverse.viewmodel.ViewModelFactory
import com.google.android.material.textfield.TextInputLayout

class PasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordBinding

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputPassword()
    }

    private fun inputPassword() {
        binding.apply {
            inputChecker(currentPasswordEditText, currentPasswordEditTextLayout)
            inputChecker(newPasswordEditText, newPasswordEditTextLayout)
            inputChecker(newConfirmPasswordEditText, newConfirmPasswordEditTextLayout)

            btnChangePassword.setOnClickListener {
                if (currentPasswordEditText.text.toString()
                        .isNotEmpty() && newPasswordEditText.text.toString()
                        .isNotEmpty() && newConfirmPasswordEditText.text.toString().isNotEmpty()
                ) {
                    if (newPasswordEditText.text.toString() == newConfirmPasswordEditText.text.toString()) {
                        changePassword(
                            currentPasswordEditText.text.toString().trim(),
                            newConfirmPasswordEditText.text.toString().trim()
                        )
                    } else {
                        showToast(getString(R.string.confirm_same_password))
                    }
                } else {
                    showToast(getString(R.string.fill_blank))
                }

            }


        }
    }

    private fun changePassword(currentPassword: String, newPassword: String) {
        viewModel.apply {
            getToken().observe(this@PasswordActivity) { token ->
                getUserId().observe(this@PasswordActivity) { userId ->
                    if (token != null && userId != null) {
                        putChangePassword(
                            token,
                            userId.toInt(),
                            currentPassword,
                            newPassword
                        )
                    }
                }
            }

            changePasswordResponse.observe(this@PasswordActivity) { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        showToast(getString(R.string.failed_change_password))
                    }

                    is Resource.Success -> {
                        showLoading(false)
                        showToast(getString(R.string.success_change_password))
                        finish()
                    }
                }
            }
        }
    }

    private fun inputChecker(view: EditText, layout: TextInputLayout) {
        view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isValidPassword()) {
                    layout.error = null
                } else {
                    layout.error = getString(R.string.error_password, s?.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}