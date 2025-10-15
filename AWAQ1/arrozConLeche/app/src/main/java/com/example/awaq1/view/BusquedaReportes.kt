package com.example.awaq1.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.awaq1.MainActivity
import com.example.awaq1.R
import com.example.awaq1.data.formularios.FormularioUnoEntity
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import com.example.awaq1.data.formularios.FormInfo
import com.example.awaq1.ui.theme.components.BottomNavigationBar
import com.example.awaq1.ui.theme.components.DisplayCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObservationListScreen(navController: NavController) {
    val context = LocalContext.current as MainActivity
    val userId = context.accountInfo.user_id
    val repo = context.container.usuariosRepository
    //Cargar SOLO formularios del usuario en sesión
    val forms1 by repo.getAllFormularioUnoForUserID(userId).collectAsState(initial = emptyList())
    val forms2 by repo.getAllFormularioDosForUserID(userId).collectAsState(initial = emptyList())
    val forms3 by repo.getAllFormularioTresForUserID(userId).collectAsState(initial = emptyList())
    val forms4 by repo.getAllFormularioCuatroForUserID(userId).collectAsState(initial = emptyList())
    val forms5 by repo.getAllFormularioCincoForUserID(userId).collectAsState(initial = emptyList())
    val forms6 by repo.getAllFormularioSeisForUserID(userId).collectAsState(initial = emptyList())
    val forms7 by repo.getAllFormularioSieteForUserID(userId).collectAsState(initial = emptyList())

    //Unificar a un solo listado común
    val allForms = remember(forms1, forms2, forms3, forms4, forms5, forms6, forms7) {
        buildList {
            addAll(forms1.map(::FormInfo))
            addAll(forms2.map(::FormInfo))
            addAll(forms3.map(::FormInfo))
            addAll(forms4.map(::FormInfo))
            addAll(forms5.map(::FormInfo))
            addAll(forms6.map(::FormInfo))
            addAll(forms7.map(::FormInfo))
        }
    }

    var completoSelected by remember { mutableStateOf(false) }
    var incompletoSelected by remember { mutableStateOf(false) }
    var subidoSelected by remember { mutableStateOf(false) }
    var noSubidoSelected by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Observaciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.leading_iconarrow),
                            contentDescription = "Atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFAED581))
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterButton(
                        text = "Completo",
                        selected = completoSelected,
                        onSelected = { completoSelected = it }
                    )
                    FilterButton(
                        text = "Incompleto",
                        selected = incompletoSelected,
                        onSelected = { incompletoSelected = it }
                    )
                    FilterButton(
                        text = "Subido",
                        selected = subidoSelected,
                        onSelected = { subidoSelected = it }
                    )
                    FilterButton(
                        text = "No Subido",
                        selected = noSubidoSelected,
                        onSelected = { noSubidoSelected = it }
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    items(allForms) { form ->
                        DisplayCard(navController = navController , formInfo = form)
                    }
                }
            }
        }
    }
}

@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    onSelected: (Boolean) -> Unit
) {
    Button(
        onClick = { onSelected(!selected) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF7CB342) else Color(0xFFAED581),
            contentColor = if (selected) Color.White else Color.Black
        ),
        modifier = Modifier.height(40.dp)
    ) {
        Text(text)
    }
}