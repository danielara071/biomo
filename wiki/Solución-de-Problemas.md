# Solución de Problemas

Esta guía te ayudará a resolver los problemas más comunes que pueden surgir al usar o desarrollar BIOMO.

## 🚨 Problemas de Instalación

### Error: "Could not resolve all dependencies"

**Síntomas**:
```
Could not resolve all dependencies for configuration ':app:debugRuntimeClasspath'
```

**Causas**:
- Conexión a internet inestable
- Configuración incorrecta de repositorios
- Versiones incompatibles de dependencias

**Soluciones**:
1. **Verificar conexión a internet**
2. **Limpiar caché de Gradle**:
   ```bash
   ./gradlew clean
   ./gradlew build --refresh-dependencies
   ```
3. **Invalidar caché de Android Studio**:
   - `File > Invalidate Caches and Restart`
4. **Verificar configuración de repositorios** en `build.gradle`:
   ```kotlin
   repositories {
       google()
       mavenCentral()
   }
   ```

### Error: "SDK location not found"

**Síntomas**:
```
SDK location not found. Define location with sdk.dir in the local.properties file
```

**Soluciones**:
1. **Crear archivo `local.properties`**:
   ```properties
   sdk.dir=C:\\Users\\TuUsuario\\AppData\\Local\\Android\\Sdk
   ```
2. **Verificar instalación del SDK** en Android Studio
3. **Configurar variable de entorno** `ANDROID_HOME`

### Error: "Gradle sync failed"

**Síntomas**:
```
Gradle sync failed: Could not find method android()
```

**Soluciones**:
1. **Verificar versión de Gradle** en `gradle/wrapper/gradle-wrapper.properties`
2. **Actualizar Android Gradle Plugin** en `build.gradle`
3. **Sincronizar proyecto** manualmente

## 🔐 Problemas de Autenticación

### Error: "Auth0 configuration error"

**Síntomas**:
```
Auth0 configuration error: Invalid client ID or domain
```

**Causas**:
- Credenciales incorrectas en `strings.xml`
- Configuración incorrecta en Auth0 dashboard
- URLs de callback mal configuradas

**Soluciones**:
1. **Verificar credenciales** en `app/src/main/res/values/strings.xml`:
   ```xml
   <string name="com_auth0_client_id">TU_CLIENT_ID_CORRECTO</string>
   <string name="com_auth0_domain">tu-dominio.auth0.com</string>
   ```
2. **Verificar configuración en Auth0**:
   - Client ID correcto
   - Domain correcto
   - URLs de callback configuradas
3. **Verificar formato de URLs**:
   ```
   https://tu-dominio.auth0.com/android/com.example.awaq1/callback
   ```

### Error: "Login failed"

**Síntomas**:
- Pantalla de login no responde
- Error después de ingresar credenciales

**Soluciones**:
1. **Verificar conexión a internet**
2. **Verificar configuración de Auth0**
3. **Revisar logs** en Android Studio:
   ```kotlin
   Log.d("Auth0", "Login attempt: $credentials")
   ```
4. **Probar en dispositivo físico** si estás usando emulador

## 📍 Problemas de Ubicación

### Error: "Location permission denied"

**Síntomas**:
```
Location permission is required but not granted
```

**Soluciones**:
1. **Verificar permisos en AndroidManifest.xml**:
   ```xml
   <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
   <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
   ```
2. **Solicitar permisos en tiempo de ejecución**:
   ```kotlin
   if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
       != PackageManager.PERMISSION_GRANTED) {
       ActivityCompat.requestPermissions(this, 
           arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 
           REQUEST_CODE_LOCATION)
   }
   ```
3. **Habilitar GPS** en el dispositivo
4. **Verificar configuración de ubicación** en Settings

### Error: "Location not available"

**Síntomas**:
- Coordenadas GPS no se obtienen
- Ubicación aparece como null

**Soluciones**:
1. **Verificar GPS habilitado** en el dispositivo
2. **Probar en ubicación abierta** (no en interiores)
3. **Configurar ubicación en emulador**:
   - `Extended Controls > Location`
   - Establecer coordenadas de prueba
4. **Verificar proveedores de ubicación**:
   ```kotlin
   val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
   val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
   ```

## 🗄️ Problemas de Base de Datos

### Error: "Room database error"

**Síntomas**:
```
Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number
```

**Soluciones**:
1. **Incrementar versión de base de datos**:
   ```kotlin
   @Database(
       entities = [...],
       version = 2, // Incrementar versión
       exportSchema = true
   )
   ```
2. **Agregar migración**:
   ```kotlin
   val MIGRATION_1_2 = object : Migration(1, 2) {
       override fun migrate(database: SupportSQLiteDatabase) {
           // Código de migración
       }
   }
   ```
3. **Limpiar datos de la app** en Settings

### Error: "Database locked"

**Síntomas**:
```
SQLiteDatabaseLockedException: database is locked
```

**Soluciones**:
1. **Cerrar todas las instancias** de la app
2. **Reiniciar el dispositivo**
3. **Verificar transacciones** no cerradas
4. **Usar `allowMainThreadQueries()`** solo para testing

### Error: "Entity not found"

**Síntomas**:
```
NoSuchElementException: Collection contains no element matching the predicate
```

**Soluciones**:
1. **Verificar ID** del formulario
2. **Verificar que el formulario existe** antes de acceder
3. **Usar `find()` en lugar de `first()`**:
   ```kotlin
   val formulario = formularios.find { it.id == formId }
   if (formulario != null) {
       // Usar formulario
   }
   ```

## 📱 Problemas de UI

### Error: "Compose recomposition issues"

