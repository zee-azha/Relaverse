package com.bangkit.relaverse.ui.main.campaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.relaverse.R
import com.bangkit.relaverse.data.remote.response.Campaign
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.data.utils.withDateFormat
import com.bangkit.relaverse.databinding.ActivityDetailsCampaignBinding
import com.bangkit.relaverse.ui.main.home.MapsActivity
import com.bangkit.relaverse.viewmodel.DetailViewModel
import com.bangkit.relaverse.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailsCampaign : AppCompatActivity() {


    private lateinit var binding: ActivityDetailsCampaignBinding
    var token: String = ""
    private var campaignId: Int? = null
    private var code: Int? = null
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            launch {
                viewModel.getAuth().collect { tokenID ->
                    if (!tokenID.isNullOrEmpty()) token = tokenID
                }
            }
        }
        binding.apply {


            btnListVolunteer.setOnClickListener {
                val intent = Intent(this@DetailsCampaign, ListUserActivity::class.java)
                intent.putExtra(ListUserActivity.CAMPAIGN_ID, campaignId)
                startActivity(intent)
            }
            loadDetail()
            code = intent.getIntExtra(EXTRA_CODE, 0)

            isOwner()
        }
    }


    private fun isOwner() {
        binding.apply {
            if (code == 0) {
                btnDelete.alpha = 1F
                btnLeave.isEnabled = false
                btnDelete.setOnClickListener {
                    val dialogTitle = getString(R.string.delete)
                    val dialogMessage = getString(R.string.message_delete)
                    val alertDialogBuilder = AlertDialog.Builder(this@DetailsCampaign)
                    with(alertDialogBuilder) {
                        setTitle(dialogTitle)
                        setMessage(dialogMessage)
                        setCancelable(false)
                        setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
                        setPositiveButton(getString(R.string.yes)) { _, _ ->
                            deleteCampaign()
                        }
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }


            } else {
                btnLeave.alpha = 1F
                btnDelete.isEnabled = false
                btnLeave.setOnClickListener {
                    val dialogTitle = getString(R.string.leave)
                    val dialogMessage = getString(R.string.message_leave)
                    val alertDialogBuilder = AlertDialog.Builder(this@DetailsCampaign)
                    with(alertDialogBuilder) {
                        setTitle(dialogTitle)
                        setMessage(dialogMessage)
                        setCancelable(false)
                        setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
                        setPositiveButton(getString(R.string.yes)) { _, _ ->
                            leaveCampaign()
                        }
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }


            }
        }

    }


    private fun leaveCampaign() {
        lifecycleScope.launch {
            viewModel.leaveCampaign(token, campaignId!!).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        showToast(getString(R.string.failed_leave_campaign))
                    }

                    is Resource.Success -> {
                        showToast(getString(R.string.leaved))
                        finish()
                    }
                }
            }
        }
    }


    private fun deleteCampaign() {
        lifecycleScope.launch {
            viewModel.deleteCampaign(token, campaignId!!).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        showToast(getString(R.string.failed_delete_campaign))
                    }

                    is Resource.Success -> {
                        showToast(getString(R.string.deleted))
                        finish()
                    }
                }
            }


        }
    }

    private fun loadDetail() {
        campaignId = intent.getIntExtra(CAMPAIGN_ID, 0)
        viewModel.apply {
            getDetailCampaignById(token, campaignId!!)

            detailHomeResponse.observe(this@DetailsCampaign) { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        showToast(getString(R.string.failed_join_campaign))
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
            Glide.with(this@DetailsCampaign)
                .load(campaign.photoEvent)
                .into(imageCampaign)

            tvCampaignTitle.text = campaign.title
            date.text = campaign.date.withDateFormat()
            tvUser.text = campaign.name
            desc.text = campaign.description
            location.text = campaign.location
            phoneNumber.text = campaign.contact

            openMap.setOnClickListener {
                openMaps(campaign.lat, campaign.lon, campaign.title, campaign.location)
            }

            phoneNumber.setOnClickListener {
                intentOpenWA(getString(R.string.wa_link, campaign.contact))
            }

        }
    }

    private fun intentOpenWA(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun openMaps(lat: Float?, lon: Float?, title: String, address: String) {
        startActivity(
            Intent(this@DetailsCampaign, MapsActivity::class.java)
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
        const val EXTRA_CODE = "CODE"
    }
}