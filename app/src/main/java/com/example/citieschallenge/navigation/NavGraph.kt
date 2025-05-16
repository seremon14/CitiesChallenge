package com.example.citieschallenge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.citieschallenge.ui.CityDetailScreen
import com.example.citieschallenge.ui.CityListScreen
import com.example.citieschallenge.ui.MapScreen

sealed class Screen(val route: String) {
    object CityList : Screen("city_list")
    object Map : Screen("map")
    object Detail : Screen("detail/{cityId}") {
        fun createRoute(cityId: Int) = "detail/$cityId"
    }
}

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.CityList.route) {
        composable(Screen.CityList.route) {
            CityListScreen(navController)
        }
        composable(Screen.Map.route) {
            MapScreen(navController)
        }
        composable(Screen.Detail.route) {
            CityDetailScreen(navController)
        }
    }
}
