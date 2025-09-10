# Formularios de Recolección de Datos

BIOMO incluye 7 formularios especializados para diferentes tipos de observaciones biológicas. Cada formulario está diseñado para capturar datos específicos según el protocolo de investigación.

## 📋 Índice de Formularios

1. [Formulario 1: Fauna en Transectos](#formulario-1-fauna-en-transectos)
2. [Formulario 2: Fauna en Punto de Conteo](#formulario-2-fauna-en-punto-de-conteo)
3. [Formulario 3: Validación de Cobertura](#formulario-3-validación-de-cobertura)
4. [Formulario 4: Parcela de Vegetación](#formulario-4-parcela-de-vegetación)
5. [Formulario 5: Fauna Búsqueda Libre](#formulario-5-fauna-búsqueda-libre)
6. [Formulario 6: Cámaras Trampa](#formulario-6-cámaras-trampa)
7. [Formulario 7: Variables Climáticas](#formulario-7-variables-climáticas)

---

## Formulario 1: Fauna en Transectos

**Propósito**: Observaciones de vida silvestre a lo largo de líneas de transecto predefinidas.

### Campos Requeridos

| Campo | Tipo | Descripción | Validación |
|-------|------|-------------|------------|
| **Transecto** | String | Identificador del transecto | No vacío |
| **Clima** | String | Condiciones climáticas | No vacío |
| **Temporada** | String | Estación del año | No vacío |
| **Tipo de Animal** | String | Categoría del animal | No vacío |
| **Nombre Común** | String | Nombre común de la especie | No vacío |
| **Nombre Científico** | String | Nombre científico de la especie | No vacío |
| **Número de Individuos** | String | Cantidad observada | No vacío |
| **Tipo de Observación** | String | Método de observación | No vacío |
| **Observaciones** | String | Notas adicionales | Opcional |

### Campos Automáticos

- **Latitud/Longitud**: Coordenadas GPS automáticas
- **Fecha**: Timestamp de creación
- **Editado**: Timestamp de última modificación

### Uso Recomendado

- Estudios de abundancia de especies
- Monitoreo de poblaciones
- Inventarios de biodiversidad

---

## Formulario 2: Fauna en Punto de Conteo

**Propósito**: Conteo de vida silvestre en puntos específicos de observación.

### Campos Requeridos

| Campo | Tipo | Descripción | Validación |
|-------|------|-------------|------------|
| **Zona** | String | Identificador de la zona | No vacío |
| **Clima** | String | Condiciones climáticas | No vacío |
| **Temporada** | String | Estación del año | No vacío |
| **Tipo de Animal** | String | Categoría del animal | No vacío |
| **Nombre Común** | String | Nombre común de la especie | No vacío |
| **Nombre Científico** | String | Nombre científico de la especie | No vacío |
| **Número de Individuos** | String | Cantidad observada | No vacío |
| **Tipo de Observación** | String | Método de observación | No vacío |
| **Altura de Observación** | String | Altura desde donde se observa | No vacío |
| **Observaciones** | String | Notas adicionales | Opcional |

### Campos Automáticos

- **Latitud/Longitud**: Coordenadas GPS automáticas
- **Fecha**: Timestamp de creación
- **Editado**: Timestamp de última modificación

### Uso Recomendado

- Censos de aves
- Monitoreo de mamíferos
- Estudios de comportamiento

---

## Formulario 3: Validación de Cobertura

**Propósito**: Validación y monitoreo de cobertura vegetal en parcelas.

### Campos Requeridos

| Campo | Tipo | Descripción | Validación |
|-------|------|-------------|------------|
| **Código** | String | Identificador único de la parcela | No vacío |
| **Clima** | String | Condiciones climáticas | No vacío |
| **Temporada** | String | Estación del año | No vacío |
| **Seguimiento** | Boolean | Si es seguimiento de parcela existente | - |
| **Cambio** | Boolean | Si hubo cambios en la parcela | - |
| **Cobertura** | String | Tipo de cobertura vegetal | No vacío |
| **Tipo de Cultivo** | String | Tipo de cultivo presente | No vacío |
| **Disturbio** | String | Tipo de disturbio observado | No vacío |
| **Observaciones** | String | Notas adicionales | Opcional |

### Campos Automáticos

- **Latitud/Longitud**: Coordenadas GPS automáticas
- **Fecha**: Timestamp de creación
- **Editado**: Timestamp de última modificación

### Uso Recomendado

- Monitoreo de cambios en cobertura
- Estudios de sucesión vegetal
- Evaluación de impactos ambientales

---

## Formulario 4: Parcela de Vegetación

**Propósito**: Mediciones detalladas de vegetación en parcelas específicas.

### Campos Requeridos

| Campo | Tipo | Descripción | Validación |
|-------|------|-------------|------------|
| **Código** | String | Identificador único de la parcela | No vacío |
| **Clima** | String | Condiciones climáticas | No vacío |
| **Temporada** | String | Estación del año | No vacío |
| **Cuadrante A** | String | Coordenada del cuadrante A | No vacío |
| **Cuadrante B** | String | Coordenada del cuadrante B | No vacío |
| **Sub-cuadrante** | String | Identificador del sub-cuadrante | No vacío |
| **Hábito de Crecimiento** | String | Forma de crecimiento de la planta | No vacío |
| **Nombre Común** | String | Nombre común de la especie | No vacío |
| **Nombre Científico** | String | Nombre científico de la especie | No vacío |
| **Placa** | String | Identificador de la placa | No vacío |
| **Circunferencia** | String | Circunferencia del tronco | No vacío |
| **Distancia** | String | Distancia desde el centro | No vacío |
| **Estatura** | String | Estatura de la planta | No vacío |
| **Altura** | String | Altura de la copa | No vacío |
| **Observaciones** | String | Notas adicionales | Opcional |

### Campos Automáticos

- **Latitud/Longitud**: Coordenadas GPS automáticas
- **Fecha**: Timestamp de creación
- **Editado**: Timestamp de última modificación

### Uso Recomendado

- Inventarios forestales
- Estudios de estructura vegetal
- Monitoreo de crecimiento

---

## Formulario 5: Fauna Búsqueda Libre

**Propósito**: Observaciones de vida silvestre durante búsquedas libres sin transectos fijos.

### Campos Requeridos

| Campo | Tipo | Descripción | Validación |
|-------|------|-------------|------------|
| **Zona** | String | Identificador de la zona | No vacío |
| **Clima** | String | Condiciones climáticas | No vacío |
| **Temporada** | String | Estación del año | No vacío |
| **Tipo de Animal** | String | Categoría del animal | No vacío |
| **Nombre Común** | String | Nombre común de la especie | No vacío |
| **Nombre Científico** | String | Nombre científico de la especie | No vacío |
| **Número de Individuos** | String | Cantidad observada | No vacío |
| **Tipo de Observación** | String | Método de observación | No vacío |
| **Altura de Observación** | String | Altura desde donde se observa | No vacío |
| **Observaciones** | String | Notas adicionales | Opcional |

### Campos Automáticos

- **Latitud/Longitud**: Coordenadas GPS automáticas
- **Fecha**: Timestamp de creación
- **Editado**: Timestamp de última modificación

### Uso Recomendado

- Exploraciones de biodiversidad
- Registros casuales de especies
- Estudios de distribución

---

## Formulario 6: Cámaras Trampa

**Propósito**: Instalación y monitoreo de cámaras trampa para captura de vida silvestre.

### Campos Requeridos

| Campo | Tipo | Descripción | Validación |
|-------|------|-------------|------------|
| **Código** | String | Identificador único de la cámara | No vacío |
| **Clima** | String | Condiciones climáticas | No vacío |
| **Temporada** | String | Estación del año | No vacío |
| **Zona** | String | Identificador de la zona | No vacío |
| **Nombre de Cámara** | String | Nombre identificador de la cámara | No vacío |
| **Placa de Cámara** | String | Número de placa de la cámara | No vacío |
| **Placa de Guaya** | String | Número de placa de la guaya | No vacío |
| **Ancho del Camino** | String | Ancho del camino donde se instaló | No vacío |
| **Fecha de Instalación** | String | Fecha de instalación de la cámara | No vacío |
| **Distancia al Objetivo** | String | Distancia al objetivo de captura | No vacío |
| **Altura de la Lente** | String | Altura de la lente de la cámara | No vacío |
| **Checklist** | String | Lista de verificación de instalación | No vacío |
| **Observaciones** | String | Notas adicionales | Opcional |

### Campos Automáticos

- **Latitud/Longitud**: Coordenadas GPS automáticas
- **Fecha**: Timestamp de creación
- **Editado**: Timestamp de última modificación

### Uso Recomendado

- Monitoreo de mamíferos
- Estudios de comportamiento
- Inventarios de fauna nocturna

---

## Formulario 7: Variables Climáticas

**Propósito**: Registro de variables climáticas y ambientales.

### Campos Requeridos

| Campo | Tipo | Descripción | Validación |
|-------|------|-------------|------------|
| **Zona** | String | Identificador de la zona | No vacío |
| **Clima** | String | Condiciones climáticas generales | No vacío |
| **Temporada** | String | Estación del año | No vacío |
| **Pluviosidad** | String | Precipitación registrada | No vacío |
| **Temperatura Máxima** | String | Temperatura máxima del día | No vacío |
| **Temperatura Mínima** | String | Temperatura mínima del día | No vacío |
| **Humedad Relativa** | String | Humedad relativa del aire | No vacío |
| **Velocidad del Viento** | String | Velocidad del viento | No vacío |
| **Dirección del Viento** | String | Dirección predominante del viento | No vacío |
| **Presión Atmosférica** | String | Presión atmosférica | No vacío |
| **Observaciones** | String | Notas adicionales | Opcional |

### Campos Automáticos

- **Latitud/Longitud**: Coordenadas GPS automáticas
- **Fecha**: Timestamp de creación
- **Editado**: Timestamp de última modificación

### Uso Recomendado

- Monitoreo climático
- Estudios de microclima
- Correlación clima-biodiversidad

---

## 🔧 Funcionalidades Comunes

### Validación de Formularios

Todos los formularios incluyen validación automática:

- **Campos requeridos**: Se validan antes de guardar
- **Formato de datos**: Verificación de tipos de datos
- **Completitud**: Indicador visual de formularios completos/incompletos

### Geolocalización

- **Captura automática**: GPS se activa al abrir cualquier formulario
- **Precisión**: Coordenadas de alta precisión
- **Validación**: Verificación de que la ubicación sea válida

### Gestión de Datos

- **Guardado local**: Todos los datos se almacenan localmente
- **Sincronización**: Preparado para sincronización con servidor
- **Respaldo**: Datos respaldados automáticamente

### Interfaz de Usuario

- **Navegación intuitiva**: Fácil acceso a todos los formularios
- **Validación en tiempo real**: Feedback inmediato al usuario
- **Indicadores visuales**: Estado de completitud claramente visible

## 📱 Guía de Uso Rápido

1. **Seleccionar formulario**: Desde el menú principal
2. **Completar campos**: Llenar todos los campos requeridos
3. **Verificar ubicación**: Asegurar que GPS esté activo
4. **Guardar**: Los datos se guardan automáticamente
5. **Revisar**: Verificar completitud en el dashboard

---

**Próximo paso**: [Arquitectura Técnica](Arquitectura-y-Estructura)
