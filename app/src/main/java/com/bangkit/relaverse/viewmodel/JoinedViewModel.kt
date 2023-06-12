package com.bangkit.relaverse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.relaverse.data.RelaverseRepository
import com.bangkit.relaverse.data.remote.response.VolunteerResponse
import com.bangkit.relaverse.data.utils.Resource
import kotlinx.coroutines.launch

class JoinedViewModel(
    private val repository: RelaverseRepository,
) : ViewModel() {

    private val _joinedResponse = MutableLiveData<Resource<VolunteerResponse>>()
    val joinedResponse get() = _joinedResponse

    fun getJoinedCampaignByUserId(
        token: String,
        userId: Int,
    ) {
        viewModelScope.launch {
            repository.getVolunteerByUserId(token, userId).collect {
                _joinedResponse.value = it
            }
        }
    }

    fun getToken(): LiveData<String?> = repository.getToken().asLiveData()
    fun getUserId(): LiveData<String?> = repository.getId().asLiveData()
}