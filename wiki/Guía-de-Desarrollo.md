# Guía de Desarrollo

Esta guía está dirigida a desarrolladores que quieran contribuir al proyecto BIOMO o entender su estructura de código.

## 🚀 Configuración del Entorno de Desarrollo

### Requisitos Previos

- **Android Studio**: Arctic Fox (2020.3.1) o superior
- **JDK**: 8 o superior
- **Git**: Para control de versiones
- **Android SDK**: API 28 o superior

### Configuración Inicial

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/tu-usuario/biomo.git
   cd biomo
   ```

2. **Configurar Auth0**:
   - Crear cuenta en [auth0.com](https://auth0.com)
   - Configurar credenciales en `app/src/main/res/values/strings.xml`

3. **Sincronizar proyecto**:
   - Abrir en Android Studio
   - Esperar sincronización automática de Gradle

## 📁 Estructura de Código

### Convenciones de Nomenclatura

#### Archivos y Clases
```kotlin
// Pantallas/Views
class HomeScreen.kt
class ObservationForm.kt

// ViewModels
class FormulariosViewModel.kt

// Repositorios
class FormulariosRepository.kt
class FormulariosRepositoryImpl.kt

// Entidades
class FormularioUnoEntity.kt
class UsuarioEntity.kt

// DAOs
class FormulariosDAO.kt
class UserDAO.kt
```

#### Variables y Funciones
```kotlin
// Variables
val userName: String
val isFormComplete: Boolean
val formList: List<Formulario>

// Funciones
fun loadFormularios()
fun validateForm()
fun saveFormulario()

