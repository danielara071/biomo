# Instalación y Configuración

Esta guía te ayudará a configurar el entorno de desarrollo para BIOMO y ejecutar la aplicación en tu dispositivo o emulador.

## 📋 Requisitos del Sistema

### Requisitos Mínimos
- **Android Studio**: Arctic Fox (2020.3.1) o superior
- **JDK**: 8 o superior
- **Android SDK**: API 28 (Android 9.0) o superior
- **RAM**: Mínimo 8GB recomendado
- **Espacio en disco**: 2GB libres

### Dispositivo/Emulador
- **Android**: 9.0 (API 28) o superior
- **RAM**: 2GB mínimo
- **Almacenamiento**: 500MB libres
- **Cámara**: Para captura de fotos (opcional)
- **GPS**: Para geolocalización

## 🚀 Configuración del Entorno

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/biomo.git
cd biomo
```

### 2. Abrir en Android Studio

1. Abre Android Studio
2. Selecciona "Open an existing project"
3. Navega a la carpeta `biomo` y selecciona el proyecto
4. Espera a que Android Studio sincronice el proyecto

### 3. Configurar Gradle

El proyecto debería sincronizarse automáticamente. Si hay problemas:

1. Ve a `File > Sync Project with Gradle Files`
2. O haz clic en el ícono de sincronización en la barra de herramientas

### 4. Configurar Auth0

#### Crear cuenta en Auth0

1. Ve a [auth0.com](https://auth0.com) y crea una cuenta gratuita
2. Crea una nueva aplicación:
   - Tipo: Native
   - Nombre: BIOMO
   - Platform: Android

#### Configurar la aplicación en Auth0

1. En el dashboard de Auth0, ve a Applications > BIOMO
2. En la pestaña "Settings", configura:
   - **Allowed Callback URLs**: `https://tu-dominio.auth0.com/android/com.example.awaq1/callback`
   - **Allowed Logout URLs**: `https://tu-dominio.auth0.com/android/com.example.awaq1/callback`

#### Configurar credenciales en la app

1. Abre `app/src/main/res/values/strings.xml`
2. Agrega las siguientes líneas:

```xml
<string name="com_auth0_client_id">TU_CLIENT_ID_AQUI</string>
<string name="com_auth0_domain">tu-dominio.auth0.com</string>
```

**⚠️ Importante**: Nunca subas estas credenciales al repositorio. Usa variables de entorno o archivos de configuración locales.

## 🔧 Configuración del Dispositivo/Emulador

### Configurar Emulador

1. En Android Studio, ve a `Tools > AVD Manager`
2. Crea un nuevo Virtual Device:
   - Selecciona un dispositivo (ej: Pixel 4)
   - Selecciona una imagen del sistema (API 28 o superior)
   - Configura las opciones avanzadas:
     - **RAM**: 2GB o más
     - **Internal Storage**: 2GB o más

### Configurar Dispositivo Físico

1. Habilita las opciones de desarrollador:
   - Ve a `Settings > About Phone`
   - Toca 7 veces en "Build Number"
2. Habilita la depuración USB:
   - Ve a `Settings > Developer Options`
   - Activa "USB Debugging"
3. Conecta el dispositivo via USB
4. Autoriza la depuración cuando aparezca el diálogo

## 🏃‍♂️ Ejecutar la Aplicación

### Primera Ejecución

1. Selecciona tu dispositivo/emulador en la barra de herramientas
2. Haz clic en el botón "Run" (▶️) o presiona `Shift + F10`
3. La aplicación se compilará e instalará automáticamente

### Verificar la Instalación

1. La aplicación debería abrirse automáticamente
2. Verifica que aparezca la pantalla de login
3. Prueba el login con una cuenta de Auth0

## 🔍 Verificar Permisos

La aplicación requiere varios permisos. Verifica que estén configurados correctamente:

### En el Emulador
1. Ve a `Settings > Apps > BIOMO > Permissions`
2. Asegúrate de que estén habilitados:
   - Camera
   - Location
   - Storage

### En Dispositivo Físico
Los permisos se solicitarán automáticamente la primera vez que uses cada función.

## 🐛 Solución de Problemas Comunes

### Error de Compilación

```
Could not resolve all dependencies
```

**Solución**: 
1. Verifica tu conexión a internet
2. Ve a `File > Invalidate Caches and Restart`
3. Sincroniza el proyecto nuevamente

### Error de Auth0

```
Auth0 configuration error
```

**Solución**:
1. Verifica que las credenciales en `strings.xml` sean correctas
2. Asegúrate de que la URL de callback esté configurada en Auth0
3. Verifica que el dominio de Auth0 sea correcto

### Error de Permisos

```
Permission denied
```

**Solución**:
1. Verifica que los permisos estén en el `AndroidManifest.xml`
2. En dispositivos físicos, ve a Settings y habilita los permisos manualmente
3. Reinicia la aplicación después de habilitar permisos

### Error de Ubicación

```
Location not available
```

**Solución**:
1. Asegúrate de que el GPS esté habilitado
2. En el emulador, configura una ubicación en `Extended Controls > Location`
3. Verifica que los permisos de ubicación estén habilitados

## 📱 Configuración Adicional

### Para Desarrollo

1. **Habilitar logs detallados**:
   ```kotlin
   // En MainActivity.kt
   Log.d("BIOMO", "Debug logs enabled")
   ```

2. **Configurar breakpoints**:
   - Coloca breakpoints en el código
   - Usa el debugger de Android Studio

3. **Monitorear base de datos**:
   - Usa Database Inspector en Android Studio
   - Ve a `View > Tool Windows > Database Inspector`

### Para Testing

1. **Ejecutar tests unitarios**:
   ```bash
   ./gradlew test
   ```

2. **Ejecutar tests de UI**:
   ```bash
   ./gradlew connectedAndroidTest
   ```

## ✅ Verificación Final

Una vez completada la instalación, verifica que:

- [ ] La aplicación se compila sin errores
- [ ] Se puede hacer login con Auth0
- [ ] Los permisos se solicitan correctamente
- [ ] La ubicación se obtiene correctamente
- [ ] Se pueden crear formularios
- [ ] Los datos se guardan en la base de datos

## 📞 Soporte

Si encuentras problemas durante la instalación:

1. Revisa la sección de [Solución de Problemas](Solución-de-Problemas)
2. Verifica los logs en Android Studio
3. Consulta la documentación oficial de Android
4. Crea un issue en el repositorio de GitHub

---

**Próximo paso**: [Documentación de Formularios](Formularios-de-Recolección)
