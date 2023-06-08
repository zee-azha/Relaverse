package com.bangkit.relaverse.ui.main.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.relaverse.data.remote.response.Campaign
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.databinding.ActivityDetailsHomeBinding
import com.bangkit.relaverse.ui.ViewModelFactory
import com.bangkit.relaverse.ui.main.DetailViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import kotlin.math.log

class DetailsHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsHomeBinding

    var token: String = ""
    var id: String = ""
    var campaignId : Int? = null
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            launch {
                viewModel.getAuth().collect { tokenID ->
                    if (!tokenID.isNullOrEmpty()) token = tokenID
                }
            }
            launch {
                viewModel.getId().collect { UID ->
                    if (!UID.isNullOrEmpty()) id = UID
                }
            }

        }
        checkCampaignUser()
        loadDetail()

    }

    private fun loadDetail() {
         campaignId = intent.getIntExtra(CAMPAIGN_ID, 0)
        viewModel.apply {
            getToken().observe(this@DetailsHomeActivity) { token ->
                if (token != null) {
                    getDetailCampaignById(token, campaignId!!)

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

            openMap.setOnClickListener {
                openMaps(campaign.lat, campaign.lon, campaign.title, campaign.location)
            }

            phoneNumber.setOnClickListener {
                intentOpenWA("https://wa.me/62${campaign.contact}")
            }

            btnJoin.setOnClickListener {
                viewModel.apply {
                    getToken().observe(this@DetailsHomeActivity) { token ->
                        if (token != null) {
                            joinCampaign(token, campaign.id, campaign.userId)
                        }
                    }
                    joinEvent()
                }

                if (campaign.whatsappLink.isNotEmpty()) {
                    intentOpenWA(campaign.whatsappLink)
                }
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

    private fun checkCampaignUser(){

               lifecycleScope.launch {
                   viewModel.checkCampaign(token,id.toInt()).collect{
                       when(it){
                           is Resource.Loading -> {
                               showLoading(true)

                           }

                           is Resource.Error -> {
                               showLoading(false)
                           }

                           is Resource.Success ->{
                               it.data.items.let{  data->
                                   var j  = 0

                                   for (i in data.list.indices) {
                                       if (campaignId.toString() != data.list[j].id.toString()) {

                                           binding.btnJoin.isEnabled = true

                                          j++

                                       } else {
                                           binding.btnJoin.isEnabled = false
                                       }


                                   }

                               }

                           }
                       }

                   }
               }





    }

    private fun openMaps(lat: Float?, lon: Float?, title: String, address: String) {
        startActivity(
            Intent(this@DetailsHomeActivity, MapsActivity::class.java)
                .apply {
                    putExtra(MapsActivity.LAT, lat!!)
                    putExtra(MapsActivity.LON, lon!!)
                    putExtra(MapsActivity.TITLE, title)
                    putExtra(MapsActivity.ADDRESS, address)
                }
        )
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