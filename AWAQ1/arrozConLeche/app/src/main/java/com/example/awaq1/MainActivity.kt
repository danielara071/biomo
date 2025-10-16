package com.example.awaq1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.awaq1.data.AccountInfo
import com.example.awaq1.data.AppContainer
import com.example.awaq1.data.AppDataContainer
import com.example.awaq1.data.remote.RetrofitClient
import com.example.awaq1.data.formularios.Ubicacion
import com.example.awaq1.data.local.TokenManager
import com.example.awaq1.data.remote.AuthRepository
import com.example.awaq1.ui.theme.AWAQ1Theme
import com.example.awaq1.view.PrincipalView


class MainActivity : ComponentActivity() {
    lateinit var container: AppContainer
    lateinit var accountInfo: AccountInfo



    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>
    // Función principal que se ejecuta al crear la actividad
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar el contenedor de datos de la aplicación
        container = AppDataContainer(this)

        // Configurar el sistema de autenticación y API
        val tokenManager = TokenManager(applicationContext)
        val apiService = RetrofitClient.create(tokenManager)
        val authRepository = AuthRepository(apiService, tokenManager)

        // Configurar el launcher para solicitar permisos de ubicación
        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted) {
                Log.d("MainActivity", "Location permission is required but not granted.")
            }
        }
        // Verificar y solicitar permisos de cámara si es necesario
        if (!arePermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, CAMERA_PERMISSION, 100
            )
        }

        // Habilitar modo edge-to-edge y configurar la interfaz
        enableEdgeToEdge()
        setContent {
            AWAQ1Theme {
                PrincipalView(modifier = Modifier, authRepository = authRepository)
            }
        }
    }
    // Solicita permisos de ubicación al usuario
    fun requestLocationPermission() {
        if (!Ubicacion(this).hasLocationPermission()) {
            locationPermissionLauncher.launch(Ubicacion.LOCATION_PERMISSION)
        }
    }
    // Verifica si todos los permisos necesarios han sido otorgados
    fun arePermissionsGranted(): Boolean {
        return CAMERA_PERMISSION.all { permission ->
            ContextCompat.checkSelfPermission(
                applicationContext,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Objeto compañero que contiene las constantes de permisos
    companion object {
        // Array con los permisos de cámara requeridos
        val CAMERA_PERMISSION = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}


