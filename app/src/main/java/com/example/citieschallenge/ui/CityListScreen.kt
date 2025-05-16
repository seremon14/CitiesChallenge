package com.example.citieschallenge.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.citieschallenge.navigation.Screen

@Composable
fun CityListScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pantalla de Listado de Ciudades")

        Button(onClick = {
            navController.navigate(Screen.Map.route)
        }) {
            Text(text = "Ir al Mapa")
        }

        Button(onClick = {
            val dummyCityId = 123 // Puedes cambiarlo luego por el ID real
            navController.navigate(Screen.Detail.createRoute(dummyCityId))
        }) {
            Text(text = "Ver Detalles de una Ciudad")
        }
    }
}