package com.bangkit.relaverse.data.remote.retrofit

import com.bangkit.relaverse.data.remote.response.CampaignResponse
import com.bangkit.relaverse.data.remote.response.CreateCampaignResponse
import com.bangkit.relaverse.data.remote.response.DefaultResponse
import com.bangkit.relaverse.data.remote.response.DeleteCampaignResponse
import com.bangkit.relaverse.data.remote.response.DetailResponse
import com.bangkit.relaverse.data.remote.response.LocationResponse
import com.bangkit.relaverse.data.remote.response.LoginResponse
import com.bangkit.relaverse.data.remote.response.ProfileResponse
import com.bangkit.relaverse.data.remote.response.UserListResponse
import com.bangkit.relaverse.data.remote.response.VolunteerResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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
        @Field("password") password: String,
    ): DefaultResponse

    @FormUrlEncoded
    @PUT("users/change-location/{id}")
    suspend fun changeLocation(
        @Header("Authorization") auth: String,
        @Path("id") id: Int,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
    ): LocationResponse

    @GET("campaign/all")
    suspend fun getCampaign(
        @Header("Authorization") token: String,
    ): Response<CampaignResponse>

    @GET("campaign/{campaignId}")
    suspend fun getCampaignById(
        @Header("Authorization") auth: String,
        @Path("campaignId") campaignId: Int,
    ): DetailResponse

    @GET("campaign/volunteer/{userId}")
    suspend fun getVolunteerByUserId(
        @Header("Authorization") auth: String,
        @Path("userId") userId: Int,
    ): VolunteerResponse

    @GET("campaign/joined/{eventId}")
    suspend fun getVolunteerUser(
        @Header("Authorization") auth: String,
        @Path("eventId") campaignId: Int,
    ): Response<UserListResponse>

    @GET("campaign/my-campaigns/{userId}")
    suspend fun getCampaignByUserId(
        @Header("Authorization") auth: String,
        @Path("userId") userId: Int,
    ): Response<CampaignResponse>

    @FormUrlEncoded
    @POST("campaign/volunteer/{campaignId}")
    suspend fun joinCampaign(
        @Header("Authorization") auth: String,
        @Path("campaignId") campaignId: Int,
        @Field("user_Id") userId: Int,
    ): DefaultResponse

    @GET("users/{userId}")
    suspend fun getUserById(
        @Header("Authorization") auth: String,
        @Path("userId") userId: Int,
    ): ProfileResponse

    @Multipart
    @POST("campaign")
    suspend fun createCampaign(
        @Header("Authorization") auth: String,
        @Part("title") title: RequestBody,
        @Part("name") name: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("contact") contact: RequestBody,
        @Part("description") description: RequestBody,
        @Part("date") date: RequestBody,
        @Part("location") location: RequestBody,
        @Part("whatsappLink") link: RequestBody,
        @Part file: MultipartBody.Part,
    ): CreateCampaignResponse


    @FormUrlEncoded
    @PUT("users/edit-profile/{userId}")
    suspend fun editProfile(
        @Header("Authorization") auth: String,
        @Path("userId") userId: Int,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phoneNumber") phoneNumber: String,
    ): DefaultResponse

    @FormUrlEncoded
    @PUT("users/change-password/{userId}")
    suspend fun changePassword(
        @Header("Authorization") auth: String,
        @Path("userId") userId: Int,
        @Field("currentPassword") currentPassword: String,
        @Field("newPassword") newPassword: String,
    ): DefaultResponse

    @DELETE("campaign/delete/{campaignId}")
    suspend fun deleteCampaign(
        @Header("Authorization") auth: String,
        @Path("campaignId") campaignId: Int,
    ): DeleteCampaignResponse

    @DELETE("campaign/leavecampaign/{campaignId}")
    suspend fun leaveCampaign(
        @Header("Authorization") auth: String,
        @Path("campaignId") campaignId: Int,
    ): DeleteCampaignResponse
}