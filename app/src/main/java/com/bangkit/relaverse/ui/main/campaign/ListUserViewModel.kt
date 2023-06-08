package com.bangkit.relaverse.ui.main.campaign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.relaverse.data.RelaverseRepository
import com.bangkit.relaverse.data.remote.response.UserListResponse
import com.bangkit.relaverse.data.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ListUserViewModel(private val repository: RelaverseRepository) : ViewModel() {

    private val _userListResponse = MutableLiveData<Resource<UserListResponse>>()
    val userListResponse: LiveData<Resource<UserListResponse>> = _userListResponse

    fun getAuth(): Flow<String?> = repository.getToken()
    fun getId(): Flow<String?> = repository.getId()

    fun listUserVolunteer(token: String, id: Int) = viewModelScope.launch {
        _userListResponse.value = Resource.Loading
        try {
            val response = repository.getVolunteerUser(token, id)
            _userListResponse.value = Resource.Success(response.body()!!)
        } catch (e: Exception) {
            _userListResponse.value = Resource.Error(e.message)
        }
    }
}