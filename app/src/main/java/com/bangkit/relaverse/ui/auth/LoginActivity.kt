package com.bangkit.relaverse.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.relaverse.R
import com.bangkit.relaverse.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}