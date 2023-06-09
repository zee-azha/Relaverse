package com.bangkit.relaverse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.relaverse.data.RelaverseRepository
import com.bangkit.relaverse.data.remote.response.LoginResponse
import com.bangkit.relaverse.data.utils.Resource
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: RelaverseRepository,
) : ViewModel() {

    private val _loginResponse = MutableLiveData<Resource<LoginResponse>>()
    val loginResponse get() = _loginResponse
    fun login(
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            repository.login(email, password).collect {
                _loginResponse.value = it
            }
        }
    }

    suspend fun register(name: String, phoneNUmber: String, email: String, password: String) =
        repository.registerUser(name, phoneNUmber, email, password)

    fun saveToken(token: String, id: String) {
        viewModelScope.launch {
            repository.saveToken(token, id)
        }
    }

    fun getToken(): LiveData<String?> = repository.getToken().asLiveData()
}