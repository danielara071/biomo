# Arquitectura y Estructura Técnica

Esta página describe la arquitectura técnica de BIOMO, incluyendo patrones de diseño, estructura de código y tecnologías utilizadas.

## 🏗️ Arquitectura General

BIOMO sigue el patrón **MVVM (Model-View-ViewModel)** con elementos de **Repository Pattern** para una separación clara de responsabilidades.

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│      VIEW       │    │   VIEWMODEL     │    │     MODEL       │
│   (Compose UI)  │◄──►│  (State Mgmt)   │◄──►│  (Data Layer)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Navigation    │    │   Repository    │    │   Room DB       │
│   (Compose)     │    │   (Data Access) │    │   (Local)       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 📁 Estructura del Proyecto

```
app/src/main/java/com/example/awaq1/
├── data/                           # Capa de Datos
│   ├── formularios/               # Entidades y DAOs de formularios
│   │   ├── FormularioEntities.kt  # Entidades de base de datos
│   │   ├── FormulariosDAO.kt      # Data Access Objects
│   │   └── FormulariosRepository.kt # Repositorio de formularios
│   ├── usuario/                   # Gestión de usuarios
│   │   ├── UserEntities.kt        # Entidades de usuario
│   │   └── UserDAO.kt             # DAO de usuarios
│   ├── AppContainer.kt            # Contenedor de dependencias
│   └── AppDataContainer.kt        # Implementación del contenedor
├── navigator/                     # Navegación
│   └── AppNavigator.kt            # Configuración de navegación
├── ui/theme/                      # Temas y estilos
│   ├── Color.kt                   # Paleta de colores
│   ├── Theme.kt                   # Tema principal
│   └── Type.kt                    # Tipografía
├── view/                          # Pantallas y componentes UI
│   ├── Home.kt                    # Pantalla principal
│   ├── ObservationForm*.kt        # Formularios de observación
│   ├── Settings.kt                # Configuraciones
│   └── PrincipalView.kt           # Vista principal con auth
└── MainActivity.kt                # Actividad principal
```

## 🔧 Componentes Principales

### 1. Capa de Presentación (View)

**Tecnología**: Jetpack Compose

```kotlin
@Composable
fun Home(navController: NavController) {
    // UI State Management
    val forms by viewModel.forms.collectAsState()
    
    // UI Components
    LazyColumn {
        items(forms) { form ->
            FormCard(form) { formId ->
                navController.navigate("form/$formId")
            }
        }
    }
}
```

**Características**:
- **Composable Functions**: Componentes reutilizables
- **State Management**: Estado reactivo con `collectAsState()`
- **Navigation**: Navegación declarativa
- **Material Design 3**: Diseño moderno y consistente

### 2. Capa de Lógica de Negocio (ViewModel)

**Patrón**: Repository + State Management

```kotlin
class FormulariosViewModel(
    private val repository: FormulariosRepository
) : ViewModel() {
    
    private val _forms = MutableStateFlow<List<Formulario>>(emptyList())
    val forms: StateFlow<List<Formulario>> = _forms.asStateFlow()
    
    fun loadForms() {
        viewModelScope.launch {
            repository.getAllForms().collect { formsList ->
                _forms.value = formsList
            }
        }
    }
}
```

**Responsabilidades**:
- Gestión de estado de la UI
- Comunicación con la capa de datos
- Lógica de negocio
- Manejo de errores

### 3. Capa de Datos (Model)

#### Repository Pattern

```kotlin
interface FormulariosRepository {
    suspend fun insertFormulario(formulario: FormularioUnoEntity): Long
    suspend fun getAllFormularios(): Flow<List<FormularioUnoEntity>>
    suspend fun updateFormulario(formulario: FormularioUnoEntity)
    suspend fun deleteFormulario(id: Long)
}

class FormulariosRepositoryImpl(
    private val dao: FormulariosDAO
) : FormulariosRepository {
    // Implementación de métodos
}
```

#### Room Database

```kotlin
@Database(
    entities = [
        FormularioUnoEntity::class,
        FormularioDosEntity::class,
        // ... otros formularios
        UsuarioEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FormulariosDatabase : RoomDatabase() {
    abstract fun formulariosDao(): FormulariosDAO
    abstract fun usuariosDao(): UserDAO
}
```

## 🔄 Flujo de Datos

### 1. Creación de Formulario

```
User Input → Composable → ViewModel → Repository → DAO → Database
     ↓              ↓           ↓           ↓        ↓
   UI State    State Update  Business    Data     Storage
              Logic        Access
```

### 2. Lectura de Datos

```
Database → DAO → Repository → ViewModel → Composable → UI
    ↓        ↓        ↓           ↓           ↓
  Storage  Data    Business   State      UI
          Access   Logic     Update    Render
```

