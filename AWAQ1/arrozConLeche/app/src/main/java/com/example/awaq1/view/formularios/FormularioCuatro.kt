package com.example.awaq1.view.formularios

import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.awaq1.MainActivity
import kotlinx.coroutines.runBlocking
import androidx.compose.material.icons.Icons
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.IconToggleButton
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.awaq1.R
import com.example.awaq1.ViewModels.CameraViewModel
import com.example.awaq1.data.formularios.FormularioCuatroEntity
import com.example.awaq1.data.formularios.ImageEntity
import com.example.awaq1.data.formularios.Ubicacion
import com.example.awaq1.view.CameraWindow
import kotlinx.coroutines.flow.first

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewForm4() {
    ObservationFormCuatro(rememberNavController())
}

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ObservationFormCuatro(navController: NavController, formularioId: Long = 0) {
    val context = LocalContext.current as MainActivity
    val appContainer = context.container

    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    val ubicacion = Ubicacion(context)

    var showCamera by remember { mutableStateOf(false) }
    val savedImageUris = remember { mutableStateOf(mutableListOf<Uri>()) }
    val cameraViewModel: CameraViewModel = viewModel()

    var codigo: String by remember { mutableStateOf("") }
    var clima by remember { mutableStateOf("") }
    var temporada by remember { mutableStateOf("Verano/Seca") }
    var quad_a: String by remember { mutableStateOf("") }
    var quad_b: String by remember { mutableStateOf("") }
    var sub_quad: String by remember { mutableStateOf("") }
    var habitoDeCrecimiento: String by remember { mutableStateOf("") }
    var nombreComun: String by remember { mutableStateOf("") }
    var nombreCientifico: String by remember { mutableStateOf("") }
    var placa: String by remember { mutableStateOf("") }
    var circunferencia: String by remember { mutableStateOf("") }
    var distancia: String by remember { mutableStateOf("") }
    var estatura: String by remember { mutableStateOf("") }
    var altura: String by remember { mutableStateOf("") }
    var observaciones: String by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var editado by remember { mutableStateOf("") }

    if (formularioId != 0L) {
        val formulario: FormularioCuatroEntity? = runBlocking {
            Log.d("Formulario4Loading", "Loading formulario4 with ID $formularioId")
            appContainer.formulariosRepository.getFormularioCuatroStream(formularioId).first()
        }

        if (formulario != null) {
            codigo = formulario.codigo
            clima = formulario.clima
            temporada = formulario.temporada
            quad_a = formulario.quad_a
            quad_b = formulario.quad_b
            sub_quad = formulario.sub_quad
            habitoDeCrecimiento = formulario.habitoDeCrecimiento
            nombreComun = formulario.nombreComun
            nombreCientifico = formulario.nombreCientifico
            placa = formulario.placa
            circunferencia = formulario.circunferencia
            distancia = formulario.distancia
            estatura = formulario.estatura
            altura = formulario.altura
            observaciones = formulario.observaciones
            fecha = formulario.fecha
            editado = formulario.editado
            location = if (formulario.latitude != null && formulario.longitude != null) {
                Pair(formulario.latitude, formulario.longitude)
            } else {
                null
            }

            // Load saved images
            val storedImages = runBlocking {
                appContainer.formulariosRepository.getImagesByFormulario(formularioId, "Formulario4")
                    .first() // Fetch the list of ImageEntity for this form
            }
            // Convert image URIs from String to Uri and store them in savedImageUris
            savedImageUris.value = storedImages.mapNotNull { imageEntity ->
                try {
                    Uri.parse(imageEntity.imageUri) // Convert String to Uri
                } catch (e: Exception) {
                    Log.e("ObservationForm", "Failed to parse URI: ${imageEntity.imageUri}", e)
                    null
                }
            }.toMutableList()
        } else {
            Log.e("Formulario4Loading", "NO se pudo obtener el formulario4 con id $formularioId")
        }
    }
    if(location == null){
        LaunchedEffect(Unit) {
            context.requestLocationPermission()
            if (ubicacion.hasLocationPermission()) {
                location = ubicacion.obtenerCoordenadas()
                if (location != null) {
                    Log.d("ObservationForm", "Location retrieved: Lat=${location!!.first}, Long=${location!!.second}")
                } else {
                    Log.d("ObservationForm", "Location is null")
                }
            } else {
                Log.d("ObservationForm", "Location permission required but not granted.")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Formulario de Observación",
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
        content = { paddingValues ->
            if (showCamera) {
                CameraWindow(
                    activity = context,
                    cameraViewModel = cameraViewModel,
                    savedImageUris = savedImageUris, // Pass state
                    onClose = { showCamera = false },
                    onGalleryClick = { /* Optional: Handle gallery selection */ }
                )
            }else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF9F9F9), RoundedCornerShape(10.dp))
                                .border(1.dp, Color(0xFF4E7029), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text("Ubicación Actual:", fontWeight = FontWeight.Bold, color = Color.Black)
                                    Text(
                                        text = location?.let { "Lati: ${it.first}\nLong: ${it.second}" }
                                            ?: "Buscando ubicación...",
                                        fontSize = 14.sp, color = Color.DarkGray
                                    )
                                }

                                Image(
                                    painter = painterResource(id = R.drawable.compass_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }


                        OutlinedTextField(
                            value = codigo,
                            onValueChange = { codigo = it },
                            label = { Text("Código") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text("Estado del Tiempo:")
                        FlowRow (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            maxItemsInEachRow = 3
                            //verticalAlignment = Alignment.CenterVertically
                        ) {
                            val weatherOptions = listOf("Soleado", "Parcialmente Nublado", "Lluvioso")
                            val weatherIcons = listOf(
                                R.drawable.sunny, // Add sunny icon in your drawable resources
                                R.drawable.cloudy, // Add partly cloudy icon in your drawable resources
                                R.drawable.rainy // Add rainy icon in your drawable resources
                            )

                            weatherOptions.forEachIndexed { index, option ->
                                IconToggleButton(
                                    checked = clima == option,
                                    onCheckedChange = { clima = option },
                                    modifier = Modifier.size(150.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .border(
                                                width = 2.dp,
                                                color = if (clima == option) Color(0xFF4E7029) else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = weatherIcons[index]),
                                            contentDescription = option,
                                            modifier = Modifier.requiredSize(95.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Text("Época")
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val seasonOptions = listOf("Verano/Seca", "Invierno/Lluviosa")
                            seasonOptions.forEach { option ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = temporada == option,
                                        onClick = { temporada = option },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF4E7029),
                                            unselectedColor = Color.Gray
                                        )
                                    )
                                    Text(option, modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                        }
                        Text("Cuadrante")
                        val quadAOpciones = listOf("A", "B")
                        val quadBOpciones = listOf("C", "D", "E", "F", "G")
                        if (quad_a == "") {
                            quad_a = quadAOpciones[0]
                        }
                        if (quad_b == "") {
                            quad_b = quadBOpciones[0]
                        }

                        Row(modifier = Modifier.fillMaxSize()) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
                                verticalArrangement = Arrangement.Center, // Center items vertically
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                            ) {
                                quadAOpciones.forEach { opcion_QA ->
                                    /*
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.scale(2f)
                                    ) {
                                        RadioButton(
                                            selected = quad_a == opcion_QA,
                                            onClick = { quad_a = opcion_QA },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color(0xFF4E7029),
                                                unselectedColor = Color.Gray
                                            )
                                        )
                                        Text(
                                            opcion_QA,
                                            style = TextStyle(
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                    }
                                     */
                                    Spacer(modifier = Modifier.size(50.dp))
                                    SquareRadioButton(
                                        text = opcion_QA,
                                        isSelected = quad_a == opcion_QA, // Check if this button is selected
                                        onClick = {
                                            quad_a = opcion_QA // Update the selected option for group A
                                        },
                                        size = 100.dp
                                    )
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                quadBOpciones.forEach { opcion_QB ->
                                    /*
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.scale(2f)
                                    ) {
                                        RadioButton(
                                            selected = quad_b == opcion_QB,
                                            onClick = { quad_b = opcion_QB },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color(0xFF4E7029),
                                                unselectedColor = Color.Gray
                                            )
                                        )
                                        Text(
                                            opcion_QB,
                                            style = TextStyle(
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                    }
                                     */
                                    SquareRadioButton(
                                        text = opcion_QB,
                                        isSelected = quad_b == opcion_QB, // Check if this button is selected
                                        onClick = {
                                            quad_b = opcion_QB // Update the selected option for group B
                                        }
                                    )
                                }
                            }
                        }

                        Text("Sub-Cuadrante")
                        val subQuadOpciones = listOf("1", "2", "3", "4")

                        if (sub_quad == "") {
                            sub_quad = subQuadOpciones[0]
                        }

                        FlowRow(modifier = Modifier.fillMaxWidth()) {
                            subQuadOpciones.forEach { opcion_sub ->
                                /*
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                ) {
                                    RadioButton(
                                        selected = sub_quad == opcion_sub,
                                        onClick = { sub_quad = opcion_sub },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF4E7029),
                                            unselectedColor = Color.Gray
                                        )
                                    )
                                    Text(
                                        opcion_sub,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                                */

                                SquareRadioButton(
                                    text = opcion_sub,
                                    isSelected = sub_quad == opcion_sub, // Check if this button is selected
                                    onClick = {
                                        sub_quad = opcion_sub // Update the selected option for group A
                                    }
                                )
                            }
                        }

			Text("Hábito de Crecimiento")
                        val alturaOptions: List<Pair<String, String>> = listOf(Pair("Arbusto", "<1mt"), Pair("Arbolito", "1-3mt"), Pair("Árbol", ">3mt"))
                        if (habitoDeCrecimiento == "") {
                            habitoDeCrecimiento = alturaOptions[0].first
                        }
                        Column {
                            alturaOptions.forEach { option ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = habitoDeCrecimiento == option.first,
                                        onClick = { habitoDeCrecimiento = option.first },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF4E7029),
                                            unselectedColor = Color.Gray
                                        )
                                    )
                                    Text("${option.first} ${option.second}", modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                        }
			OutlinedTextField(
                            value = nombreComun,
                            onValueChange = { nombreComun = it },
                            label = { Text("Nombre Común") },
                            modifier = Modifier.fillMaxWidth()
                        )

			OutlinedTextField(
                            value = nombreCientifico,
                            onValueChange = { nombreCientifico = it },
                            label = { Text("Nombre Científico") },
                            modifier = Modifier.fillMaxWidth()
                        )
			
			OutlinedTextField(
                            value = placa,
                            onValueChange = { placa = it },
                            label = { Text("Placa") },
                            modifier = Modifier.fillMaxWidth()
                        )

			OutlinedTextField(
                            value = circunferencia,
                            onValueChange = { circunferencia = it },
                            label = { Text("Circunferencia en cm") },
                            modifier = Modifier.fillMaxWidth()
                        )

			OutlinedTextField(
                            value = distancia,
                            onValueChange = { distancia = it },
                            label = { Text("Distancia en mt") },
                            modifier = Modifier.fillMaxWidth()
                        )

			OutlinedTextField(
                            value = estatura,
                            onValueChange = { estatura = it },
                            label = { Text("Estatura Biomonitor en mt") },
                            modifier = Modifier.fillMaxWidth()
                        )

			OutlinedTextField(
                            value = altura,
                            onValueChange = { altura = it },
                            label = { Text("Altura en mt") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Camera Button
                        Button(
                            onClick = { showCamera = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4E7029),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Take Photo",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tomar Foto")
                        }

                        // Log.d("ObservationForm", "savedImageUri: ${savedImageUri.value}")

                        // Display the saved image
                        savedImageUris.value.forEach { uri ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = uri),
                                    contentDescription = "Saved Image",
                                    modifier = Modifier.size(100.dp)
                                )
                                Button(onClick = {
                                    savedImageUris.value = savedImageUris.value.toMutableList().apply { remove(uri) }
                                },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent // Removes background color
                                    ),
                                    elevation = null // Removes shadow/elevation for a completely flat button
                                ) {
                                    Text(text = "X",
                                        color = Color.Red,
                                        fontSize = 20.sp)
                                }
                            }
                        }

                        // Observaciones
                        OutlinedTextField(
                            value = observaciones,
                            onValueChange = { observaciones = it },
                            label = { Text("Observaciones") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            maxLines = 4
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("home") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4E7029),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Atras",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }

                            Button(
                                onClick = {
                                    if (fecha.isNullOrEmpty()) {
                                        fecha = getCurrentDate()
                                    }
                                    editado = getCurrentDate()
                                    val formulario = FormularioCuatroEntity(
                                        codigo = codigo,
                                        clima = clima,
                                        temporada = temporada,
                                        quad_a = quad_a,
                                        quad_b = quad_b,
                                        sub_quad = sub_quad,
                                        habitoDeCrecimiento = habitoDeCrecimiento,
                                        nombreComun = nombreComun,
                                        nombreCientifico = nombreCientifico,
                                        placa = placa,
                                        circunferencia = circunferencia,
                                        distancia = distancia,
                                        estatura = estatura,
                                        altura = altura,
                                        observaciones = observaciones,
                                        latitude = location?.first,
                                        longitude = location?.second,
                                        fecha = fecha,
                                        editado = editado
                                    ).withID(formularioId)

                                    runBlocking {
                                        // Insert regresa su id
                                        val formId = appContainer.usuariosRepository.insertUserWithFormularioCuatro(
                                            context.accountInfo.user_id, formulario
                                        )
                                        Log.d("ImageDAO", "formId: $formId")

                                        // Borrar todas las fotos en ese reporte
                                        appContainer.formulariosRepository.deleteImagesByFormulario(
                                            formularioId = formId,
                                            formularioType = "Formulario4"
                                        )

                                        // Agregar todas las imagenes al reporte
                                        savedImageUris.value.forEach { uri ->
                                            val image = ImageEntity(
                                                formularioId = formId,
                                                formularioType = "Formulario4",
                                                imageUri = uri.toString()
                                            )
                                            appContainer.formulariosRepository.insertImage(image)
                                        }
                                    }
                                    navController.navigate("home")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4E7029),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Guardar",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SquareRadioButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    size: Dp = 60.dp // Default size is 60.dp
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(size) // Use the customizable size
            .border(
                width = 2.dp,
                color = if (isSelected) Color(0xFF4E7029) else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if (isSelected) Color(0xFFC5E1A5) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick), // Handle selection
        contentAlignment = Alignment.Center // Center the text
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = (size.value / 3).sp, // Adjust font size relative to the box size
                color = if (isSelected) Color.Black else Color.Gray
            )
        )
    }
}