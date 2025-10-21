package com.example.awaq1.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.awaq1.MainActivity
import com.example.awaq1.data.remote.OfflineAuthRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    offlineAuthRepository: OfflineAuthRepository,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    // val context = LocalContext.current as MainActivity
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    
    var isOfflineMode by remember { mutableStateOf(false) }
    var offlineKey by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    // Observe offline mode and key
    LaunchedEffect(Unit) {
        offlineAuthRepository.isOfflineMode().collect { offline ->
            isOfflineMode = offline
        }
    }
    
    LaunchedEffect(Unit) {
        offlineAuthRepository.getOfflineKey().collect { key ->
            offlineKey = key ?: ""
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Text(
            text = "Perfil de Usuario",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4E7029),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // User Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Usuario",
                        tint = Color(0xFF4E7029),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOfflineMode) "Usuario Offline" else "Usuario Online",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Text(
                    text = if (isOfflineMode) "Modo Offline" else "Modo Online",
                    fontSize = 14.sp,
                    color = if (isOfflineMode) Color(0xFFFF9800) else Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Offline Key Card (only show in offline mode)
        if (isOfflineMode && offlineKey.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.OfflineBolt,
                            contentDescription = "Clave Offline",
                            tint = Color(0xFF4E7029),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Clave Offline",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Text(
                        text = "Guarda esta clave para acceder sin conexión:",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Offline Key Display
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = offlineKey,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4E7029),
                                modifier = Modifier.weight(1f)
                            )
                            
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(offlineKey))
                                    snackbarMessage = "Clave copiada al portapapeles"
                                    showSnackbar = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copiar clave",
                                    tint = Color(0xFF4E7029)
                                )
                            }
                        }
                    }
                    
                    Text(
                        text = "Nota: Esta clave te permite acceder a la aplicación sin conexión a internet.",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        // Logout Button
        Button(
            onClick = {
                scope.launch {
                    if (isOfflineMode) {
                        offlineAuthRepository.signOutOffline()
                    }
                    onLogout()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD32F2F),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Cerrar Sesión",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    // Snackbar for copy feedback
    if (showSnackbar) {
        LaunchedEffect(showSnackbar) {
            kotlinx.coroutines.delay(2000)
            showSnackbar = false
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = snackbarMessage,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
