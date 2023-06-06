package com.bangkit.relaverse.ui.create_event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.relaverse.data.RelaverseRepository
import com.bangkit.relaverse.data.remote.response.CreateCampaignResponse
import com.bangkit.relaverse.data.remote.response.ProfileResponse
import com.bangkit.relaverse.data.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody


class CreateEventViewModel (
    private val repository: RelaverseRepository,
) : ViewModel(){



    suspend fun createEvent(token: String,
                            title: RequestBody,
                            name: RequestBody,
                            userId: RequestBody,
                            latitude: RequestBody,
                            longitude: RequestBody,
                            contact: RequestBody,
                            description: RequestBody,
                            date: RequestBody,
                            location:RequestBody,
                            link:RequestBody,
                            photoEvent: MultipartBody.Part): Flow<Resource<CreateCampaignResponse>>
    = repository.addCampaign(token,photoEvent,title,name,userId,latitude,longitude,contact,description,date,location,link)

    fun getId(): Flow<String?> = repository.getId()
    fun getToken(): Flow<String?> = repository.getToken()

    suspend fun getUserById(
        token: String,
        userId: Int,
    ):Flow<Resource<ProfileResponse>> = repository.getUserById(token, userId)

    fun saveLoc(location: String,lat:String,lng:String){
        viewModelScope.launch {
            repository.saveLocation(location, lat, lng)
        }
    }

    fun delete(){
        viewModelScope.launch {
            repository.delete()
        }
    }
    fun getloc(): Flow<String?> = repository.getLoc()
    fun getlat(): Flow<String?> = repository.getLat()
    fun getlng(): Flow<String?> = repository.getLng()



}



