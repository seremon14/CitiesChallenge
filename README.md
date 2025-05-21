# 🏙️ Ualá Cities Challenge - Android App

Este proyecto es la solución al **Mobile Challenge de Ualá**, cuyo objetivo es demostrar habilidades de diseño de arquitectura, calidad de código, rendimiento, buenas prácticas de desarrollo móvil y experiencia de usuario con Jetpack Compose.

---

## 📱 Descripción general

La aplicación permite visualizar y explorar una lista de más de **200,000 ciudades** (desde un archivo JSON), filtrarlas por nombre, marcarlas como favoritas y ver sus coordenadas en un mapa. Cuenta con una interfaz adaptable a orientación **portrait** y **landscape**, navegación fluida y persistencia de favoritos entre sesiones.

---

## 🎯 Requisitos cumplidos

✔️ Filtro de ciudades por prefijo, con búsqueda eficiente  
✔️ Lista ordenada alfabéticamente (ciudad, país)  
✔️ Soporte para favoritos persistentes  
✔️ Visualización en mapa con botón de información adicional  
✔️ UI dinámica según orientación del dispositivo  
✔️ Tests unitarios de ViewModel, repositorios y DataStore  
✔️ Tests de UI con Jetpack Compose  
✔️ Navegación entre pantallas con parámetros  
✔️ Arquitectura moderna, inyección de dependencias y buenas prácticas

---

## 🧠 Arquitectura del proyecto

El proyecto fue desarrollado usando principios de **Clean Architecture**, con una clara separación de responsabilidades:

```
com.example.citieschallenge
│
├── data                 // Implementaciones de repositorios y DataStore
│   ├── local            // Implementación de persistencia (DataStore)
│   └── repository       // Carga de datos desde assets
│
├── domain
│   └── model            // Modelos como City y Coordinate
│
├── viewmodel            // CityViewModel e interfaces para testing
│
├── ui
│   ├── components       // SearchBar, CityListItem, etc.
│   └── screens          // CityListScreen, MapScreen
│
├── navigation           // NavGraph con rutas y parámetros
└── di                   // Inyección de dependencias con Hilt
```

---

## 📦 Modelos

### City

```kotlin
@Serializable
data class City(
    @SerialName("_id")
    val id: Long,
    val name: String,
    val country: String,
    @SerialName("coord")
    val coordinate: Coordinate,
    val isFavorite: Boolean = false
)
```

### Coordinate

```kotlin
@Serializable
data class Coordinate(
    val lon: Float,
    val lat: Float
)
```

Se usó `kotlinx.serialization` para parsear el archivo `cities.json` y `@SerialName("_id")` para mapear correctamente el campo proveniente del JSON.

---

## 🔄 Persistencia con DataStore

```kotlin
interface IFavoritesDataStore {
    val favoritesFlow: Flow<Set<Long>>
    suspend fun saveFavorite(cityId: Long)
    suspend fun removeFavorite(cityId: Long)
}
```

La implementación utiliza `DataStore<Preferences>` para almacenar un `Set<String>` con los IDs de ciudades favoritas. Esta abstracción permite testear el ViewModel sin depender de la implementación concreta.

---

## 🗺️ Carga de ciudades

```kotlin
class CityRepositoryImpl @Inject constructor(...) : ICityRepository {
    override suspend fun loadCities(): List<City>
}
```

- Lee `cities.json` una sola vez por ejecución.
- Usa `kotlinx.serialization` con `ignoreUnknownKeys = true`.
- Ordena las ciudades por nombre para facilitar la búsqueda.

---

## 🧠 ViewModel

El `CityViewModel` gestiona toda la lógica de presentación:

- Filtrado por prefijo (con búsqueda en tiempo real)
- Visibilidad de favoritos
- Control de paginación (visibleCount)
- Ciudad seleccionada
- Eventos para navegación y mostrar detalles

Usa `StateFlow` y `derivedStateOf` para mantener estados reactivos y eficientes.

---

## 🧪 Pruebas

### ✅ Unitarias

- `CityViewModelTest`: cobertura completa de la lógica de búsqueda, favoritos, visibilidad y selección.
- `CityRepositoryImplTest`: prueba que no recargue el archivo y maneje errores de parsing.
- `FavoritesDataStoreImplTest`: test de persistencia usando `DataStoreFactory`.

### ✅ UI Tests

- `SearchBarTest`: cambia texto y limpia búsqueda.
- `CityListItemTest`: muestra información y responde a favoritos.
- `CityListScreenTest`: test de interacción con navegación (mockeada).

---

## 🚀 Decisiones técnicas destacadas

| Tema | Decisión tomada |
|------|------------------|
| Arquitectura | Separación por capas + ViewModel con dependencias por interfaz |
| Persistencia | DataStore + Flow para favoritos persistentes |
| Desempeño | Carga inicial más lenta, pero búsqueda instantánea |
| Tests | Test de unidad + UI (con Jetpack Compose Test) |
| UI adaptativa | Uso de `LocalConfiguration` para soporte landscape |
| Navegación | NavController con rutas parametrizadas y tipo seguro |
| Mapeo JSON | Uso de kotlinx.serialization con @SerialName |

---

## 🛠️ Tecnologías utilizadas

- Kotlin + Jetpack Compose
- Kotlinx Serialization
- Android DataStore
- Hilt (Inyección de dependencias)
- Navigation Compose
- Coroutines + StateFlow
- JUnit + MockK + Compose UI Test

---

## 📂 Cómo ejecutar el proyecto

1. Clonar el repositorio
2. Abrir en Android Studio (Arctic Fox o superior)
3. Ejecutar la app en un emulador o dispositivo
4. Ejecutar pruebas desde el panel de **Test** o con `./gradlew test`

---

## 📌 Pendientes / mejoras posibles

- Reemplazar el archivo JSON por una API real
- Mostrar alerta de error si el archivo falla al cargar
- Agregar paginación por scroll para listas muy largas

---

## 🙌 Autor

Sebastián Rendón Montoya  
[GitHub](https://github.com/seremon14) • [LinkedIn](https://www.linkedin.com/in/sebastian-rendon/) • [Website](https://seremon14.github.io/)