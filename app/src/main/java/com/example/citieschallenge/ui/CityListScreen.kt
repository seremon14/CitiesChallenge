package com.example.citieschallenge.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.citieschallenge.model.City
import com.example.citieschallenge.navigation.Screen
import com.example.citieschallenge.viewmodel.CityViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(
    navController: NavController,
    viewModel: CityViewModel = viewModel()
) {
    val cityList by viewModel.filteredCities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val visibleCount by viewModel.visibleCount.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ciudades") })
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Lista de ciudades
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        SearchBar(searchQuery, onQueryChange = viewModel::updateSearchQuery)
                        CityList(
                            cities = cityList.take(visibleCount),
                            onClick = { city ->
                                viewModel.selectCity(city)
                            },
                            onEndReached = {
                                if (visibleCount < cityList.size) viewModel.loadMore()
                            }
                        )
                    }

                    // Mapa
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        selectedCity?.let {
                            EmbeddedMap(it.name, it.coordinate.lat, it.coordinate.lon)
                        } ?: Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Selecciona una ciudad")
                        }
                    }
                }
            } else {
                // Modo Portrait
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    SearchBar(searchQuery, onQueryChange = viewModel::updateSearchQuery)
                    CityList(
                        cities = cityList.take(visibleCount),
                        onClick = { city ->
                            navController.navigate(Screen.Detail.createRoute(city.id))
                        },
                        onMapClick = { city ->
                            navController.navigate(
                                Screen.Map.createRoute(
                                    city.name,
                                    city.coordinate.lat,
                                    city.coordinate.lon
                                )
                            )
                        },
                        onEndReached = {
                            if (visibleCount < cityList.size) viewModel.loadMore()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Buscar ciudad") },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun CityList(
    cities: List<City>,
    onClick: (City) -> Unit,
    onMapClick: ((City) -> Unit)? = null,
    onEndReached: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(cities, key = { _, city -> city.id }) { index, city ->
            ListItem(
                headlineContent = { Text("${city.name}, ${city.country}") },
                supportingContent = {
                    Text("Lat: ${city.coordinate.lat}, Lon: ${city.coordinate.lon}")
                },
                trailingContent = {
                    onMapClick?.let {
                        IconButton(onClick = { it(city) }) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Ver en mapa"
                            )
                        }
                    }
                },
                modifier = Modifier.clickable { onClick(city) }
            )
            HorizontalDivider()

            if (index == cities.lastIndex) {
                LaunchedEffect(Unit) { onEndReached() }
            }
        }
    }
}

@Composable
fun EmbeddedMap(cityName: String, lat: Float, lon: Float) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(lat, lon) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(lat.toDouble(), lon.toDouble()),
                12f
            ),
            durationMs = 1000
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = LatLng(lat.toDouble(), lon.toDouble())),
            title = cityName
        )
    }
}

