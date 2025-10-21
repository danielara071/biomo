import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.awaq1.MainActivity
import com.example.awaq1.R
import com.example.awaq1.data.remote.OfflineAuthRepository
import com.example.awaq1.ui.theme.components.BottomNavigationBar
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsScreen(
    navController: NavController,
    offlineAuthRepository: OfflineAuthRepository? = null
) {
    val context = LocalContext.current as MainActivity
    val nombre = context.accountInfo.username.substringBefore("@")
    val email = context.accountInfo.username
    val userId = context.accountInfo.user_id
    
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    
    var offlineKey by remember { mutableStateOf<String?>(null) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    // Load offline key for current user
    LaunchedEffect(userId) {
        offlineAuthRepository?.let { repo ->
            offlineKey = repo.getOfflineKeyForUser(userId)
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.b),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Hola \n$nombre",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.perfil),
                    contentDescription = "User Profile",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                UserInfoRow(icon = Icons.Default.Email, info = email)
                UserInfoRow(icon = Icons.Default.Lock, info = "Password")
                UserInfoRow(icon = Icons.Default.Phone, info = "+57 312 345 6789")

                Spacer(modifier = Modifier.height(16.dp))

                // Offline Key Section
                offlineAuthRepository?.let { repo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.9f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                            }
                            
                            if (offlineKey != null) {
                                Text(
                                    text = "Tu clave para acceso offline:",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                
                                // Offline Key Display
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFF5F5F5)
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
                                            text = offlineKey!!,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4E7029),
                                            modifier = Modifier.weight(1f)
                                        )
                                        
                                        IconButton(
                                            onClick = {
                                                clipboardManager.setText(AnnotatedString(offlineKey!!))
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
                            } else {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            offlineKey = repo.generateOfflineKeyForUser(userId)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4E7029)
                                    )
                                ) {
                                    Text(
                                        text = "Generar Clave Offline",
                                        color = Color.White
                                    )
                                }
                            }
                            
                            Text(
                                text = "Esta clave te permite acceder sin conexión a internet.",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                SettingsSection("GENERAL", listOf("Editar Perfil"))
                Spacer(modifier = Modifier.height(16.dp))
                SettingsSection("NOTIFICACIONES", listOf("Notificaciones"))
                Spacer(modifier = Modifier.height(16.dp))
                SettingsSection("ACCIONES", listOf("Cerrar sesión"))
            }
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

@Composable
fun UserInfoRow(icon: ImageVector, info: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black, // Changed to white for better visibility on image background
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = info, fontSize = 16.sp, color = Color.Black) // Changed to white for better visibility
    }
}

@Composable
fun SettingsSection(title: String, items: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray // Changed to light gray for better visibility on image background
        )
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item, fontSize = 16.sp, color = Color.Black)
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }
}