// Constantes
companion object {
    private const val DATABASE_NAME = "formularios_database"
    private const val REQUEST_CODE_CAMERA = 100
}
```

### Estructura de Paquetes

```
com.example.awaq1/
├── data/                    # Capa de datos
│   ├── formularios/        # Formularios
│   ├── usuario/            # Usuarios
│   └── AppContainer.kt     # Inyección de dependencias
├── navigator/              # Navegación
├── ui/theme/              # Temas y estilos
├── view/                  # Pantallas UI
└── MainActivity.kt        # Actividad principal
```

## 🏗️ Patrones de Desarrollo

### 1. MVVM Pattern

#### View (Compose)
```kotlin
@Composable
fun ObservationForm(
    navController: NavController,
    formId: Long
) {
    val viewModel: FormulariosViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    when (uiState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Success -> FormContent(uiState.data)
        is UiState.Error -> ErrorScreen(uiState.message)
    }
}
```

#### ViewModel
```kotlin
@HiltViewModel
class FormulariosViewModel @Inject constructor(
    private val repository: FormulariosRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadFormulario(id: Long) {
        viewModelScope.launch {
            try {
                val formulario = repository.getFormularioById(id)
                _uiState.value = UiState.Success(formulario)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
```

### 2. Repository Pattern

```kotlin
interface FormulariosRepository {
    suspend fun getAllFormularios(): Flow<List<FormularioUnoEntity>>
    suspend fun insertFormulario(formulario: FormularioUnoEntity): Long
    suspend fun updateFormulario(formulario: FormularioUnoEntity)
    suspend fun deleteFormulario(id: Long)
}

@Singleton
class FormulariosRepositoryImpl @Inject constructor(
    private val dao: FormulariosDAO
) : FormulariosRepository {
    
    override suspend fun getAllFormularios(): Flow<List<FormularioUnoEntity>> {
        return dao.getAllFormularios()
    }
    
    // ... implementación de otros métodos
}
```

### 3. Dependency Injection

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideFormulariosDatabase(@ApplicationContext context: Context): FormulariosDatabase {
        return Room.databaseBuilder(
            context,
            FormulariosDatabase::class.java,
            "formularios_database"
        ).build()
    }
    
    @Provides
    fun provideFormulariosDAO(database: FormulariosDatabase): FormulariosDAO {
        return database.formulariosDao()
    }
}
```

## 🎨 Desarrollo de UI

### Composable Functions

#### Estructura Básica
```kotlin
@Composable
fun FormCard(
    formulario: Formulario,
    onEditClick: (Long) -> Unit,
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
            Text(
                text = formulario.titulo,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = formulario.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
```

#### State Management
```kotlin
@Composable
fun FormularioScreen(
    formId: Long,
    viewModel: FormulariosViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(formId) {
        viewModel.loadFormulario(formId)
    }
    
    // UI Content
}
```

### Navigation

#### Definición de Rutas
```kotlin
@Serializable
data class FormUnoID(val form_id: Long = 0)

@Serializable
data class FormDosID(val form_id: Long = 0)

// ... otras rutas
```

#### Configuración de Navegación
```kotlin
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Home(navController = navController)
        }
        
        composable<FormUnoID> { backStackEntry ->
            val formId = backStackEntry.toRoute()
            ObservationForm(navController, formId.form_id)
        }
        
        // ... otras rutas
    }
}
```

## 🗄️ Desarrollo de Base de Datos

### Entidades

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
}
```

### DAOs

```kotlin
@Dao
interface FormulariosDAO {
    
    @Query("SELECT * FROM Formulario1 WHERE id = :id")
    suspend fun getFormularioById(id: Long): FormularioUnoEntity?
    
    @Query("SELECT * FROM Formulario1 ORDER BY fecha DESC")
    fun getAllFormularios(): Flow<List<FormularioUnoEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormulario(formulario: FormularioUnoEntity): Long
    
    @Update
    suspend fun updateFormulario(formulario: FormularioUnoEntity)
    
    @Delete
    suspend fun deleteFormulario(formulario: FormularioUnoEntity)
    
    @Query("DELETE FROM Formulario1 WHERE id = :id")
    suspend fun deleteFormularioById(id: Long)
}
```

### Migraciones

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Formulario1 ADD COLUMN nueva_columna TEXT")
    }
}

@Database(
    entities = [FormularioUnoEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FormulariosDatabase : RoomDatabase() {
    // ... implementación
}
```

## 🧪 Testing

### Unit Tests

```kotlin
@RunWith(MockitoJUnitRunner::class)
class FormulariosRepositoryTest {
    
    @Mock
    private lateinit var mockDao: FormulariosDAO
    
    private lateinit var repository: FormulariosRepository
    
    @Before
    fun setup() {
        repository = FormulariosRepositoryImpl(mockDao)
    }
    
    @Test
    fun `insert formulario should return valid ID`() = runTest {
        // Given
        val formulario = FormularioUnoEntity(
            transecto = "T1",
            clima = "Soleado",
            // ... otros campos
        )
        val expectedId = 1L
        
        whenever(mockDao.insertFormulario(formulario)).thenReturn(expectedId)
        
        // When
        val result = repository.insertFormulario(formulario)
        
        // Then
        assertThat(result).isEqualTo(expectedId)
        verify(mockDao).insertFormulario(formulario)
    }
}
```

### UI Tests

```kotlin
@RunWith(AndroidJUnit4::class)
class FormularioScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun formularioScreen_displaysFormFields() {
        composeTestRule.setContent {
            BIOMOTheme {
                ObservationForm(
                    navController = mockNavController,
                    formId = 1L
                )
            }
        }
        
        composeTestRule.onNodeWithText("Transecto").assertIsDisplayed()
        composeTestRule.onNodeWithText("Clima").assertIsDisplayed()
        composeTestRule.onNodeWithText("Temporada").assertIsDisplayed()
    }
    
    @Test
    fun formularioScreen_savesFormWhenComplete() {
        // Test implementation
    }
}
```

## 🔧 Herramientas de Desarrollo

### Debugging

#### Logs
```kotlin
class FormulariosRepositoryImpl @Inject constructor(
    private val dao: FormulariosDAO
) {
    companion object {
        private const val TAG = "FormulariosRepository"
    }
    
    suspend fun insertFormulario(formulario: FormularioUnoEntity): Long {
        Log.d(TAG, "Inserting formulario: ${formulario.transecto}")
        return try {
            val id = dao.insertFormulario(formulario)
            Log.d(TAG, "Formulario inserted with ID: $id")
            id
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting formulario", e)
            throw e
        }
    }
}
```

#### Database Inspector
- Abrir Database Inspector en Android Studio
- Ver datos en tiempo real
- Ejecutar queries personalizadas

### Performance

#### Profiling
```kotlin
// Usar @Composable para funciones que se recomponen frecuentemente
@Composable
fun ExpensiveComputation() {
    val result by remember {
        derivedStateOf {
            // Cálculo costoso
            heavyComputation()
        }
    }
    
    Text(text = result.toString())
}
```

#### Memory Management
```kotlin
class FormulariosViewModel @Inject constructor(
    private val repository: FormulariosRepository
) : ViewModel() {
    
    // Usar viewModelScope para corrutinas
    fun loadData() {
        viewModelScope.launch {
            // Operaciones de red o base de datos
        }
    }
    
    // Limpiar recursos
    override fun onCleared() {
        super.onCleared()
        // Cleanup
    }
}
```

## 📱 Integración de Servicios

### Auth0

```kotlin
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val account = Auth0(
        context.getString(R.string.com_auth0_client_id),
        context.getString(R.string.com_auth0_domain)
    )
    
    suspend fun login(): Result<Credentials> {
        return try {
            val credentials = account.webAuth()
                .start(context, callback)
            Result.success(credentials)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Location Services

```kotlin
class LocationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    
    suspend fun getCurrentLocation(): Pair<Double, Double>? {
        return withContext(Dispatchers.IO) {
            try {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                location?.let { Pair(it.latitude, it.longitude) }
            } catch (e: SecurityException) {
                Log.e("LocationManager", "Location permission denied", e)
                null
            }
        }
    }
}
```

## 🚀 Deployment

### Build Variants

```kotlin
android {
    buildTypes {
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

### Signing

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("keystore/release.keystore")
            storePassword = "password"
            keyAlias = "alias"
            keyPassword = "password"
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

## 📋 Checklist de Desarrollo

### Antes de Commit

- [ ] Código compila sin errores
- [ ] Tests unitarios pasan
- [ ] Tests de UI pasan
- [ ] Código sigue convenciones de estilo
- [ ] Documentación actualizada
- [ ] Logs de debug removidos

### Code Review

- [ ] Lógica de negocio correcta
- [ ] Manejo de errores apropiado
- [ ] Performance optimizada
- [ ] UI/UX consistente
- [ ] Seguridad considerada

### Testing

- [ ] Casos de uso principales cubiertos
- [ ] Casos edge manejados
- [ ] Performance en dispositivos reales
- [ ] Compatibilidad con diferentes versiones de Android

---

**Próximo paso**: [Solución de Problemas](Solución-de-Problemas)
