package com.bangkit.relaverse.ui.main.campaign

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.relaverse.databinding.FragmentCampaignBinding
import com.bangkit.relaverse.ui.create_event.CreateEventActivity


class CampaignFragment : Fragment() {

    private lateinit var binding: FragmentCampaignBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCampaignBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(requireActivity(), CreateEventActivity::class.java))
        }
    }

}