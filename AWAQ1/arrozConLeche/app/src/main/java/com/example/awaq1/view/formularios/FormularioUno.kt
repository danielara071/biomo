package com.example.awaq1.view.formularios

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
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
import androidx.compose.ui.res.painterResource
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
import com.example.awaq1.R
import com.example.awaq1.data.formularios.FormularioUnoEntity
import kotlinx.coroutines.runBlocking
import androidx.compose.material.icons.Icons
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.Add
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.awaq1.ViewModels.CameraViewModel
import com.example.awaq1.data.formularios.ImageEntity
import com.example.awaq1.data.formularios.Ubicacion
import com.example.awaq1.view.CameraWindow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun Preview() {
    ObservationForm(rememberNavController())
}

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ObservationForm(navController: NavController, formularioId: Long = 0L) {
    val context = LocalContext.current as MainActivity
    val appContainer = context.container

    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    val ubicacion = Ubicacion(context)
    val scope = rememberCoroutineScope()

    var readOnly by remember { mutableStateOf(false) }

    var transecto by remember { mutableStateOf("") }
    var clima by remember { mutableStateOf("") }
    var temporada by remember { mutableStateOf("Verano/Seca") }
    var tipoAnimal by remember { mutableStateOf("") }
    var nombreComun by remember { mutableStateOf("") }
    var nombreCientifico by remember { mutableStateOf("") }
    var numeroIndividuos by remember { mutableStateOf("") }
    var tipoObservacion by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var editado by remember { mutableStateOf("") }
    val cameraViewModel: CameraViewModel = viewModel()
    var showCamera by remember { mutableStateOf(false) }
    val savedImageUris = remember { mutableStateOf(mutableListOf<Uri>()) }

    if (formularioId != 0L) {
        val formulario: FormularioUnoEntity? = runBlocking {
            Log.d("Formulario1Loading", "Loading formulario1 with ID $formularioId")
            appContainer.formulariosRepository.getFormularioUnoStream(formularioId).first()
        }

        if (formulario != null) {
            // 🔒 clave de solo lectura
            readOnly = formulario.synced

            transecto = formulario.transecto
            clima = formulario.clima
            temporada = formulario.temporada
            tipoAnimal = formulario.tipoAnimal
            nombreComun = formulario.nombreComun
            nombreCientifico = formulario.nombreCientifico
            numeroIndividuos = formulario.numeroIndividuos
            tipoObservacion = formulario.tipoObservacion
            observaciones = formulario.observaciones
            fecha = formulario.fecha
            editado = formulario.editado
            location = if (formulario.latitude != null && formulario.longitude != null) {
                Pair(formulario.latitude, formulario.longitude)
            } else null

            val storedImages = runBlocking {
                appContainer.formulariosRepository.getImagesByFormulario(formularioId, "Formulario1").first()
            }
            savedImageUris.value = storedImages.mapNotNull { imageEntity ->
                try { Uri.parse(imageEntity.imageUri) } catch (_: Exception) { null }
            }.toMutableList()
        } else {
            Log.e("Formulario1Loading", "NO se pudo obtener el formulario1 con id $formularioId")
        }
    }

    if (location == null) {
        LaunchedEffect(Unit) {
            context.requestLocationPermission()
            if (ubicacion.hasLocationPermission()) {
                location = ubicacion.obtenerCoordenadas()
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
                    savedImageUris = savedImageUris,
                    onClose = { showCamera = false },
                    onGalleryClick = { /* no-op */ }
                )
            } else {
                Box(modifier = Modifier.background(color = Color.White)) {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .background(color = Color.White),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        // Banner de Solo lectura
                        if (readOnly) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFF3CD), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Text("Formulario subido: solo lectura", color = Color(0xFF856404))
                            }
                        }

                        // Ubicación
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

                        // Transecto
                        OutlinedTextField(
                            value = transecto,
                            onValueChange = { if (!readOnly) transecto = it },
                            label = { Text("Número de Transecto") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !readOnly
                        )

                        // Clima
                        Text("Estado del Tiempo:")
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            maxItemsInEachRow = 3
                        ) {
                            val weatherOptions = listOf("Soleado", "Parcialmente Nublado", "Lluvioso")
                            val weatherIcons = listOf(R.drawable.sunny, R.drawable.cloudy, R.drawable.rainy)

                            weatherOptions.forEachIndexed { index, option ->
                                IconToggleButton(
                                    checked = clima == option,
                                    onCheckedChange = { if (!readOnly) clima = option },
                                    enabled = !readOnly,
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

                        // Época
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
                                        onClick = { if (!readOnly) temporada = option },
                                        enabled = !readOnly,
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF4E7029),
                                            unselectedColor = Color.Gray
                                        )
                                    )
                                    Text(option, modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                        }

                        // Tipo de Animal
                        Text("Tipo de Animal")
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            maxItemsInEachRow = 3
                        ) {
                            val animals = listOf("Mamífero", "Ave", "Reptil", "Anfibio", "Insecto")
                            if (tipoAnimal == "") tipoAnimal = animals[0]

                            animals.forEach { animal ->
                                IconToggleButton(
                                    checked = tipoAnimal == animal,
                                    onCheckedChange = { if (!readOnly) tipoAnimal = animal },
                                    enabled = !readOnly,
                                    modifier = Modifier.size(155.dp)
                                ) {
                                    val imageResource = when (animal) {
                                        "Mamífero" -> R.drawable.ic_mamifero
                                        "Ave" -> R.drawable.ic_ave
                                        "Reptil" -> R.drawable.ic_reptil
                                        "Anfibio" -> R.drawable.ic_anfibio
                                        "Insecto" -> R.drawable.ic_insecto
                                        else -> android.R.drawable.ic_menu_gallery
                                    }
                                    Box(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .border(
                                                width = 2.dp,
                                                color = if (tipoAnimal == animal) Color(0xFF4E7029) else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Image(
                                                painter = painterResource(id = imageResource),
                                                contentDescription = animal,
                                                modifier = Modifier.requiredSize(75.dp)
                                            )
                                            Text(
                                                text = animal,
                                                fontSize = 20.sp,
                                                color = if (tipoAnimal == animal) Color(0xFF4E7029) else Color(0xFF3F3F3F),
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Textos
                        OutlinedTextField(
                            value = nombreComun,
                            onValueChange = { if (!readOnly) nombreComun = it },
                            label = { Text("Nombre Común") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !readOnly
                        )
                        OutlinedTextField(
                            value = nombreCientifico,
                            onValueChange = { if (!readOnly) nombreCientifico = it },
                            label = { Text("Nombre Científico") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !readOnly
                        )
                        OutlinedTextField(
                            value = numeroIndividuos,
                            onValueChange = { if (!readOnly) numeroIndividuos = it },
                            label = { Text("Número de Individuos") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !readOnly
                        )
                        Text("Tipo de Observación")
                        val observacionOptions = listOf("La Vió", "Huella", "Rastro", "Cacería", "Le dijeron")
                        if (tipoObservacion == "") tipoObservacion = observacionOptions[0]
                        Column {
                            observacionOptions.forEach { option ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = tipoObservacion == option,
                                        onClick = { if (!readOnly) tipoObservacion = option },
                                        enabled = !readOnly,
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF4E7029),
                                            unselectedColor = Color.Gray
                                        )
                                    )
                                    Text(option, modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                        }

                        // Fotos
                        Button(
                            onClick = { if (!readOnly) showCamera = true },
                            enabled = !readOnly,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4E7029),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Take Photo", modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tomar Foto")
                        }

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
                                Button(
                                    onClick = {
                                        if (!readOnly) {
                                            savedImageUris.value = savedImageUris.value.toMutableList().apply { remove(uri) }
                                        }
                                    },
                                    enabled = !readOnly,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                    elevation = null
                                ) {
                                    Text(text = "X", color = Color.Red, fontSize = 20.sp)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = observaciones,
                            onValueChange = { if (!readOnly) observaciones = it },
                            label = { Text("Observaciones") },
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            maxLines = 4,
                            enabled = !readOnly
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
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
                                Text("Atras", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold))
                            }

                            Button(
                                onClick = {
                                    if (readOnly) return@Button
                                    if (fecha.isEmpty()) fecha = getCurrentDate()
                                    editado = getCurrentDate()
                                    val formulario = FormularioUnoEntity(
                                        transecto = transecto,
                                        clima = clima,
                                        temporada = temporada,
                                        tipoAnimal = tipoAnimal,
                                        nombreComun = nombreComun,
                                        nombreCientifico = nombreCientifico,
                                        numeroIndividuos = numeroIndividuos,
                                        tipoObservacion = tipoObservacion,
                                        observaciones = observaciones,
                                        latitude = location?.first,
                                        longitude = location?.second,
                                        fecha = fecha,
                                        editado = editado
                                    ).withID(formularioId)

                                    scope.launch {
                                        val formId = appContainer.usuariosRepository
                                            .insertUserWithFormularioUno(context.accountInfo.user_id, formulario)

                                        appContainer.formulariosRepository.deleteImagesByFormulario(
                                            formularioId = formId, formularioType = "Formulario1"
                                        )
                                        savedImageUris.value.forEach { uri ->
                                            appContainer.formulariosRepository.insertImage(
                                                ImageEntity(formularioId = formId, formularioType = "Formulario1", imageUri = uri.toString())
                                            )
                                        }
                                    }
                                    navController.navigate("home")
                                },
                                enabled = !readOnly,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4E7029),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Guardar", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold))
                            }
                        }
                    }
                }
            }
        }
    )
}

fun getCurrentDate(existingDate: String? = null): String {
    return existingDate ?: run {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        dateFormat.format(date)
    }
}
