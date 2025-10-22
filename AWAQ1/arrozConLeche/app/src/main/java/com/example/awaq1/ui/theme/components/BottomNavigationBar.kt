package com.example.awaq1.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.awaq1.view.NavigationButton

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp) // Spacer at the bottom of the navbar
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround, // Space items evenly across the row
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            // Home Icon Button
            NavigationButton(
                label = "Inicio",
                icon = Icons.Default.Home,
                isActive = currentRoute == "home",
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )

            // Add (Reporte) Button - Centered
            IconButton(
                onClick = {
                    navController.navigate("elegir_reporte") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF4CAF50), CircleShape) // Green background circle
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Reporte",
                    tint = Color.White
                )
            }

            //Search Button
            NavigationButton(
                label = "Busqueda",
                icon = Icons.Default.Search,
                isActive = currentRoute == "buscar",
                onClick = {
                    navController.navigate("buscar") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )

            // Settings Icon Button
            NavigationButton(
                label = "Configuraciones",
                icon = Icons.Default.Settings,
                isActive = currentRoute == "settings",
                onClick = {
                    navController.navigate("settings") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(45.dp))
    }
}
