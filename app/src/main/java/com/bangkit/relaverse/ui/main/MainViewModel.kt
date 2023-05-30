package com.bangkit.relaverse.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.relaverse.data.RelaverseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: RelaverseRepository,
) : ViewModel() {

    fun getToken(): Flow<String?> = repository.getToken()
    fun getId(): Flow<String?> = repository.getId()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    suspend fun updateLocation(
        token: String,
        id: Int,
        lat: String,
        long: String,
    ) = repository.updateLocation(token, id, lat, long)
}


