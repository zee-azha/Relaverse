package com.bangkit.relaverse.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val user: User? = null,

    @field:SerializedName("token")
    val token: String? = null,
)

data class LocationResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)

data class User(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("phone_number")
    val phoneNumber: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("latitude")
    val latitude: Float? = null,

    @field:SerializedName("longitude")
    val longitude: Float? = null,
)