**Síntomas**:
- UI no se actualiza
- Performance lenta
- Crashes inesperados

**Soluciones**:
1. **Evitar recomposiciones innecesarias**:
   ```kotlin
   @Composable
   fun ExpensiveComponent() {
       val data by remember {
           derivedStateOf {
               heavyComputation()
           }
       }
   }
   ```
2. **Usar `remember` para valores costosos**
3. **Evitar lógica pesada en composables**

### Error: "Navigation issues"

**Síntomas**:
- Navegación no funciona
- Rutas no encontradas
- Back stack incorrecto

**Soluciones**:
1. **Verificar definición de rutas**:
   ```kotlin
   @Serializable
   data class FormUnoID(val form_id: Long = 0)
   ```
2. **Verificar configuración de NavHost**
3. **Usar `rememberNavController()`** correctamente
4. **Verificar argumentos de navegación**

### Error: "State not updating"

**Síntomas**:
- UI no refleja cambios de estado
- Datos no se actualizan

**Soluciones**:
1. **Verificar StateFlow/Flow**:
   ```kotlin
   val uiState by viewModel.uiState.collectAsState()
   ```
2. **Verificar que el ViewModel esté inyectado** correctamente
3. **Verificar que el estado se esté emitiendo**:
   ```kotlin
   _uiState.value = newState
   ```

## 🔧 Problemas de Performance

### Error: "App crashes with OutOfMemoryError"

**Síntomas**:
```
java.lang.OutOfMemoryError: Failed to allocate memory
```

**Soluciones**:
1. **Optimizar imágenes**:
   - Usar formatos eficientes (WebP)
   - Redimensionar imágenes
   - Usar bibliotecas como Coil
2. **Limpiar recursos**:
   ```kotlin
   override fun onCleared() {
       super.onCleared()
       // Limpiar recursos
   }
   ```
3. **Usar paginación** para listas grandes

### Error: "Slow database operations"

**Síntomas**:
- App se congela durante operaciones de DB
- Tiempo de respuesta lento

**Soluciones**:
1. **Ejecutar operaciones en background**:
   ```kotlin
   viewModelScope.launch(Dispatchers.IO) {
       val result = repository.getData()
       withContext(Dispatchers.Main) {
           // Actualizar UI
       }
   }
   ```
2. **Usar índices** en la base de datos
3. **Optimizar queries** complejas

## 📷 Problemas de Cámara

### Error: "Camera permission denied"

**Síntomas**:
```
Permission denied: android.permission.CAMERA
```

**Soluciones**:
1. **Verificar permisos en AndroidManifest.xml**:
   ```xml
   <uses-permission android:name="android.permission.CAMERA" />
   ```
2. **Solicitar permisos en tiempo de ejecución**
3. **Verificar que la cámara esté disponible**:
   ```kotlin
   if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
       // Usar cámara
   }
   ```

### Error: "Camera not working"

**Síntomas**:
- Cámara no se abre
- Imagen no se captura

**Soluciones**:
1. **Verificar que la cámara no esté en uso** por otra app
2. **Probar en dispositivo físico** (emulador puede tener limitaciones)
3. **Verificar configuración de CameraX**

## 🐛 Debugging

### Herramientas de Debug

#### 1. Logs
```kotlin
Log.d("TAG", "Debug message")
Log.e("TAG", "Error message", exception)
Log.w("TAG", "Warning message")
```

#### 2. Database Inspector
- Abrir en Android Studio
- Ver datos en tiempo real
- Ejecutar queries

#### 3. Layout Inspector
- Inspeccionar jerarquía de UI
- Ver propiedades de componentes

#### 4. Profiler
- Monitorear CPU, memoria, red
- Identificar cuellos de botella

### Logs Útiles

```kotlin
// Auth0
Log.d("Auth0", "Login attempt: $credentials")

// Database
Log.d("Database", "Inserting formulario: $formulario")

// Location
Log.d("Location", "GPS coordinates: $latitude, $longitude")

// Navigation
Log.d("Navigation", "Navigating to: $route")
```

## 📞 Obtener Ayuda

### Antes de Reportar un Problema

1. **Revisar esta guía** de solución de problemas
2. **Verificar logs** en Android Studio
3. **Probar en dispositivo físico** si usas emulador
4. **Verificar versión** de Android y de la app

### Información para Reportar

Al reportar un problema, incluye:

1. **Descripción del problema**
2. **Pasos para reproducir**
3. **Comportamiento esperado vs actual**
4. **Logs relevantes**
5. **Información del dispositivo**:
   - Modelo
   - Versión de Android
   - Versión de la app
6. **Screenshots** si aplica

### Recursos Adicionales

- [Documentación de Android](https://developer.android.com/)
- [Documentación de Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Documentación de Room](https://developer.android.com/training/data-storage/room)
- [Documentación de Auth0](https://auth0.com/docs)

## 🔄 Soluciones Rápidas

### Reset Completo

Si nada funciona:

1. **Limpiar proyecto**:
   ```bash
   ./gradlew clean
   ```

2. **Invalidar caché**:
   - `File > Invalidate Caches and Restart`

3. **Reinstalar app**:
   - Desinstalar desde el dispositivo
   - Reinstalar desde Android Studio

4. **Reset de base de datos**:
   - Limpiar datos de la app en Settings
   - O desinstalar y reinstalar

### Verificación Rápida

```bash
# Verificar que todo compila
./gradlew assembleDebug

# Ejecutar tests
./gradlew test

# Verificar linting
./gradlew lint
```

---

**Última actualización**: Diciembre 2024

**¿Necesitas más ayuda?** Revisa la [Guía de Desarrollo](Guía-de-Desarrollo) o crea un issue en el repositorio.
