package com.bangkit.relaverse.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.relaverse.data.RelaverseRepository
import com.bangkit.relaverse.data.remote.response.CampaignResponse
import com.bangkit.relaverse.data.remote.response.DetailResponse
import com.bangkit.relaverse.data.remote.response.JoinResponse
import com.bangkit.relaverse.data.remote.response.UserListResponse
import com.bangkit.relaverse.data.remote.response.VolunteerResponse
import com.bangkit.relaverse.data.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class DetailViewModel(
    private val repository: RelaverseRepository,
) : ViewModel() {

    fun getToken(): LiveData<String?> = repository.getToken().asLiveData()
    fun getAuth(): Flow<String?> = repository.getToken()
    fun getId(): Flow<String?> = repository.getId()
    private val _detailHomeResponse = MutableLiveData<Resource<DetailResponse>>()

    private val _userListResponse = MutableLiveData<Resource<UserListResponse>>()
    val userListResponse: LiveData<Resource<UserListResponse>> = _userListResponse
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

    suspend fun checkCampaign(
        token: String,
        userId: Int,
    ) = repository.getVolunteerByUserId(token, userId)

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

   fun listUserVolunteer(token: String, id: Int) = viewModelScope.launch{
        _userListResponse.value = Resource.Loading
        try {
            val response = repository.getVolunteerUser(token,id)
            _userListResponse.value = Resource.Success(response.body()!!)
        } catch (e: Exception) {
            _userListResponse.value = Resource.Error(e.message)
        }
    }
}