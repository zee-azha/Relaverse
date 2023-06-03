package com.bangkit.relaverse.data

import com.bangkit.relaverse.data.local.UserPreferences
import com.bangkit.relaverse.data.remote.response.DetailResponse
import com.bangkit.relaverse.data.remote.response.JoinResponse
import com.bangkit.relaverse.data.remote.response.LocationResponse
import com.bangkit.relaverse.data.remote.response.LoginResponse
import com.bangkit.relaverse.data.remote.response.RegisterResponse
import com.bangkit.relaverse.data.remote.retrofit.ApiService
import com.bangkit.relaverse.data.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


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

    suspend fun registerUser(
        name: String, phoneNumber: String, email: String, password: String,
    ): Flow<Resource<RegisterResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.register(name, phoneNumber, email, password)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateLocation(
        token: String, id: Int, lat: String, long: String,
    ): Flow<Resource<LocationResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.changeLocation(token, id, lat, long)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getAllCampaign(
        token: String
    ) = apiService.getCampaign(token)

    suspend fun saveToken(token: String, id: String) {
        userPreferences.saveToken(token, id)
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    fun getToken(): Flow<String?> = userPreferences.getToken()

    fun getId(): Flow<String?> = userPreferences.getId()

    suspend fun getDetailCampaignById(
        token: String,
        campaignId: Int,
    ): Flow<Resource<DetailResponse>> {
        return flow {
            emit(Resource.Loading)
            try {
                val response = apiService.getCampaignById(token, campaignId)
                emit(Resource.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    suspend fun joinCampaign(
        token: String,
        campaignId: Int,
    ): Flow<Resource<JoinResponse>> {
        return flow {
            emit(Resource.Loading)
            try {
                val response = apiService.joinCampaign(token, campaignId)
                emit(Resource.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

}