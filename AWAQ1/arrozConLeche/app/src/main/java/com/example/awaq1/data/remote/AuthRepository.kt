package com.example.awaq1.data.remote

import com.example.awaq1.data.local.TokenManager
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) {
    suspend fun signIn(email: String, password: String) {
        val response = apiService.signIn(AuthRequest(email, password))
        if (response.isSuccessful && response.body() != null) {
            val token = response.body()!!.token
            tokenManager.saveToken(token)
        } else {
            throw Exception("Credenciales inválidas o error del servidor.")
        }
    }
    suspend fun signOut() {
        tokenManager.deleteToken()
    }

    fun getAuthToken(): Flow<String?> {
        return tokenManager.token
    }

}