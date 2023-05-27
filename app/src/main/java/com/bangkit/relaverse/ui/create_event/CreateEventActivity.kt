package com.bangkit.relaverse.ui.create_event

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.relaverse.databinding.ActivityCreateEventBinding

class CreateEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}