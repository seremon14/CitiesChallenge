package com.example.citieschallenge.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.citieschallenge.navigation.Screen
import com.example.citieschallenge.viewmodel.CityViewModel

@Composable
fun CityDetailScreen(
    navController: NavHostController,
    cityId: Long,
    viewModel: CityViewModel = viewModel()
) {
    val city = viewModel.getCityById(cityId)

    city?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Ciudad: ${city.name}", style = MaterialTheme.typography.headlineMedium)
            Text("País: ${city.country}", style = MaterialTheme.typography.bodyLarge)
            Text("Latitud: ${city.coordinate.lat}", style = MaterialTheme.typography.bodyLarge)
            Text("Longitud: ${city.coordinate.lon}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(Screen.Map.createRoute(it.name, it.coordinate.lat, it.coordinate.lon))
            }) {
                Text("Ver en Mapa")
            }
        }
    } ?: Text("Ciudad no encontrada")
}
