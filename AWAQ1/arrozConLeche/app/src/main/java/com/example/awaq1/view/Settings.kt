package com.example.awaq1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.awaq1.ui.theme.components.BottomNavigationBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(navController: NavController, onLogout: () -> Unit) {
    var notificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFAED581)) // Color similar al de la imagen
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.background(color = Color.White).fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                SectionTitle("GENERAL")
                MenuItem("Sobre Desarrolladores") {
                    navController.navigate("about_us")
                }
                MenuItem("Editar Perfil") {
                    navController.navigate("perfil")
                }
                MenuItem("Cambiar contraseña") {
                    // Acción para cambiar contraseña
                }

                Spacer(modifier = Modifier.height(16.dp))

                SectionTitle("NOTIFICACIONES")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Notificaciones", fontSize = 18.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it })
                }

                Spacer(modifier = Modifier.height(16.dp))

                SectionTitle("ACCIONES")
                MenuItem("Cerrar sesión") {
                    onLogout()
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun MenuItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}