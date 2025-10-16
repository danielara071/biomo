package com.example.awaq1.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.awaq1.MainActivity
import com.example.awaq1.data.formularios.FormularioCincoEntity
import com.example.awaq1.data.formularios.FormularioCuatroEntity
import com.example.awaq1.data.formularios.FormularioDosEntity
import com.example.awaq1.data.formularios.FormularioSeisEntity
import com.example.awaq1.data.formularios.FormularioSieteEntity
import com.example.awaq1.data.formularios.FormularioTresEntity
import com.example.awaq1.data.formularios.FormularioUnoEntity
import com.example.awaq1.data.formularios.Ubicacion
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.awaq1.data.formularios.FormInfo
import com.example.awaq1.ui.theme.components.BottomNavigationBar
import com.example.awaq1.ui.theme.components.DisplayCard
import com.example.awaq1.ui.theme.components.searchBar
import kotlin.collections.buildList
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import com.example.awaq1.data.syncAllPending
import kotlinx.coroutines.launch

@Composable
fun Home(navController: NavController) {
    val context = LocalContext.current as MainActivity
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var query by remember { mutableStateOf("") }
    val ubicacion = Ubicacion(context)
    if(location == null){
        LaunchedEffect(Unit) {
            context.requestLocationPermission()
            if (ubicacion.hasLocationPermission()) {
                location = ubicacion.obtenerCoordenadas()
                if (location != null) {
                    Log.d("LogIn", "Location retrieved: Lat=${location!!.first}, Long=${location!!.second}")
                } else {
                    Log.d("LogIn", "Location is null")
                }
            } else {
                Log.d("ObservationForm", "Location permission required but not granted.")
            }
        }
    }
    //Username para saludo
    val nombre = context.accountInfo.username.substringBefore("@")

    val appContainer = context.container
    val forms1: List<FormularioUnoEntity> by appContainer.usuariosRepository.getAllFormularioUnoForUserID(
        context.accountInfo.user_id
    )
        .collectAsState(initial = emptyList())
    val forms2: List<FormularioDosEntity> by appContainer.usuariosRepository.getAllFormularioDosForUserID(
        context.accountInfo.user_id
    )
        .collectAsState(initial = emptyList())
    val forms3: List<FormularioTresEntity> by appContainer.usuariosRepository.getAllFormularioTresForUserID(
        context.accountInfo.user_id
    )
        .collectAsState(initial = emptyList())
    val forms4: List<FormularioCuatroEntity> by appContainer.usuariosRepository.getAllFormularioCuatroForUserID(
        context.accountInfo.user_id
    )
        .collectAsState(initial = emptyList())
    val forms5: List<FormularioCincoEntity> by appContainer.usuariosRepository.getAllFormularioCincoForUserID(
        context.accountInfo.user_id
    )
        .collectAsState(initial = emptyList())
    val forms6: List<FormularioSeisEntity> by appContainer.usuariosRepository.getAllFormularioSeisForUserID(
        context.accountInfo.user_id
    )
        .collectAsState(initial = emptyList())
    val forms7: List<FormularioSieteEntity> by appContainer.usuariosRepository.getAllFormularioSieteForUserID(
        context.accountInfo.user_id
    )
        .collectAsState(initial = emptyList())
    val count by appContainer.formulariosRepository.getAllFormulariosCount()
        .collectAsState(initial = 0)
    val allForms = remember(forms1, forms2, forms3, forms4, forms5, forms6, forms7) {
        buildList{
            addAll(forms1.map { FormInfo(it) })
            addAll(forms2.map { FormInfo(it) })
            addAll(forms3.map { FormInfo(it) })
            addAll(forms4.map { FormInfo(it) })
            addAll(forms5.map { FormInfo(it) })
            addAll(forms6.map { FormInfo(it) })
            addAll(forms7.map { FormInfo(it) })
        }
    }
    //Mostrar solo formularios que coincidan
    //Filtrar por texto
    val filtered = remember(allForms, query) {
        if(query.isBlank()) allForms
        else{
            val q = query.trim().lowercase()
            allForms.filter { info ->
            listOf(
                info.tipo,
                info.valorIdentificador,
                info.primerTag,
                info.primerContenido,
                info.segundoTag,
                info.segundoContenido,
                info.fechaCreacion,
                info.fechaEdicion
            ).any{it.lowercase().contains(q)}
            }
        }

    }
    //Formularios totales por usuario
    val userTotal = remember(forms1, forms2, forms3, forms4, forms5, forms6, forms7) {
        forms1.size + forms2.size + forms3.size + forms4.size + forms5.size + forms6.size + forms7.size
    }

    val pendingCount = remember(forms1, forms2, forms3, forms4, forms5, forms6, forms7) {
        forms1.count { it.esCompleto() && !it.synced } +
                forms2.count { it.esCompleto() && !it.synced } +
                forms3.count { it.esCompleto() && !it.synced } +
                forms4.count { it.esCompleto() && !it.synced } +
                forms5.count { it.esCompleto() && !it.synced } +
                forms6.count { it.esCompleto() && !it.synced } +
                forms7.count { it.esCompleto() && !it.synced }
    }

    val scope = rememberCoroutineScope()
    val snack = remember { SnackbarHostState() }
    val visibleTotal = if (query.isBlank()) userTotal else filtered.size
    val visibleList = if(query.isBlank()) allForms else filtered

    Scaffold(
        bottomBar = {
            Column() {
                BottomNavigationBar(navController)
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFFFFF))
                    .padding(paddingValues)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(bottomStart = 2000.dp, bottomEnd = 2000.dp))
                        .background(Color(0xFFCDE4B4)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.Center) {
                        var fontSize by remember { mutableStateOf(50.sp) }

                        Text(
                            text = "Hola, $nombre!",
                            fontSize = fontSize,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4E7029),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                // Dashboard Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Row {
                        Text(
                            text = "DASHBOARD",
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333),
                            modifier = Modifier.padding(bottom = 8.dp),
                            textAlign = TextAlign.Center
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Button(
                                enabled = pendingCount > 0,
                                onClick = {
                                    scope.launch {
                                        val result = syncAllPending(
                                            api = appContainer.authApiService,  // Asegúrate de exponerlo en tu container
                                            repo = appContainer.formulariosRepository,
                                            forms1, forms2, forms3, forms4, forms5, forms6, forms7
                                        )
                                        val msg = if (result.errors.isEmpty()) {
                                            "Sincronizados: ${result.successCount}"
                                        } else {
                                            "Sincronizados: ${result.successCount}. Errores: ${result.errors.size}"
                                        }
                                        snack.showSnackbar(msg)
                                    }
                                }
                            ) {
                                Text(text = if (pendingCount > 0) "Sincronizar ($pendingCount)" else "Sincronizar")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    searchBar(value = query,
                        onValueChange = {query = it},
                        placeholder = "Buscar Formulario")
                    // Stats Row
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {


                        StatsColumn(label = "Total", count = visibleTotal, color = Color.Black)
                    }


                    // Forms Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        reverseLayout = true,
                        modifier = Modifier
                            .padding(horizontal = 0.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    ) {
                        if(visibleList.isEmpty()){
                            item{
                                Text(
                                    text = if(query.isBlank()) "No hay formularios" else "Sin resultados para \"$query\"",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray
                                )
                            }
                        } else{
                            items(visibleList,
                                key = {it.formId to it.formulario //clave estable
                                }){formInfo ->
                                DisplayCard(navController = navController, formInfo = formInfo)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun StatsColumn(label: String, count: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$count",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 24.sp,
            color = Color.Gray
        )
    }
}


@Composable
fun NavigationButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isActive: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) Color(0xFF4CAF50) else Color.Gray // Green when active, gray when inactive
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) Color(0xFF4CAF50) else Color.Gray // Green when active, gray when inactive
        )
    }
}

