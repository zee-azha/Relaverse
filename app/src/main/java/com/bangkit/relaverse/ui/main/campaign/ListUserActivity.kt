package com.bangkit.relaverse.ui.main.campaign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.relaverse.R
import com.bangkit.relaverse.data.remote.response.CampaignData
import com.bangkit.relaverse.data.utils.CampaignAdapter
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.data.utils.UserAdapter
import com.bangkit.relaverse.databinding.ActivityCreateEventBinding
import com.bangkit.relaverse.databinding.ActivityListUserBinding
import com.bangkit.relaverse.ui.ViewModelFactory
import com.bangkit.relaverse.ui.main.DetailViewModel
import com.bangkit.relaverse.ui.main.home.DetailsHomeActivity
import kotlinx.coroutines.launch

class ListUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListUserBinding
    var token: String = ""
    var campaignId : Int? = null
    private lateinit var userAdapter: UserAdapter
    private val viewModel by viewModels<ListUserViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        campaignId = intent.getIntExtra(CAMPAIGN_ID, 0)
        lifecycleScope.launch {
            launch {
                viewModel.getAuth().collect { tokenID ->
                    if (!tokenID.isNullOrEmpty()) token = tokenID
                }
            }
        }
        setAdapter()
        getUser()
    }

    private fun getUser() {
        viewModel.listUserVolunteer(token,campaignId!!.toInt())
        viewModel.userListResponse.observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    result.data.user.let { hasil ->
                        userAdapter.submitList(hasil)
                        Log.d("bangke",hasil.toString())
                    }
                }

                is Resource.Loading -> {

                }

                is Resource.Error -> {

                }
            }


        }
    }

    private fun setAdapter() {
        userAdapter = UserAdapter()
        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@ListUserActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = userAdapter

        }
    }

    companion object {
        const val CAMPAIGN_ID: String = "campaignId"
    }
}