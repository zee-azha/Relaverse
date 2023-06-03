package com.bangkit.relaverse.ui.main.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.relaverse.data.remote.response.Campaign
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.databinding.ActivityDetailsHomeBinding
import com.bangkit.relaverse.ui.ViewModelFactory
import com.bangkit.relaverse.ui.main.DetailViewModel
import com.bumptech.glide.Glide

class DetailsHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsHomeBinding

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadDetail()
    }

    private fun loadDetail() {
        val campaignId = intent.getIntExtra(CAMPAIGN_ID, 0)
        viewModel.apply {
            getToken().observe(this@DetailsHomeActivity) { token ->
                if (token != null) {
                    getDetailCampaignById(token, campaignId)
                }

            }
            detailHomeResponse.observe(this@DetailsHomeActivity) { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        showToast("Failed to load Campaign")
                    }

                    is Resource.Success -> {
                        showLoading(false)
                        showToast(result.data.message)
                        showDetailData(result.data.campaign)
                    }
                }
            }
        }
    }

    private fun showDetailData(campaign: Campaign) {
        binding.apply {
            Glide.with(this@DetailsHomeActivity)
                .load(campaign.photoEvent)
                .into(imgCampaign)

            tvCampaignTitle.text = campaign.title
            date.text = campaign.date
            tvUser.text = campaign.name
            desc.text = campaign.description
            location.text = campaign.location
            phoneNumber.text = campaign.contact

            phoneNumber.setOnClickListener {
                intentOpenWA("https://wa.me/${campaign.contact}")
            }

            btnJoin.setOnClickListener {
                viewModel.apply {
                    getToken().observe(this@DetailsHomeActivity) { token ->
                        if (token != null) {
                            joinCampaign(token, campaign.id)
                        }
                    }

                }

                joinEvent()

                intentOpenWA(campaign.whatsappLink)
            }
        }
    }

    private fun intentOpenWA(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun joinEvent() {
        viewModel.joinResponse.observe(this@DetailsHomeActivity) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Error -> {
                    showLoading(false)
                    showToast("Failed to join Campaign")
                }

                is Resource.Success -> {
                    showLoading(false)
                    showToast(result.data.message)
                }
            }
        }
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val CAMPAIGN_ID = "campaignId"
    }
}