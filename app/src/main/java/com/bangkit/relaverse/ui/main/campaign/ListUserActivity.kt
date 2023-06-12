package com.bangkit.relaverse.ui.main.campaign

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.data.utils.UserAdapter
import com.bangkit.relaverse.databinding.ActivityListUserBinding
import com.bangkit.relaverse.viewmodel.ListUserViewModel
import com.bangkit.relaverse.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class ListUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListUserBinding
    var token: String = ""
    private var campaignId: Int? = null
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
        viewModel.listUserVolunteer(token, campaignId!!.toInt())
        viewModel.userListResponse.observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    showLoading(false)
                    result.data.user.let { data ->
                        userAdapter.submitList(data)
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
        userAdapter = UserAdapter()
        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@ListUserActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = userAdapter

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

    companion object {
        const val CAMPAIGN_ID: String = "campaignId"
    }
}