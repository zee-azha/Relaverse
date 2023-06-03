package com.bangkit.relaverse.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.relaverse.data.RelaverseRepository
import com.bangkit.relaverse.data.remote.response.ProfileResponse
import com.bangkit.relaverse.data.utils.Resource
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: RelaverseRepository,
) : ViewModel() {

    private val _profileResponse = MutableLiveData<Resource<ProfileResponse>>()
    val profileResponse get() = _profileResponse

    fun getUserById(
        token: String,
        userId: Int,
    ) {
        viewModelScope.launch {
            repository.getUserById(token, userId).collect {
                _profileResponse.value = it
            }
        }
    }

    fun getToken(): LiveData<String?> = repository.getToken().asLiveData()
    fun getUserId(): LiveData<String?> = repository.getId().asLiveData()
}