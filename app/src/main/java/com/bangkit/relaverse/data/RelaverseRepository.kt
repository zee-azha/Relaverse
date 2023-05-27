package com.bangkit.relaverse.data

import com.bangkit.relaverse.data.local.UserPreferences
import com.bangkit.relaverse.data.remote.response.LoginResponse
import com.bangkit.relaverse.data.remote.retrofit.ApiService
import com.bangkit.relaverse.data.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class RelaverseRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
) {
    suspend fun login(
        email: String,
        password: String,
    ): Flow<Resource<LoginResponse>> {
        return flow {
            emit(Resource.Loading)
            try {
                val response = apiService.login(email, password)
                emit(Resource.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    suspend fun saveToken(token: String) {
        userPreferences.saveToken(token)
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    fun getToken(): Flow<String?> = userPreferences.getToken()

}