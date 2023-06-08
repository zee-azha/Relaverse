package com.bangkit.relaverse.ui.main.campaign

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.relaverse.data.remote.response.CampaignData
import com.bangkit.relaverse.data.utils.CampaignAdapter
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.databinding.FragmentCampaignBinding
import com.bangkit.relaverse.ui.ViewModelFactory
import com.bangkit.relaverse.ui.create_event.CreateEventActivity
import com.bangkit.relaverse.ui.main.MainViewModel
import com.bangkit.relaverse.ui.main.home.DetailsHomeActivity
import kotlinx.coroutines.launch


class CampaignFragment : Fragment() {

    private lateinit var binding: FragmentCampaignBinding
    var token: String = ""
    var id: String = ""
    private lateinit var campaignAdapter: CampaignAdapter
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCampaignBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            launch {
                viewModel.getToken().collect { tokenID ->
                    if (!tokenID.isNullOrEmpty()) token = tokenID
                }
            }
            launch {
                viewModel.getId().collect { UID ->
                    if (!UID.isNullOrEmpty()) id = UID
                }
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(requireActivity(), CreateEventActivity::class.java))
        }
        getCampaign()
        setAdapter()
    }

    private fun getCampaign() {
        viewModel.getCampaignByUserId(token, id.toInt())
        viewModel.myCampaignResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    showLoading(false)
                    result.data.items.let { data ->
                        campaignAdapter.submitList(data)
                    }
                }

                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Error -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun setAdapter() {
        campaignAdapter = CampaignAdapter()

        binding.apply {
            rvCampaign.layoutManager = LinearLayoutManager(requireContext())
            rvCampaign.setHasFixedSize(true)
            rvCampaign.adapter = campaignAdapter

        }

        campaignAdapter.setOnItemClickCallback(object : CampaignAdapter.OnItemClickCallback {
            override fun onItemClicked(campaignData: CampaignData) {
                val intent = Intent(context, DetailsCampaign::class.java)
                intent.putExtra(DetailsHomeActivity.CAMPAIGN_ID, campaignData.id)
                startActivity(intent)
            }
        })
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

}