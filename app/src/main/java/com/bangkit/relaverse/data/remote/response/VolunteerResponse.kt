package com.bangkit.relaverse.data.remote.response

import com.google.gson.annotations.SerializedName

data class VolunteerResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("userData")
    val items: UserData,
)

data class UserData(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("eventList")
    val list: List<CampaignData>,
)