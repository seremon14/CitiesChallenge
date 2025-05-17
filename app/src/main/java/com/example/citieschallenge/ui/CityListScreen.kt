package com.example.citieschallenge.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.citieschallenge.navigation.Screen
import com.example.citieschallenge.viewmodel.CityViewModel

@Composable
fun CityListScreen(
    navController: NavController,
    viewModel: CityViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val cityList by viewModel.cities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn {
            items(
                items = cityList.take(50),
                key = { city -> city.id }
            ) { city ->
                ListItem(
                    headlineContent = { Text("${city.name}, ${city.country}") },
                    supportingContent = {
                        Text("Lat: ${city.coordinate.lat}, Lon: ${city.coordinate.lon}")
                    },
                    trailingContent = {
                        IconButton(onClick = {
                            navController.navigate(Screen.Map.createRoute(city.coordinate.lat, city.coordinate.lon))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Ver en mapa"
                            )
                        }
                    },
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Detail.createRoute(city.id))
                    }
                )
                HorizontalDivider()
            }
        }
    }
}
