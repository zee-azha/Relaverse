package com.bangkit.relaverse.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.relaverse.data.RelaverseRepository
import com.bangkit.relaverse.data.remote.response.CampaignResponse
import com.bangkit.relaverse.data.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: RelaverseRepository,
) : ViewModel() {

    private val _campaignResponse = MutableLiveData<Resource<CampaignResponse>>()
    private val _myCampaignResponse = MutableLiveData<Resource<CampaignResponse>>()
    val campaignResponse: LiveData<Resource<CampaignResponse>> = _campaignResponse
    val myCampaignResponse: LiveData<Resource<CampaignResponse>> = _myCampaignResponse


    fun getToken(): Flow<String?> = repository.getToken()
    fun getId(): Flow<String?> = repository.getId()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
    fun getCampaignByUserId(token: String,id: Int) = viewModelScope.launch{
        _myCampaignResponse.value = Resource.Loading
        try {
            val response = repository.getCampaignByUserId(token, id)
            _myCampaignResponse.value = Resource.Success(response.body()!!)
        }catch (e: Exception){
            _myCampaignResponse.value = Resource.Error(e.message)
            Log.d("gagal",e.message.toString())
        }
    }
    fun getAllCampaign(token: String) = viewModelScope.launch {
        _campaignResponse.value = Resource.Loading
        try {
            val response = repository.getAllCampaign(token)
            _campaignResponse.value = Resource.Success(response.body()!!)
        } catch (e: Exception) {
            _campaignResponse.value = Resource.Error(e.message)
        }
    }

    suspend fun updateLocation(
        token: String,
        id: Int,
        lat: String,
        long: String,
    ) = repository.updateLocation(token, id, lat, long)
}