## 🗄️ Base de Datos

### Room Configuration

```kotlin
@Database(
    entities = [
        FormularioUnoEntity::class,
        FormularioDosEntity::class,
        FormularioTresEntity::class,
        FormularioCuatroEntity::class,
        FormularioCincoEntity::class,
        FormularioSeisEntity::class,
        FormularioSieteEntity::class,
        UsuarioEntity::class,
        UsuarioFormulario1Entity::class,
        // ... otras entidades de asociación
    ],
    version = 1,
    exportSchema = true
)
```

### Relaciones de Datos

```kotlin
// Relación Usuario-Formularios
@Entity(
    tableName = "UsuarioFormulario1",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FormularioUnoEntity::class,
            parentColumns = ["id"],
            childColumns = ["formId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["usuarioId", "formId"]
)
```

## 🔐 Autenticación

### Auth0 Integration

```kotlin
class MainActivity : ComponentActivity() {
    private lateinit var account: Auth0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        account = Auth0.getInstance(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )
    }
}
```

### User Management

```kotlin
data class AccountInfo(
    val username: String,
    val user_id: Long
)

@Entity(tableName = "Usuarios")
data class UsuarioEntity(
    val username: String,
    val lastAccess: String,
    val lastLogin: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
)
```

## 📍 Servicios de Ubicación

### GPS Integration

```kotlin
class Ubicacion(private val context: Context) {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    
    suspend fun obtenerCoordenadas(): Pair<Double, Double>? {
        // Implementación de obtención de coordenadas GPS
    }
    
    fun hasLocationPermission(): Boolean {
        // Verificación de permisos de ubicación
    }
}
```

## 🎨 UI/UX Architecture

### Theme System

```kotlin
@Composable
fun BIOMOTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
```

### Navigation

```kotlin
@Composable
fun AppNavigator(onLogout: () -> Unit) {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { Home(navController) }
        composable<FormUnoID> { backStackEntry ->
            val formId = backStackEntry.toRoute()
            ObservationForm(navController, formId.form_id)
        }
        // ... otras rutas
    }
}
```

## 🔧 Dependency Injection

### AppContainer

```kotlin
interface AppContainer {
    val formulariosRepository: FormulariosRepository
    val usuariosRepository: UsuariosRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val database = Room.databaseBuilder(
        context,
        FormulariosDatabase::class.java,
        "formularios_database"
    ).build()
    
    override val formulariosRepository: FormulariosRepository by lazy {
        FormulariosRepositoryImpl(database.formulariosDao())
    }
    
    override val usuariosRepository: UsuariosRepository by lazy {
        UsuariosRepositoryImpl(database.usuariosDao())
    }
}
```

## 📱 Platform Integration

### Permissions

```kotlin
class MainActivity : ComponentActivity() {
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted) {
                Log.d("MainActivity", "Location permission denied")
            }
        }
    }
}
```

### Camera Integration

```kotlin
// Camera permissions and setup
private val CAMERA_PERMISSION = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)
```

## 🧪 Testing Architecture

### Unit Testing

```kotlin
@Test
fun `insert formulario should return valid ID`() = runTest {
    val formulario = FormularioUnoEntity(
        transecto = "T1",
        clima = "Soleado",
        // ... otros campos
    )
    
    val id = repository.insertFormulario(formulario)
    assertThat(id).isGreaterThan(0)
}
```

### UI Testing

```kotlin
@Test
fun homeScreen_displaysForms() {
    composeTestRule.setContent {
        BIOMOTheme {
            Home(navController = mockNavController)
        }
    }
    
    composeTestRule.onNodeWithText("Dashboard").assertIsDisplayed()
}
```

## 📊 Performance Considerations

### Database Optimization

- **Indexing**: Índices en campos de búsqueda frecuente
- **Pagination**: Carga paginada de formularios
- **Background Processing**: Operaciones de DB en background threads

### UI Performance

- **Lazy Loading**: `LazyColumn` para listas grandes
- **State Management**: Uso eficiente de `StateFlow`
- **Composition**: Minimizar recomposiciones innecesarias

## 🔄 Data Flow Patterns

### State Management

```kotlin
// Unidirectional data flow
User Action → ViewModel → Repository → Database
     ↑                              ↓
UI State ← StateFlow ← Repository ← Database
```

### Error Handling

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

## 🚀 Scalability Considerations

### Modular Architecture

- **Feature Modules**: Separación por funcionalidad
- **Shared Modules**: Componentes reutilizables
- **Data Modules**: Separación de capas de datos

### Future Enhancements

- **Offline Sync**: Sincronización con servidor
- **Real-time Updates**: WebSocket integration
- **Analytics**: Tracking de uso de la aplicación

---

**Próximo paso**: [Guía de Desarrollo](Guía-de-Desarrollo)
