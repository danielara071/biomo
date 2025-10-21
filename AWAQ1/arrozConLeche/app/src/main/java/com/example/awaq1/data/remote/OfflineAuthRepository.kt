package com.example.awaq1.data.remote

import com.example.awaq1.data.local.TokenManager
import com.example.awaq1.data.usuario.UsuariosRepository
import com.example.awaq1.data.usuario.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDateTime
import java.util.UUID

class OfflineAuthRepository(
    private val tokenManager: TokenManager,
    private val usuariosRepository: UsuariosRepository
) {
    suspend fun signInOffline(offlineKey: String): Boolean {
        // Simple validation - you can make this more sophisticated
        if (offlineKey.length >= 8) {
            // Find user by offline key
            val userId = usuariosRepository.getUserIdByOfflineKey(offlineKey)
            if (userId != null) {
                tokenManager.saveOfflineKey(offlineKey)
                tokenManager.setOfflineMode(true)
                return true
            }
        }
        return false
    }

    suspend fun signInOfflineWithUsername(offlineKey: String): String? {
        // Simple validation - you can make this more sophisticated
        if (offlineKey.length >= 8) {
            // Find user by offline key
            val userId = usuariosRepository.getUserIdByOfflineKey(offlineKey)
            if (userId != null) {
                // Get the actual username for this user
                val userFlow = usuariosRepository.getUserById(userId)
                // We need to get the first value from the flow
                return try {
                    userFlow.firstOrNull()?.username
                } catch (e: Exception) {
                    null
                }
            }
        }
        return null
    }

    suspend fun signOutOffline() {
        tokenManager.setOfflineMode(false)
        tokenManager.clearOfflineMode()
    }

    fun getOfflineKey(): Flow<String?> = tokenManager.getOfflineKey()
    
    fun isOfflineMode(): Flow<Boolean> = tokenManager.isOfflineMode()

    fun generateOfflineKey(): String {
        // Generate a simple offline key
        return "OFFLINE_${UUID.randomUUID().toString().substring(0, 8).uppercase()}"
    }

    suspend fun generateOfflineKeyForUser(userId: Long): String {
        val offlineKey = generateOfflineKey()
        usuariosRepository.updateOfflineKey(userId, offlineKey)
        return offlineKey
    }

    suspend fun getOfflineKeyForUser(userId: Long): String? {
        return usuariosRepository.getOfflineKeyByUserId(userId)
    }

    suspend fun hasAnyOfflineUsers(): Boolean {
        // Check if there are any users with offline keys
        return try {
            val allUsers = usuariosRepository.getAllUsers()
            allUsers.firstOrNull()?.any { user -> user.offlineKey != null } ?: false
        } catch (e: Exception) {
            false
        }
    }
}
