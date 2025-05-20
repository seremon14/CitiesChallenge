package com.example.citieschallenge.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.citieschallenge.navigation.Screen
import com.example.citieschallenge.ui.components.CityDetailDialog
import com.example.citieschallenge.ui.components.CityList
import com.example.citieschallenge.ui.components.CityListTopBar
import com.example.citieschallenge.ui.components.EmbeddedMap
import com.example.citieschallenge.ui.components.SearchBar
import com.example.citieschallenge.viewmodel.CityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(
    navController: NavController,
    viewModel: CityViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val showCityDetailDialog = remember { mutableStateOf(false) }
    val visibleCities by viewModel.visibleCities.collectAsState()
    val filteredCities by viewModel.filteredCities.collectAsState()

    Scaffold(
        topBar = {
            if (!isLandscape) {
                CityListTopBar(
                    isFilteringFavorites = uiState.showOnlyFavorites,
                    onToggleFavorites = viewModel::toggleShowOnlyFavorites
                )
            }
        }
    ) { paddingValues ->
    if (uiState.isLoading) {
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
                            .weight(0.8f)
                            .fillMaxHeight()
                    ) {
                        CityListTopBar(
                            isFilteringFavorites = uiState.showOnlyFavorites,
                            onToggleFavorites = viewModel::toggleShowOnlyFavorites
                        )

                        SearchBar(uiState.searchQuery, onQueryChange = viewModel::updateSearchQuery)

                        CityList(
                            cities = visibleCities,
                            favoriteCityIds = uiState.favoriteCityIds,
                            onClick = { viewModel.selectCity(it) },
                            onFavoriteToggle = { viewModel.toggleFavorite(it.id) },
                            onEndReached = {
                                if (uiState.visibleCount < filteredCities.size) viewModel.loadMore()
                            }
                        )
                    }

                    // Mapa
                    Box(
                        modifier = Modifier
                            .weight(1.2f)
                            .fillMaxHeight()
                    ) {
                        uiState.selectedCity?.let { city ->
                            EmbeddedMap(city.name, city.coordinate.lat, city.coordinate.lon)

                            // Botón flotante
                            FloatingActionButton(
                                onClick = { showCityDetailDialog.value = true },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp),
                                containerColor = MaterialTheme.colorScheme.onSecondary
                            ) {
                                Icon(Icons.Default.Info, contentDescription = "More info")
                            }

                            // Mostrar el diálogo si es necesario
                            if (showCityDetailDialog.value) {
                                CityDetailDialog(city = city, onDismiss = { showCityDetailDialog.value = false })
                            }

                        } ?: Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            EmbeddedMap("Medellín", 6.142551f, -75.620789f)
                            Text(
                                "Selecciona una ciudad",
                                modifier = Modifier.align(Alignment.Center)
                            )
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
                    SearchBar(uiState.searchQuery, onQueryChange = viewModel::updateSearchQuery)
                    CityList(
                        cities = visibleCities,
                        favoriteCityIds = uiState.favoriteCityIds,
                        onClick = { city ->
                            viewModel.selectCity(city)
                            navController.navigate(
                                Screen.Map.createRoute(
                                    city.id,
                                    city.name,
                                    city.coordinate.lat,
                                    city.coordinate.lon
                                )
                            )
                        },
                        onFavoriteToggle = { viewModel.toggleFavorite(it.id) },
                        onEndReached = {
                            if (uiState.visibleCount < filteredCities.size) viewModel.loadMore()
                        }
                    )
                }
            }
        }
    }
}
