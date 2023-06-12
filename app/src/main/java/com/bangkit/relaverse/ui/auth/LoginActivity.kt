package com.bangkit.relaverse.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.relaverse.R
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.data.utils.isValidEmail
import com.bangkit.relaverse.data.utils.isValidPassword
import com.bangkit.relaverse.databinding.ActivityLoginBinding
import com.bangkit.relaverse.ui.main.MainActivity
import com.bangkit.relaverse.viewmodel.AuthViewModel
import com.bangkit.relaverse.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputValidation()
        loginUser()
        toRegister()
    }

    private fun loginUser() {
        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.apply {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    if (binding.emailEditTextLayout.error == null && binding.passwordEditTextLayout.error == null) {
                        login(email, password)
                        loginResponse.observe(this@LoginActivity) { result ->
                            when (result) {
                                is Resource.Loading -> {
                                    showLoading(true)
                                }

                                is Resource.Error -> {
                                    showLoading(false)
                                    Toast.makeText(
                                        this@LoginActivity,
                                        resources.getString(R.string.login_error_message),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                is Resource.Success -> {
                                    showLoading(false)
                                    showToast(result.data.message)
                                    saveToken(
                                        result.data.token.toString(),
                                        result.data.user?.id.toString()
                                    )

                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            MainActivity::class.java
                                        )
                                    )
                                    finishAffinity()
                                }
                            }
                        }
                    }
                } else {
                    showToast(getString(R.string.fill_blank))
                }
            }

        }
    }

    private fun toRegister() {
        binding.btnToRegis.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun inputValidation() {
        binding.apply {
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

            passwordEditText.addTextChangedListener(object : TextWatcher {
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
        }
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