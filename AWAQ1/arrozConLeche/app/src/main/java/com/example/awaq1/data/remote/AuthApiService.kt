package com.example.awaq1.data.remote


import com.example.awaq1.data.formularios.FormularioUnoEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/biomo/users/login")
    suspend fun signIn(@Body authRequest: AuthRequest): Response<AuthResponse>

    @GET("api/biomo/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    @POST("/api/biomo/forms/1/submission")
    suspend fun sendFormularioUno(@Body formularioEntities: FormularioUnoEntity): Response<FormularioResponse>

    @POST("biomo/users/logout")
    suspend fun logout()

}