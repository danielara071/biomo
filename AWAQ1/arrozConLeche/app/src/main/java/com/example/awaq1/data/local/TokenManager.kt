package com.example.awaq1.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class TokenManager(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val OFFLINE_KEY = stringPreferencesKey("offline_key")
        private val IS_OFFLINE_MODE = booleanPreferencesKey("is_offline_mode")
    }

    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun deleteToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    suspend fun saveOfflineKey(offlineKey: String) {
        context.dataStore.edit { preferences ->
            preferences[OFFLINE_KEY] = offlineKey
            preferences[IS_OFFLINE_MODE] = true
        }
    }

    fun getOfflineKey(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[OFFLINE_KEY]
    }

    fun isOfflineMode(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_OFFLINE_MODE] ?: false
    }

    suspend fun setOfflineMode(isOffline: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_OFFLINE_MODE] = isOffline
        }
    }

    suspend fun clearOfflineMode() {
        context.dataStore.edit { preferences ->
            preferences.remove(OFFLINE_KEY)
            preferences[IS_OFFLINE_MODE] = false
        }
    }
}