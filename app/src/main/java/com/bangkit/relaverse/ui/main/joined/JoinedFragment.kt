package com.bangkit.relaverse.ui.main.joined

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.relaverse.R
import com.bangkit.relaverse.data.remote.response.CampaignData
import com.bangkit.relaverse.data.utils.CampaignAdapter
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.databinding.FragmentJoinedBinding
import com.bangkit.relaverse.ui.ViewModelFactory
import com.bangkit.relaverse.ui.main.campaign.DetailsCampaign
import com.bangkit.relaverse.ui.main.home.DetailsHomeActivity


class JoinedFragment : Fragment() {

    private lateinit var binding: FragmentJoinedBinding
    private lateinit var campaignAdapter: CampaignAdapter
    private val viewModel by viewModels<JoinedViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentJoinedBinding.inflate(inflater, container, false)
        setAdapter()
        getJoinedCampaign()
        return binding.root
    }

    private fun getJoinedCampaign() {
        viewModel.apply {
            getToken().observe(viewLifecycleOwner) { token ->
                getUserId().observe(viewLifecycleOwner) { userId ->
                    if (token != null && userId != null) {
                        getJoinedCampaignByUserId(token, userId.toInt())
                    }
                }
                joinedResponse.observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Resource.Loading -> {
                            showLoading(true)
                        }

                        is Resource.Error -> {
                            showLoading(false)
                            showToast(getString(R.string.failed_to_load_campaign))
                        }

                        is Resource.Success -> {
                            showLoading(false)
                            campaignAdapter.submitList(result.data.items.list)
                        }
                    }
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

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}