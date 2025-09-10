# API Reference

Esta página documenta las APIs internas de BIOMO para desarrolladores que quieran entender o extender la funcionalidad.

## 📊 Data Layer APIs

### FormulariosRepository

```kotlin
interface FormulariosRepository {
    // Formulario 1 - Fauna en Transectos
    suspend fun insertFormularioUno(formulario: FormularioUnoEntity): Long
    suspend fun getAllFormularioUno(): Flow<List<FormularioUnoEntity>>
    suspend fun getFormularioUnoById(id: Long): FormularioUnoEntity?
    suspend fun updateFormularioUno(formulario: FormularioUnoEntity)
    suspend fun deleteFormularioUno(id: Long)
    
    // Formulario 2 - Fauna en Punto de Conteo
    suspend fun insertFormularioDos(formulario: FormularioDosEntity): Long
    suspend fun getAllFormularioDos(): Flow<List<FormularioDosEntity>>
    suspend fun getFormularioDosById(id: Long): FormularioDosEntity?
    suspend fun updateFormularioDos(formulario: FormularioDosEntity)
    suspend fun deleteFormularioDos(id: Long)
    
    // ... otros formularios
    
    // Métodos generales
    suspend fun getAllFormulariosCount(): Flow<Int>
    suspend fun getFormulariosByUser(userId: Long): Flow<List<Any>>
}
```

### UsuariosRepository

```kotlin
interface UsuariosRepository {
    suspend fun insertUser(usuario: UsuarioEntity): Long
    suspend fun getUserById(id: Long): UsuarioEntity?
    suspend fun getUserByUsername(username: String): UsuarioEntity?
    suspend fun updateUser(usuario: UsuarioEntity)
    suspend fun deleteUser(id: Long)
    
    // Asociaciones Usuario-Formulario
    suspend fun associateUserWithFormulario1(userId: Long, formId: Long)
    suspend fun getFormulariosUnoForUser(userId: Long): Flow<List<FormularioUnoEntity>>
    // ... otros métodos de asociación
}
```

## 🗄️ Database APIs

### FormulariosDAO

```kotlin
@Dao
interface FormulariosDAO {
    
    // Formulario 1 - Fauna en Transectos
    @Query("SELECT * FROM Formulario1 WHERE id = :id")
    suspend fun getFormularioUnoById(id: Long): FormularioUnoEntity?
    
    @Query("SELECT * FROM Formulario1 ORDER BY fecha DESC")
    fun getAllFormularioUno(): Flow<List<FormularioUnoEntity>>
    
    @Query("SELECT * FROM Formulario1 WHERE latitude IS NOT NULL AND longitude IS NOT NULL")
    fun getFormularioUnoWithLocation(): Flow<List<FormularioUnoEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormularioUno(formulario: FormularioUnoEntity): Long
    
    @Update
    suspend fun updateFormularioUno(formulario: FormularioUnoEntity)
    
    @Delete
    suspend fun deleteFormularioUno(formulario: FormularioUnoEntity)
    
    @Query("DELETE FROM Formulario1 WHERE id = :id")
    suspend fun deleteFormularioUnoById(id: Long)
    
    // Formulario 2 - Fauna en Punto de Conteo
    @Query("SELECT * FROM Formulario2 WHERE id = :id")
    suspend fun getFormularioDosById(id: Long): FormularioDosEntity?
    
    @Query("SELECT * FROM Formulario2 ORDER BY fecha DESC")
    fun getAllFormularioDos(): Flow<List<FormularioDosEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormularioDos(formulario: FormularioDosEntity): Long
    
    @Update
    suspend fun updateFormularioDos(formulario: FormularioDosEntity)
    
    @Delete
    suspend fun deleteFormularioDos(formulario: FormularioDosEntity)
    
    // ... otros formularios
    
    // Queries complejas
    @Query("""
        SELECT COUNT(*) FROM Formulario1 
        WHERE fecha BETWEEN :startDate AND :endDate
    """)
    suspend fun getFormularioUnoCountByDateRange(startDate: String, endDate: String): Int
    
    @Query("""
        SELECT * FROM Formulario1 
        WHERE nombreCientifico LIKE :searchTerm 
        OR nombreComun LIKE :searchTerm
    """)
    fun searchFormularioUno(searchTerm: String): Flow<List<FormularioUnoEntity>>
}
```

