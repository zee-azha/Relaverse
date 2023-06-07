package com.bangkit.relaverse.data.remote.response

import com.google.gson.annotations.SerializedName

data class VolunteerResponse (
    @field:SerializedName("error") val error: Boolean,

    @field:SerializedName("message") val message: String,

    @field:SerializedName("userData") val items: UserData
        )

data class UserData (
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("eventList") val list: List<EventList>
        )

data class EventList (
    @field:SerializedName("campaigntId")
    val id: Int,

    @field:SerializedName("campaignTitle")
    val title: String,

    @field:SerializedName("campaigntName")
    val name: String,

    @field:SerializedName("campaignCreatorId")
    val userId: String,

    @field:SerializedName("campaignLat")
    val latitude: Float,

    @field:SerializedName("campaignLon")
    val longitude: Float,

    @field:SerializedName("campaignContact")
    val contact: String,

    @field:SerializedName("campaignDescription")
    val description: String,

    @field:SerializedName("campaignDate")
    val date: String,

    @field:SerializedName("photoEvent")
    val campaignPhoto: String,
        )

