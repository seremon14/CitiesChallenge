package com.example.citieschallenge.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import com.example.citieschallenge.domain.model.City
import com.example.citieschallenge.domain.model.Coordinate
import com.example.citieschallenge.viewmodel.ICityViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Rule
import org.junit.Test

class CityListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    class FakeCityViewModel : ICityViewModel {
        override val uiState = MutableStateFlow(CityUiState())
        override val visibleCities = MutableStateFlow<List<City>>(emptyList())
        override val filteredCities = MutableStateFlow<List<City>>(emptyList())

        override fun toggleShowOnlyFavorites() {}
        override fun updateSearchQuery(query: String) {}
        override fun toggleFavorite(cityId: Long) {}
        override fun selectCity(city: City) {}
        override fun loadMore() { }
        fun setLoading(bool: Boolean) {uiState.update { it.copy(isLoading = bool) }}
        fun setCities(cities: List<City>) {
            uiState.update { it.copy(cities = cities)}
            filteredCities.value = cities
            visibleCities.value = cities
        }
    }

    @Test
    fun cityListScreen_showsLoadingIndicator_whenLoading() {
        val viewModel = FakeCityViewModel().apply {
            setLoading(true)
        }

        composeTestRule.setContent {
            CityListScreen(
                navController = rememberNavController(),
                viewModel = viewModel
            )
        }

        composeTestRule.onNode(hasTestTag("CircularProgressIndicator"))
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun cityListScreen_showsCityList_whenLoaded() {
        val viewModel = FakeCityViewModel().apply {
            setLoading(false)
            setCities(
                listOf(
                    City(1, "Bogotá", "Colombia", Coordinate(4.7110f, -74.0721f)),
                    City(2, "Madrid", "España", Coordinate(40.4168f, -3.7038f))
                )
            )
        }

        composeTestRule.setContent {
            CityListScreen(
                navController = rememberNavController(),
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText("Bogotá, Colombia").assertIsDisplayed()
        composeTestRule.onNodeWithText("Madrid, España").assertIsDisplayed()
    }
}