### UserDAO

```kotlin
@Dao
interface UserDAO {
    @Query("SELECT * FROM Usuarios WHERE id = :id")
    suspend fun getUserById(id: Long): UsuarioEntity?
    
    @Query("SELECT * FROM Usuarios WHERE username = :username")
    suspend fun getUserByUsername(username: String): UsuarioEntity?
    
    @Query("SELECT * FROM Usuarios ORDER BY lastLogin DESC")
    fun getAllUsers(): Flow<List<UsuarioEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(usuario: UsuarioEntity): Long
    
    @Update
    suspend fun updateUser(usuario: UsuarioEntity)
    
    @Delete
    suspend fun deleteUser(usuario: UsuarioEntity)
    
    // Asociaciones Usuario-Formulario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuarioFormulario1(association: UsuarioFormulario1Entity)
    
    @Query("""
        SELECT f.* FROM Formulario1 f
        INNER JOIN UsuarioFormulario1 uf ON f.id = uf.formId
        WHERE uf.usuarioId = :userId
        ORDER BY f.fecha DESC
    """)
    fun getFormulariosUnoForUser(userId: Long): Flow<List<FormularioUnoEntity>>
}
```

## 🎯 ViewModel APIs

### FormulariosViewModel

```kotlin
class FormulariosViewModel @Inject constructor(
    private val repository: FormulariosRepository
) : ViewModel() {
    
    // State Management
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    private val _formularios = MutableStateFlow<List<FormularioUnoEntity>>(emptyList())
    val formularios: StateFlow<List<FormularioUnoEntity>> = _formularios.asStateFlow()
    
    // Actions
    fun loadFormularios() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                repository.getAllFormularioUno().collect { forms ->
                    _formularios.value = forms
                    _uiState.value = UiState.Success
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
    
    fun insertFormulario(formulario: FormularioUnoEntity) {
        viewModelScope.launch {
            try {
                val id = repository.insertFormularioUno(formulario)
                loadFormularios() // Refresh list
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error al insertar")
            }
        }
    }
    
    fun updateFormulario(formulario: FormularioUnoEntity) {
        viewModelScope.launch {
            try {
                repository.updateFormularioUno(formulario)
                loadFormularios() // Refresh list
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error al actualizar")
            }
        }
    }
    
    fun deleteFormulario(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteFormularioUno(id)
                loadFormularios() // Refresh list
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error al eliminar")
            }
        }
    }
}

sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
}
```

## 📍 Location APIs

### Ubicacion

```kotlin
class Ubicacion(private val context: Context) {
    
    companion object {
        private const val TAG = "Ubicacion"
    }
    
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    suspend fun obtenerCoordenadas(): Pair<Double, Double>? {
        return withContext(Dispatchers.IO) {
            try {
                if (!hasLocationPermission()) {
                    Log.w(TAG, "Location permission not granted")
                    return@withContext null
                }
                
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                
                location?.let { 
                    Log.d(TAG, "Location obtained: ${it.latitude}, ${it.longitude}")
                    Pair(it.latitude, it.longitude) 
                }
            } catch (e: SecurityException) {
                Log.e(TAG, "Location permission denied", e)
                null
            } catch (e: Exception) {
                Log.e(TAG, "Error obtaining location", e)
                null
            }
        }
    }
    
    fun requestLocationPermission(activity: Activity, launcher: ActivityResultLauncher<String>) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
```

## 🔐 Authentication APIs

### AuthManager

```kotlin
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val account = Auth0(
        context.getString(R.string.com_auth0_client_id),
        context.getString(R.string.com_auth0_domain)
    )
    
    suspend fun login(activity: Activity): Result<Credentials> {
        return try {
            val credentials = account.webAuth()
                .withScope("openid profile email")
                .start(activity, callback)
            Result.success(credentials)
        } catch (e: Exception) {
            Log.e("AuthManager", "Login failed", e)
            Result.failure(e)
        }
    }
    
    fun logout(activity: Activity, callback: (Boolean) -> Unit) {
        account.webAuth()
            .clearSession(callback)
    }
    
    fun getCurrentUser(): UserProfile? {
        return account.userInfo
    }
}
```

## 🎨 UI Component APIs

### FormularioCard

