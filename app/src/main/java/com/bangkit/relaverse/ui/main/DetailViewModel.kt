package com.bangkit.relaverse.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.relaverse.data.RelaverseRepository
import com.bangkit.relaverse.data.remote.response.DetailResponse
import com.bangkit.relaverse.data.remote.response.JoinResponse
import com.bangkit.relaverse.data.utils.Resource
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: RelaverseRepository,
) : ViewModel() {

    fun getToken(): LiveData<String?> = repository.getToken().asLiveData()

    private val _detailHomeResponse = MutableLiveData<Resource<DetailResponse>>()
    val detailHomeResponse get() = _detailHomeResponse

    fun getDetailCampaignById(
        token: String,
        campaignId: Int,
    ) {
        viewModelScope.launch {
            repository.getDetailCampaignById(token, campaignId).collect {
                _detailHomeResponse.value = it
            }
        }
    }

    private val _joinResponse = MutableLiveData<Resource<JoinResponse>>()
    val joinResponse get() = _joinResponse

    fun joinCampaign(
        token: String,
        campaignId: Int,
        userId: Int,
    ) {
        viewModelScope.launch {
            repository.joinCampaign(token, campaignId, userId).collect {
                _joinResponse.value = it
            }
        }
    }
}