package com.bangkit.relaverse.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.relaverse.R
import com.bangkit.relaverse.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}