package com.example.awaq1.data.remote


import com.example.awaq1.data.formularios.FormularioCincoEntity
import com.example.awaq1.data.formularios.FormularioCuatroEntity
import com.example.awaq1.data.formularios.FormularioDosEntity
import com.example.awaq1.data.formularios.FormularioSeisEntity
import com.example.awaq1.data.formularios.FormularioSieteEntity
import com.example.awaq1.data.formularios.FormularioTresEntity
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

    @POST("/api/biomo/forms/2/submission")
    suspend fun sendFormularioDos(@Body formularioEntities: FormularioDosEntity): Response<FormularioResponse>

    @POST("/api/biomo/forms/3/submission")
    suspend fun sendFormularioTres(@Body formularioEntities: FormularioTresEntity): Response<FormularioResponse>

    @POST("/api/biomo/forms/4/submission")
    suspend fun sendFormularioCuatro(@Body formularioEntities: FormularioCuatroEntity): Response<FormularioResponse>

    @POST("/api/biomo/forms/5/submission")
    suspend fun sendFormularioCinco(@Body formularioEntities: FormularioCincoEntity): Response<FormularioResponse>

    @POST("/api/biomo/forms/6/submission")
    suspend fun sendFormularioSeis(@Body formularioEntities: FormularioSeisEntity): Response<FormularioResponse>

    @POST("/api/biomo/forms/7/submission")
    suspend fun sendFormularioSiete(@Body formularioEntities: FormularioSieteEntity): Response<FormularioResponse>

    @POST("biomo/users/logout")
    suspend fun logout()

}