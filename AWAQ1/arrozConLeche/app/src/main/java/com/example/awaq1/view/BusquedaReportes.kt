import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.awaq1.MainActivity
import com.example.awaq1.R
import com.example.awaq1.data.formularios.FormInfo
import com.example.awaq1.ui.theme.components.BottomNavigationBar
import com.example.awaq1.ui.theme.components.DisplayCard
import com.example.awaq1.ui.theme.components.searchBar

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
    var filtroSeleccionado by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }

    //Filtrar los formularios segun el flitro seleccionado
    //TODO(Hay que agregar los filtros de subido y no subido)
    val filtered = remember(allForms, filtroSeleccionado, query) {
        allForms.filter { form ->
            val q = query.trim().lowercase()

            val matchesFilter = when(filtroSeleccionado){
                "Completo" -> form.completo
                "Incompleto" -> !form.completo
                "Subido" -> form.synced
                "No Subido" -> !form.synced
                else -> true
            }
            val matchesQuery = q.isBlank() || listOf(
                form.tipo,
                form.valorIdentificador,
                form.primerTag,
                form.primerContenido,
                form.segundoTag,
                form.segundoContenido,
                form.fechaCreacion,
                form.fechaEdicion
            ).any{it.lowercase().contains(q)}
            matchesFilter && matchesQuery
        }
    }
    val visibleList = if(filtroSeleccionado.isBlank() && query.isBlank()) allForms else filtered

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Observaciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
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
                        selected = filtroSeleccionado == "Completo",
                        onSelected = { filtroSeleccionado = if (filtroSeleccionado == "Completo") "" else "Completo" }
                    )
                    FilterButton(
                        text = "Incompleto",
                        selected = filtroSeleccionado == "Incompleto",
                        onSelected = { filtroSeleccionado = if (filtroSeleccionado == "Incompleto") "" else "Incompleto" }
                    )
                    FilterButton(
                        text = "Subido",
                        selected = filtroSeleccionado == "Subido",
                        onSelected = { filtroSeleccionado = if (filtroSeleccionado == "Subido") "" else "Subido"}
                    )
                    FilterButton(
                        text = "No Subido",
                        selected = filtroSeleccionado == "No Subido",
                        onSelected = { filtroSeleccionado = if (filtroSeleccionado == "No Subido") "" else "No Subido" }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                searchBar(value = query,
                    onValueChange = {query = it},
                    placeholder = "Buscar Formulario")

                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    items(visibleList) { form ->
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