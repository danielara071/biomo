package com.example.awaq1.view

import android.graphics.Paint.Align
import android.os.Build
import android.widget.Button
import android.widget.RadioButton
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.awaq1.R
import com.example.awaq1.navigator.FormCincoID
import com.example.awaq1.navigator.FormCuatroID
import com.example.awaq1.navigator.FormDosID
import com.example.awaq1.navigator.FormSeisID
import com.example.awaq1.navigator.FormSieteID
import com.example.awaq1.navigator.FormTresID
import com.example.awaq1.navigator.FormUnoID


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewSelectFormulario() {
    SelectFormularioScreen(rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SelectFormularioScreen(navController: NavController) {
    var pais by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var zona: String by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Formulario de Geolocalización",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.03).em,
                            color = Color(0xFF4E7029)
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF4E7029)
                )
            )
        },
        floatingActionButton = {FloatingActionButton(
            onClick = { navController.navigate("home") },
            containerColor = Color(0xFF4E7029),
            contentColor = Color.White,
            modifier = Modifier
                .size(36.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                modifier = Modifier.size(24.dp) // keep some padding inside the circle
            )
        }
        }

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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = pais,
                    onValueChange = { pais = it },
                    label = { Text("Nombre País") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = departamento,
                    onValueChange = { departamento = it },
                    label = { Text("Nombre Departamento") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = municipio,
                    onValueChange = { municipio = it },
                    label = { Text("Nombre Municipio") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Zona")
                FlowRow(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    maxItemsInEachRow = 2
                ) {
                    val zonasOpciones = listOf(
                        "Vereda",
                        "Finca",
                        "Río",
                        "Quebrada"
                    )
                    zonasOpciones.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (zona == option) Color(0xFFE4F1D4) else Color.Transparent
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = zona == option,
                                onClick = {zona = option},
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF4E7029),
                                    unselectedColor = Color.Gray
                                )
                            )
                            Text(
                                text = option,
                                modifier = Modifier.padding(start = 6.dp),
                                fontSize = 18.sp,
                                fontWeight = if(zona == option ) FontWeight.SemiBold else FontWeight.Normal,
                                color = if(zona == option) Color(0xFF4E7029) else Color(0xFF3F3F3F)
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(32.dp))
                FormChooseButton(FormUnoID(), "Fauna en Transectos", navController)
                FormChooseButton(FormDosID(), "Fauna en Punto de Conteo", navController)
                FormChooseButton(FormTresID(), "Validación de Cobertura", navController)
                FormChooseButton(FormCuatroID(), "Parcela de Vegetación", navController)
                FormChooseButton(FormCincoID(), "Fauna Busqueda Libre", navController)
                FormChooseButton(FormSeisID(), "Camaras Trampa", navController)
                FormChooseButton(FormSieteID(), "Variables Climaticas", navController)
            }
        }
    }
}

@Composable
fun FormChooseButton(route: Any, text: String, navController: NavController) {
    Button(
        onClick = { navController.navigate(route) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4E7029),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(30),
        modifier = Modifier
            .width(300.dp) // Fixed width and height
    ) {
        Text(
            text,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}