package com.example.citieschallenge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.citieschallenge.ui.screens.CityListScreen
import com.example.citieschallenge.ui.screens.MapScreen

sealed class Screen(val route: String) {
    object CityList : Screen("city_list")

    object Map : Screen("map/{cityName}/{lat}/{lon}") {
        fun createRoute(cityName: String, lat: Float, lon: Float) = "map/$cityName/$lat/$lon"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.CityList.route) {

        composable(Screen.CityList.route) {
            CityListScreen(navController = navController)
        }

        composable(
            route = Screen.Map.route,
            arguments = listOf(
                navArgument("cityName") { type = NavType.StringType },
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
            val lat = backStackEntry.arguments?.getFloat("lat") ?: 0f
            val lon = backStackEntry.arguments?.getFloat("lon") ?: 0f
            MapScreen(navController = navController, cityName = cityName, lat = lat, lon = lon)
        }
    }
}