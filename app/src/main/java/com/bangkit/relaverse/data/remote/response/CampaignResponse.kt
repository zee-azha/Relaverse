package com.bangkit.relaverse.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class CampaignResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("campaigns")
    val items: List<CampaignData>,
)

data class CreateCampaignResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)

data class DeleteCampaignResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)
@Parcelize
data class CampaignData(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("latitude")
    val latitude: Float,

    @field:SerializedName("longitude")
    val longitude: Float,

    @field:SerializedName("contact")
    val contact: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("whatsappLink")
    val link: String,

    @field:SerializedName("photoEvent")
    val photoEvent: String,
) : Parcelable