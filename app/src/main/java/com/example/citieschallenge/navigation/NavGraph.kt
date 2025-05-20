package com.example.citieschallenge.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.citieschallenge.ui.screens.CityListScreen
import com.example.citieschallenge.ui.screens.MapScreen
import com.example.citieschallenge.viewmodel.CityViewModel

sealed class Screen(val route: String) {
    object CityList : Screen("city_list")

    object Map : Screen("map/{cityId}/{cityName}/{lat}/{lon}") {
        fun createRoute(cityId: Long, cityName: String, lat: Float, lon: Float) = "map/$cityId/$cityName/$lat/$lon"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.CityList.route) {

        composable(Screen.CityList.route) { backStackEntry ->
            // ViewModel se inicializa aquí
            val viewModel: CityViewModel = hiltViewModel(backStackEntry)
            CityListScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = Screen.Map.route,
            arguments = listOf(
                navArgument("cityId") { type = NavType.LongType },
                navArgument("cityName") { type = NavType.StringType },
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            // Recuperar argumentos
            val cityId = backStackEntry.arguments?.getLong("cityId") ?: 0L
            val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
            val lat = backStackEntry.arguments?.getFloat("lat") ?: 0f
            val lon = backStackEntry.arguments?.getFloat("lon") ?: 0f

            // Obtener el BackStackEntry del screen padre
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.CityList.route)
            }

            // Reusar el mismo ViewModel que en CityList
            val viewModel: CityViewModel = hiltViewModel(parentEntry)

            MapScreen(
                navController = navController,
                cityId = cityId,
                cityName = cityName,
                lat = lat,
                lon = lon,
                viewModel = viewModel
            )
        }
    }
}