```kotlin
@Composable
fun FormularioCard(
    formulario: FormularioUnoEntity,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onEditClick(formulario.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Transecto: ${formulario.transecto}",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                IconButton(onClick = { onDeleteClick(formulario.id) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar"
                    )
                }
            }
            
            Text(
                text = "Tipo: ${formulario.tipoAnimal}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Text(
                text = "Especie: ${formulario.nombreComun}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Creado: ${formulario.fecha}",
                    style = MaterialTheme.typography.bodySmall
                )
                
                if (formulario.esCompleto()) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completo",
                        tint = Color.Green
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Incompleto",
                        tint = Color.Orange
                    )
                }
            }
        }
    }
}
```

### FormularioInput

```kotlin
@Composable
fun FormularioInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isRequired: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { 
            Text(
                text = if (isRequired) "$label *" else label
            ) 
        },
        modifier = modifier.fillMaxWidth(),
        isError = isRequired && value.isBlank(),
        supportingText = if (isRequired && value.isBlank()) {
            Text("Este campo es requerido")
        } else null
    )
}
```

## 🧪 Testing APIs

### Test Utilities

```kotlin
class TestUtils {
    companion object {
        fun createTestFormularioUno(): FormularioUnoEntity {
            return FormularioUnoEntity(
                transecto = "T1",
                clima = "Soleado",
                temporada = "Seco",
                tipoAnimal = "Ave",
                nombreComun = "Gorrión",
                nombreCientifico = "Passer domesticus",
                numeroIndividuos = "5",
                tipoObservacion = "Visual",
                observaciones = "Test observation",
                latitude = 19.4326,
                longitude = -99.1332,
                fecha = "2024-01-01",
                editado = "2024-01-01"
            )
        }
        
        fun createTestUsuario(): UsuarioEntity {
            return UsuarioEntity(
                username = "test@example.com",
                lastAccess = "2024-01-01",
                lastLogin = "2024-01-01"
            )
        }
    }
}
```

### Mock Repositories

```kotlin
class MockFormulariosRepository : FormulariosRepository {
    private val formularios = mutableListOf<FormularioUnoEntity>()
    
    override suspend fun insertFormularioUno(formulario: FormularioUnoEntity): Long {
        val id = formularios.size.toLong() + 1
        formularios.add(formulario.copy(id = id))
        return id
    }
    
    override suspend fun getAllFormularioUno(): Flow<List<FormularioUnoEntity>> {
        return flowOf(formularios.toList())
    }
    
    // ... otros métodos mock
}
```

## 📊 Data Models

### FormularioUnoEntity

```kotlin
@Entity(tableName = "Formulario1")
data class FormularioUnoEntity(
    var transecto: String,
    var clima: String,
    var temporada: String,
    var tipoAnimal: String,
    var nombreComun: String,
    var nombreCientifico: String,
    var numeroIndividuos: String,
    var tipoObservacion: String,
    var observaciones: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val fecha: String,
    val editado: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    
    fun esCompleto(): Boolean {
        return !(transecto.isBlank() ||
                clima.isBlank() ||
                temporada.isBlank() ||
                tipoAnimal.isBlank() ||
                nombreCientifico.isBlank() ||
                nombreComun.isBlank() ||
                numeroIndividuos.isBlank() ||
                tipoObservacion.isBlank())
    }
    
    fun withID(id: Long): FormularioUnoEntity {
        val newForm = this.copy()
        newForm.id = id
        return newForm
    }
}
```

### UsuarioEntity

```kotlin
@Entity(tableName = "Usuarios")
data class UsuarioEntity(
    val username: String,
    val lastAccess: String,
    val lastLogin: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
```

## 🔧 Utility Functions

### Date Utils

```kotlin
object DateUtils {
    fun getCurrentDateString(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Date())
    }
    
    fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            dateString
        }
    }
}
```

### Validation Utils

```kotlin
object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    fun isValidCoordinate(latitude: Double?, longitude: Double?): Boolean {
        return latitude != null && longitude != null &&
               latitude in -90.0..90.0 && longitude in -180.0..180.0
    }
    
    fun isRequiredField(value: String): Boolean {
        return value.isNotBlank()
    }
}
```

---

**Para más información**: [Guía de Desarrollo](Guía-de-Desarrollo) | [Arquitectura Técnica](Arquitectura-y-Estructura)
