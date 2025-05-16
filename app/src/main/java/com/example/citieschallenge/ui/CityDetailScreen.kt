package com.example.citieschallenge.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun CityDetailScreen(navController: NavController) {
    val cityId = navController
        .currentBackStackEntryAsState()
        .value
        ?.arguments
        ?.getString("cityId")

    Text(text = "Detalles de la ciudad con ID: $cityId")
}
