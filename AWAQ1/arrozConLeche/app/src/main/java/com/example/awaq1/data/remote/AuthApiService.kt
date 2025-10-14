package com.example.awaq1.data.remote


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/biomo/users/login")
    suspend fun signIn(@Body authRequest: AuthRequest): Response<AuthResponse>

    @GET("api/biomo/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    @POST("biomo/users/logout")
    suspend fun logout()

}