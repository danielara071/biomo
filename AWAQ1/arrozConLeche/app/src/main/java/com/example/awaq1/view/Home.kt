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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
import com.example.awaq1.data.formularios.FormInfo
import com.example.awaq1.view.components.BottomNavigationBar
import com.example.awaq1.view.components.DisplayCard

@Composable
fun Home(navController: NavController) {
    val context = LocalContext.current as MainActivity
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
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
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Dashboard",
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats Row
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StatsColumn(label = "Total", count = count, color = Color.Black)
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
                        items(count = 1) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        items(forms1) { form ->
                            val formCard = FormInfo(form)
                            DisplayCard(navController = navController, formInfo = formCard)
                        }
                        items(forms2) { form ->
                            val formCard = FormInfo(form)
                            DisplayCard(navController = navController, formInfo = formCard)
                        }
                        items(forms3) { form ->
                            val formCard = FormInfo(form)
                            DisplayCard(navController = navController, formInfo = formCard)
                        }
                        items(forms4) { form ->
                            val formCard = FormInfo(form)
                            DisplayCard(navController = navController, formInfo = formCard)
                        }
                        items(forms5) { form ->
                            val formCard = FormInfo(form)
                            DisplayCard(navController = navController, formInfo = formCard)
                        }
                        items(forms6) { form ->
                            val formCard = FormInfo(form)
                            DisplayCard(navController = navController, formInfo = formCard)
                        }
                        items(forms7) { form ->
                            val formCard = FormInfo(form)
                            DisplayCard(navController = navController, formInfo = formCard)
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

// Se ve algo así
// +---------------+
// | tipo:valorId  |
// | pTag: pCont   |
// | sTag: sCont   |
// +---------------+

