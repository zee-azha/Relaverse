package com.bangkit.relaverse.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    val error: Boolean,
    val message: String,
    val user: User,
)

data class UserListResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("userList")
    val user: List<User>,
)
