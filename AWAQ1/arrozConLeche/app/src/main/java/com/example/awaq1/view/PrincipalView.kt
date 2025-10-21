package com.example.awaq1.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.room.Transaction
import com.example.awaq1.MainActivity
import com.example.awaq1.R
import com.example.awaq1.data.AccountInfo
import com.example.awaq1.data.remote.AuthRepository
import com.example.awaq1.data.remote.OfflineAuthRepository
import com.example.awaq1.data.usuario.UsuarioEntity
import com.example.awaq1.navigator.AppNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

// Sets global (MainActivity) field accoundInfo, from a username
@SuppressLint("NewApi")
suspend fun setAccountInfoOnLogin(context: MainActivity, username: String) {
    val usuariosRepository = context.container.usuariosRepository

    // Get user's ID in database, or create its entry if not found
    val userId: Long = usuariosRepository.getUserIdByUsername(username)
        ?: usuariosRepository.insertUser(
            UsuarioEntity(
                username = username,
                lastAccess = LocalDateTime.now().toString(),
                lastLogin = LocalDateTime.now().toString()
            )
        )
    context.accountInfo = AccountInfo(username, userId)
}

@Composable
fun PrincipalView(
    modifier: Modifier = Modifier,
    authRepository: AuthRepository,
    offlineAuthRepository: OfflineAuthRepository
) {
    var loggedIn by remember { mutableStateOf(false) }
    var isOfflineMode by remember { mutableStateOf(false) }
    val context = LocalContext.current as MainActivity
    val scope = rememberCoroutineScope()

    // Check if user is in offline mode on startup
    LaunchedEffect(Unit) {
        offlineAuthRepository.isOfflineMode().collect { offline ->
            isOfflineMode = offline
            if (offline) {
                // Get the offline key and find the user
                offlineAuthRepository.getOfflineKey().collect { key ->
                    key?.let { offlineKey ->
                        scope.launch {
                            val username = offlineAuthRepository.signInOfflineWithUsername(offlineKey)
                            username?.let { user ->
                                setAccountInfoOnLogin(context, user)
                                loggedIn = true
                            }
                        }
                    }
                }
            }
        }
    }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        //context.accountInfo = AccountInfo("example@example.com", user_id = 1L)
        if (loggedIn) { // loggedIn o true
            AppNavigator(
                onLogout = {
                    loggedIn = false
                },
                offlineAuthRepository = offlineAuthRepository,
                Modifier.padding(innerPadding))
        } else {
            LogIn(
                authRepository = authRepository,
                offlineAuthRepository = offlineAuthRepository,
                onLoginSuccess =  { username ->
                    scope.launch {
                        setAccountInfoOnLogin(context, username)
                        // Generate offline key for this user after successful login
                        val userId = context.container.usuariosRepository.getUserIdByUsername(username)
                        userId?.let { id ->
                            offlineAuthRepository.generateOfflineKeyForUser(id)
                        }
                        loggedIn = true
                    }
                },
                modifier = Modifier.padding(innerPadding)
            )
//            LoginScreen(
//                auth0 = auth0,
//                onLoginSuccess = {
//                    credentials = it
//                    loggedIn = true
//                },
//                modifier = Modifier.padding(innerPadding)
//            )
        }
    }
}
