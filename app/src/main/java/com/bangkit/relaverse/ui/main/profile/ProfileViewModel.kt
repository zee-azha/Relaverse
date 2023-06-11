package com.bangkit.relaverse.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.relaverse.data.RelaverseRepository
import com.bangkit.relaverse.data.remote.response.DefaultResponse
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

    private val _editProfileResponse = MutableLiveData<Resource<DefaultResponse>>()
    val editProfileResponse get() = _profileResponse

    fun putEditProfile(
        token: String,
        userId: Int,
        email: String,
        name: String,
        phoneNumber: String,
    ) {
        viewModelScope.launch {
            repository.editProfile(token, userId, email, name, phoneNumber).collect {
                _editProfileResponse.value = it
            }
        }
    }

    private val _changePasswordResponse = MutableLiveData<Resource<DefaultResponse>>()
    val changePasswordResponse get() = _changePasswordResponse

    fun putChangePassword(
        token: String,
        userId: Int,
        currentPassword: String,
        newPassword: String,
    ) {
        viewModelScope.launch {
            repository.changePassword(token, userId, currentPassword, newPassword).collect {
                _changePasswordResponse.value = it
            }
        }
    }

    fun getToken(): LiveData<String?> = repository.getToken().asLiveData()
    fun getUserId(): LiveData<String?> = repository.getId().asLiveData()
}