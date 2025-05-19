package com.example.citieschallenge.ui.screens

import com.example.citieschallenge.domain.model.City

data class CityUiState(
    val cities: List<City> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val visibleCount: Int = 50,
    val selectedCity: City? = null,
    val favoriteCityIds: Set<Long> = emptySet(),
    val showOnlyFavorites: Boolean = false
)
