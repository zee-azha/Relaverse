package com.bangkit.relaverse.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(

    @field:SerializedName("campaign")
    val campaign: Campaign,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)

data class Campaign(

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("contact")
    val contact: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("photoEvent")
    val photoEvent: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: Float? = null,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("userId")
    val userId: Int,

    @field:SerializedName("lat")
    val lat: Float? = null,

    @field:SerializedName("whatsappLink")
    val whatsappLink: String,
)
