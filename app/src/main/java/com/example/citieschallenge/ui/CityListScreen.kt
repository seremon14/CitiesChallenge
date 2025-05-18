package com.example.citieschallenge.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
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
    viewModel: CityViewModel = viewModel()
) {
    val cityList by viewModel.filteredCities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val visibleCount by viewModel.visibleCount.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val favoriteCityIds by viewModel.favoriteCityIds.collectAsState()
    val showOnlyFavorites by viewModel.showOnlyFavorites.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            CityListTopBar(
                isFilteringFavorites = showOnlyFavorites,
                onToggleFavorites = viewModel::toggleShowOnlyFavorites
            )
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
                            favoriteCityIds = favoriteCityIds,
                            onClick = { city -> viewModel.selectCity(city) },
                            onFavoriteToggle = { city -> viewModel.toggleFavorite(city.id) },
                            onMapClick = null,
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
                        favoriteCityIds = favoriteCityIds,
                        onClick = { city ->
                            navController.navigate(Screen.Detail.createRoute(city.id))
                        },
                        onFavoriteToggle = { city -> viewModel.toggleFavorite(city.id) },
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
