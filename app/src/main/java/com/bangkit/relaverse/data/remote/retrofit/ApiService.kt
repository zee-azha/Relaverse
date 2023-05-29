package com.bangkit.relaverse.data.remote.retrofit

import com.bangkit.relaverse.data.remote.response.LocationResponse
import com.bangkit.relaverse.data.remote.response.LoginResponse
import com.bangkit.relaverse.data.remote.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @FormUrlEncoded
    @PUT("users/change-location/{id}")
    suspend fun changeLocation(
    @Header("Authorization") auth: String,
    @Path("id") id: Int,
    @Field("latitude") latitude: String,
    @Field("longitude") longitude: String
    ):LocationResponse
}