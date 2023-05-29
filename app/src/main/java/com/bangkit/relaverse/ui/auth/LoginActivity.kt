package com.bangkit.relaverse.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.databinding.ActivityLoginBinding
import com.bangkit.relaverse.ui.ViewModelFactory
import com.bangkit.relaverse.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginUser()
        toRegister()
    }

    private fun loginUser() {
        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.apply {
                login(email, password)

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    loginResponse.observe(this@LoginActivity) { result ->
                        when (result) {
                            is Resource.Loading -> {
                                showLoading(true)
                            }

                            is Resource.Error -> {
                                showLoading(false)
                                showToast(result.error.toString())
                            }

                            is Resource.Success -> {
                                showLoading(false)
                                showToast(result.data?.message.toString())
                                saveToken(result.data?.token.toString(),result.data?.user?.id.toString())
                                Log.d("IDS",result.data?.user?.id.toString())
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finishAffinity()
                            }
                        }
                    }
                }
            }

        }
    }

    private fun toRegister() {
        binding.btnToRegis.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
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