# BIOMO - Aplicación Android de Biomonitoreo

Biomo es una aplicación Android integral diseñada para el monitoreo biológico y la recolección de datos en investigaciones de campo. La aplicación permite a investigadores y biólogos de campo recolectar, almacenar y gestionar diversos tipos de datos de observación ambiental y de vida silvestre a través de formularios estructurados.

## Características

### 🔐 Autenticación
- Inicio de sesión seguro usando autenticación Auth0
- Gestión de cuentas de usuario con seguimiento de sesiones
- Soporte para autenticación de dos factores

### 📊 Formularios de Recolección de Datos
La aplicación incluye 7 formularios especializados de recolección de datos:

1. **Fauna en Transectos (Formulario 1)** - Observaciones de vida silvestre a lo largo de líneas de transecto
2. **Fauna en Punto de Conteo (Formulario 2)** - Conteo de vida silvestre en puntos específicos
3. **Validación de Cobertura (Formulario 3)** - Validación de cobertura vegetal
4. **Parcela de Vegetación (Formulario 4)** - Mediciones de parcelas de vegetación
5. **Fauna Búsqueda Libre (Formulario 5)** - Observaciones de vida silvestre en búsqueda libre
6. **Cámaras Trampa (Formulario 6)** - Instalación y monitoreo de cámaras trampa
7. **Variables Climáticas (Formulario 7)** - Registro de variables climáticas

### 📍 Servicios de Ubicación
- Captura de coordenadas GPS para todas las observaciones
- Gestión de permisos de ubicación
- Geotagging automático de entradas de datos

### 📱 Interfaz de Usuario
- Interfaz moderna Material Design 3
- Jetpack Compose para UI reactiva
- Navegación intuitiva entre formularios y características
- Dashboard con resumen de datos y estadísticas

### 💾 Gestión de Datos
- Base de datos SQLite local con persistencia Room
- Capacidad de recolección de datos sin conexión
- Sincronización y respaldo de datos
- Validación de completitud de formularios

### 🗺️ Características Adicionales
- Vista de mapa interactiva para visualización de ubicaciones
- Capacidades de búsqueda y filtrado para datos recolectados
- Configuración y gestión de perfil de usuario
- Sección "Acerca de" con información de la aplicación

## Stack Tecnológico

- **Lenguaje**: Kotlin
- **Framework UI**: Jetpack Compose
- **Arquitectura**: MVVM con patrón Repository
- **Base de Datos**: Room (SQLite)
- **Autenticación**: Auth0
- **Ubicación**: Google Play Services Location
- **Cámara**: AndroidX Camera2
- **Navegación**: Navigation Compose
- **Serialización**: Kotlinx Serialization

## Requisitos

- Android API 28 (Android 9.0) o superior
- Permiso de cámara para captura de fotos
- Permisos de ubicación para coordenadas GPS
- Conexión a internet para autenticación

## Permisos

La aplicación requiere los siguientes permisos:
- `CAMERA` - Para capturar fotos durante las observaciones
- `ACCESS_FINE_LOCATION` - Para coordenadas GPS precisas
- `ACCESS_COARSE_LOCATION` - Para ubicación aproximada
- `READ_EXTERNAL_STORAGE` - Para acceder a imágenes almacenadas
- `WRITE_EXTERNAL_STORAGE` - Para guardar imágenes capturadas
- `READ_MEDIA_IMAGES` - Para acceder a archivos multimedia
- `INTERNET` - Para autenticación y sincronización de datos

## Estructura del Proyecto

```
Biomo/arrozConLeche/
├── app/
│   ├── src/main/java/com/example/awaq1/
│   │   ├── data/           # Capa de datos (entidades, DAOs, repositorios)
│   │   ├── navigator/      # Configuración de navegación
│   │   ├── ui/theme/       # Temas de UI
│   │   └── view/           # Pantallas y componentes de UI
│   ├── src/main/res/       # Recursos (layouts, strings, imágenes)
│   └── build.gradle.kts    # Dependencias a nivel de aplicación
├── build.gradle.kts        # Configuración a nivel de proyecto
└── gradle/                 # Gradle wrapper y dependencias
```

## Comenzar

1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Configura las credenciales de Auth0 en `strings.xml`
4. Construye y ejecuta la aplicación

## Modelos de Datos

La aplicación utiliza entidades estructuradas para cada tipo de formulario, incluyendo:
- Métodos de validación de formularios
- Generación automática de ID
- Seguimiento de marcas de tiempo
- Almacenamiento de coordenadas GPS
- Seguimiento de asociación de usuarios

## Contribuir

Este proyecto parece ser parte de un curso universitario (5to semestre) enfocado en el desarrollo de aplicaciones móviles para aplicaciones de monitoreo biológico.

## Licencia

Este proyecto es parte de un curso académico y está destinado a fines educativos.