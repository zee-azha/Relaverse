package com.bangkit.relaverse.data.remote.response

data class ProfileResponse(
    val error: Boolean,
    val message: String,
    val user: User,
)
