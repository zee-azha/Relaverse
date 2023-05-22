package com.bangkit.relaverse.ui.create_event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.relaverse.R
import com.bangkit.relaverse.databinding.ActivityCreateEventBinding

class CreateEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}