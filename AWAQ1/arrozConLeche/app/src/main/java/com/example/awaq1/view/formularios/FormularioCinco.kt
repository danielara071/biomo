package com.example.awaq1.view.formularios

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.RadioButtonDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.awaq1.MainActivity
import com.example.awaq1.R
import com.example.awaq1.ViewModels.CameraViewModel
import com.example.awaq1.data.formularios.FormularioCincoEntity
import com.example.awaq1.data.formularios.ImageEntity
import com.example.awaq1.data.formularios.Ubicacion
import com.example.awaq1.view.CameraWindow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ObservationFormCinco(
    navController: NavController,
    formularioId: Long = 0L
) {
    val context = LocalContext.current as MainActivity
    val appContainer = context.container

    var readOnly by remember { mutableStateOf(false) }

    var zona by remember { mutableStateOf("") }
    var clima by remember { mutableStateOf("") }
    var temporada by remember { mutableStateOf("Verano/Seca") }
    var tipoAnimal by remember { mutableStateOf("") }
    var nombreComun by remember { mutableStateOf("") }
    var nombreCientifico by remember { mutableStateOf("") }
    var numeroIndividuos by remember { mutableStateOf("") }
    var tipoObservacion by remember { mutableStateOf("") }
    var alturaObservacion by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var editado by remember { mutableStateOf("") }

    val cameraViewModel: CameraViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    var showCamera by remember { mutableStateOf(false) }
    val savedImageUris = remember { mutableStateOf(mutableListOf<Uri>()) }

    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    val ubicacion = Ubicacion(context)

    if (formularioId != 0L) {
        val formulario: FormularioCincoEntity? = runBlocking {
            Log.d("Formulario5Loading", "Loading formulario5 with ID $formularioId")
            appContainer.formulariosRepository.getFormularioCincoStream(formularioId).first()
        }

        if (formulario != null) {
            readOnly = formulario.synced

            zona = formulario.zona
            clima = formulario.clima
            temporada = formulario.temporada
            tipoAnimal = formulario.tipoAnimal
            nombreComun = formulario.nombreComun
            nombreCientifico = formulario.nombreCientifico
            numeroIndividuos = formulario.numeroIndividuos
            tipoObservacion = formulario.tipoObservacion
            alturaObservacion = formulario.alturaObservacion
            observaciones = formulario.observaciones
            fecha = formulario.fecha
            editado = formulario.editado

            location = if (formulario.latitude != null && formulario.longitude != null) {
                Pair(formulario.latitude, formulario.longitude)
            } else null

            val storedImages = runBlocking {
                appContainer.formulariosRepository.getImagesByFormulario(formularioId, "Formulario5").first()
            }
            savedImageUris.value = storedImages.mapNotNull { ie ->
                runCatching { Uri.parse(ie.imageUri) }.getOrNull()
            }.toMutableList()
        } else {
            Log.e("Formulario5Loading", "NO se pudo obtener el formulario5 con id $formularioId")
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
                        text = "Formulario 5",
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
        }
    ) { paddingValues ->
        if (showCamera) {
            CameraWindow(
                activity = context,
                cameraViewModel = cameraViewModel,
                savedImageUris = savedImageUris,
                onClose = { showCamera = false },
                onGalleryClick = { }
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                if (readOnly) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFF3CD), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) { Text("Formulario subido: solo lectura", color = Color(0xFF856404)) }
                }

                // Zona (ejemplo con RadioButtons)
                Text("Zona")
                val zonas = listOf("Bosque", "Arreglo Agroforestal", "Cultivos Transitorios", "Cultivos Permanentes")
                if (zona.isEmpty()) zona = zonas[0]
                Column {
                    zonas.forEach { option ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = zona == option,
                                onClick = { if (!readOnly) zona = option },
                                enabled = !readOnly,
                                colors = colors(selectedColor = Color(0xFF4E7029), unselectedColor = Color.Gray)
                            )
                            Text(option, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }

                // Clima
                Text("Estado del Tiempo:")
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    maxItemsInEachRow = 3
                ) {
                    val opts = listOf("Soleado", "Parcialmente Nublado", "Lluvioso")
                    val icons = listOf(R.drawable.sunny, R.drawable.cloudy, R.drawable.rainy)
                    opts.forEachIndexed { i, op ->
                        IconToggleButton(
                            checked = clima == op,
                            onCheckedChange = { if (!readOnly) clima = op },
                            enabled = !readOnly,
                            modifier = Modifier.size(150.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .border(2.dp, if (clima == op) Color(0xFF4E7029) else Color.Transparent, RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = icons[i]),
                                    contentDescription = op,
                                    modifier = Modifier.requiredSize(95.dp)
                                )
                            }
                        }
                    }
                }

                // Época
                Text("Época")
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    val seasonOptions = listOf("Verano/Seca", "Invierno/Lluviosa")
                    seasonOptions.forEach { option ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = temporada == option,
                                onClick = { if (!readOnly) temporada = option },
                                enabled = !readOnly,
                                colors = colors(selectedColor = Color(0xFF4E7029), unselectedColor = Color.Gray)
                            )
                            Text(option, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }

                // Tipo Animal
                Text("Tipo de Animal")
                val animals = listOf("Mamífero", "Ave", "Reptil", "Anfibio", "Insecto")
                if (tipoAnimal.isEmpty()) tipoAnimal = animals[0]
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    maxItemsInEachRow = 3
                ) {
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
                                    .border(2.dp, if (tipoAnimal == animal) Color(0xFF4E7029) else Color.Transparent, RoundedCornerShape(8.dp))
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
                val obsOps = listOf("La Vió", "Huella", "Rastro", "Cacería", "Le dijeron")
                if (tipoObservacion.isEmpty()) tipoObservacion = obsOps[0]
                Column {
                    obsOps.forEach { option ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = tipoObservacion == option,
                                onClick = { if (!readOnly) tipoObservacion = option },
                                enabled = !readOnly,
                                colors = colors(selectedColor = Color(0xFF4E7029), unselectedColor = Color.Gray)
                            )
                            Text(option, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
                OutlinedTextField(
                    value = alturaObservacion,
                    onValueChange = { if (!readOnly) alturaObservacion = it },
                    label = { Text("Altura Observación") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !readOnly
                )
                OutlinedTextField(
                    value = observaciones,
                    onValueChange = { if (!readOnly) observaciones = it },
                    label = { Text("Observaciones") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    maxLines = 4,
                    enabled = !readOnly
                )

                // Fotos
                Button(
                    onClick = { if (!readOnly) showCamera = true },
                    enabled = !readOnly,
                    colors = buttonColors(containerColor = Color(0xFF4E7029), contentColor = Color.White),
                    shape = RoundedCornerShape(10)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Take Photo", modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Tomar Foto")
                }

                savedImageUris.value.forEach { uri ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(model = uri),
                            contentDescription = "Saved Image",
                            modifier = Modifier.size(100.dp)
                        )
                        Button(
                            onClick = {
                                if (!readOnly) savedImageUris.value =
                                    savedImageUris.value.toMutableList().apply { remove(uri) }
                            },
                            enabled = !readOnly,
                            colors = buttonColors(containerColor = Color.Transparent),
                            elevation = null
                        ) { Text("X", color = Color.Red, fontSize = 20.sp) }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { navController.navigate("home") },
                        colors = buttonColors(containerColor = Color(0xFF4E7029), contentColor = Color.White),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f)
                    ) { Text("Atras", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold)) }

                    Button(
                        onClick = {
                            if (readOnly) return@Button
                            if (fecha.isEmpty()) fecha = getCurrentDate()
                            editado = getCurrentDate()

                            val entity = FormularioCincoEntity(
                                zona = zona,
                                clima = clima,
                                temporada = temporada,
                                tipoAnimal = tipoAnimal,
                                nombreComun = nombreComun,
                                nombreCientifico = nombreCientifico,
                                numeroIndividuos = numeroIndividuos,
                                tipoObservacion = tipoObservacion,
                                alturaObservacion = alturaObservacion,
                                observaciones = observaciones,
                                latitude = location?.first,
                                longitude = location?.second,
                                fecha = fecha,
                                editado = editado
                            ).withID(formularioId)

                            runBlocking {
                                val formId = appContainer.usuariosRepository.insertUserWithFormularioCinco(
                                    context.accountInfo.user_id, entity
                                )
                                appContainer.formulariosRepository.deleteImagesByFormulario(formularioId = formId, formularioType = "Formulario5")
                                savedImageUris.value.forEach { u ->
                                    appContainer.formulariosRepository.insertImage(
                                        ImageEntity(formularioId = formId, formularioType = "Formulario5", imageUri = u.toString())
                                    )
                                }
                            }
                            navController.navigate("home")
                        },
                        enabled = !readOnly,
                        colors = buttonColors(containerColor = Color(0xFF4E7029), contentColor = Color.White),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f)
                    ) { Text("Guardar", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold)) }
                }
            }
        }
    }
}
