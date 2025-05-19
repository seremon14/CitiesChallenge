package com.example.citieschallenge.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.citieschallenge.navigation.Screen
import com.example.citieschallenge.ui.components.CityList
import com.example.citieschallenge.ui.components.CityListTopBar
import com.example.citieschallenge.ui.components.EmbeddedMap
import com.example.citieschallenge.ui.components.SearchBar
import com.example.citieschallenge.viewmodel.CityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(
    navController: NavController,
    viewModel: CityViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val filteredCities by remember(uiState.cities, uiState.searchQuery, uiState.showOnlyFavorites, uiState.favoriteCityIds) {
        derivedStateOf {
            var result = if (uiState.searchQuery.isBlank()) {
                uiState.cities
            } else {
                uiState.cities.filter {
                    it.name.startsWith(uiState.searchQuery, ignoreCase = true)
                }
            }

            if (uiState.showOnlyFavorites) {
                result = result.filter { it.id in uiState.favoriteCityIds }
            }

            result
        }
    }

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
                            cities = filteredCities.take(uiState.visibleCount),
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
                        uiState.selectedCity?.let {
                            EmbeddedMap(it.name, it.coordinate.lat, it.coordinate.lon)
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
                        cities = filteredCities.take(uiState.visibleCount),
                        favoriteCityIds = uiState.favoriteCityIds,
                        onClick = { city ->
                            viewModel.selectCity(city)
                            navController.navigate(
                                Screen.Map.createRoute(
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
