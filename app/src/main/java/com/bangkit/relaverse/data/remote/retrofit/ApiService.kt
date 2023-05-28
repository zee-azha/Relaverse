package com.bangkit.relaverse.data.remote.retrofit

import com.bangkit.relaverse.data.remote.response.LoginResponse
import com.bangkit.relaverse.data.remote.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
        suspend fun register(
        @Field("name") name: String,
        @Field("phone_number") phoneNumber: String,
        @Field("email") email: String,
        @Field("password") password: String
        ):RegisterResponse
}