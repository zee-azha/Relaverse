package com.bangkit.relaverse.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.relaverse.R
import com.bangkit.relaverse.data.utils.Resource
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
        binding.apply {
            btnToLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }

            btnRegister.setOnClickListener {
                Register()
            }
        }
    }

    private fun Register() {
        binding.apply {
            val name = nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val phoneNumber = phoneEditText.text.toString().trim()
            lifecycleScope.launch {
                if (job.isActive) job.cancel()
                job = launch {
                    viewModel.register(name, phoneNumber, email, password).collect {
                        when (it) {
                            is Resource.Success -> {
                                showLoading(false)
                                Toast.makeText(
                                    this@RegisterActivity, it.data!!.message, Toast.LENGTH_LONG
                                ).show()
                                intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            is Resource.Error -> {
                                showLoading(false)
                                Toast.makeText(
                                    this@RegisterActivity,
                                    resources.getString(R.string.regist_error_message),
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}