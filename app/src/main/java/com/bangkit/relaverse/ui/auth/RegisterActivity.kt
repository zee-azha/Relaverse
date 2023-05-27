package com.bangkit.relaverse.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.relaverse.R
import com.bangkit.relaverse.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}