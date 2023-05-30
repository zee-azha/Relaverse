package com.bangkit.relaverse.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.relaverse.databinding.FragmentAboutProfileBinding


class AboutProfileFragment : Fragment() {

    private lateinit var binding: FragmentAboutProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAboutProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


}