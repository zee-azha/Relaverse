package com.bangkit.relaverse.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.relaverse.R
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.data.utils.isValidEmail
import com.bangkit.relaverse.data.utils.isValidPassword
import com.bangkit.relaverse.data.utils.isValidPhoneNumber
import com.bangkit.relaverse.databinding.ActivityRegisterBinding
import com.bangkit.relaverse.ui.ViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: AuthViewModel by viewModels { factory }
    private var job: Job = Job()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)
        inputValidation()
        binding.apply {
            btnToLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }

            btnRegister.setOnClickListener {
                register()
            }
        }
    }

    private fun register() {
        binding.apply {
            val name = nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val phoneNumber = phoneEditText.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && phoneNumber.isNotEmpty()) {
                if (nameEditTextLayout.error == null && emailEditTextLayout.error == null && passwordEditTextLayout.error == null && phoneEditTextLayout.error == null) {
                    lifecycleScope.launch {
                        if (job.isActive) job.cancel()
                        job = launch {
                            viewModel.register(name, phoneNumber, email, password).collect {
                                when (it) {
                                    is Resource.Success -> {
                                        showLoading(false)
                                        showToast(it.data.message)
                                        intent =
                                            Intent(this@RegisterActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }

                                    is Resource.Error -> {
                                        showLoading(false)
                                        showToast(getString(R.string.regist_error_message))
                                    }

                                    is Resource.Loading -> {
                                        showLoading(true)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                showToast(getString(R.string.fill_blank))
            }

        }
    }

    private fun inputValidation() {
        binding.apply {
            nameEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty()) {
                        nameEditTextLayout.error = getString(R.string.fill_blank)
                    } else {
                        nameEditTextLayout.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            emailEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().trim().isValidEmail()) {
                        emailEditTextLayout.error = null
                    } else {
                        emailEditTextLayout.error = getString(R.string.error_email)
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            binding.passwordEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().trim().isValidPassword()) {
                        passwordEditTextLayout.error = null
                    } else {
                        passwordEditTextLayout.error = getString(R.string.error_password, s?.length)
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            phoneEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().trim().isValidPhoneNumber()) {
                        phoneEditTextLayout.error = null
                    } else {
                        phoneEditTextLayout.error =
                            getString(R.string.error_phone)
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}