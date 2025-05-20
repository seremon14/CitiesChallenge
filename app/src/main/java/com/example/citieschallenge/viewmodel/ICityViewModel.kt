package com.example.citieschallenge.viewmodel

import com.example.citieschallenge.domain.model.City
import com.example.citieschallenge.ui.screens.CityUiState
import kotlinx.coroutines.flow.StateFlow

interface ICityViewModel {
    val uiState: StateFlow<CityUiState>
    val visibleCities: StateFlow<List<City>>
    val filteredCities: StateFlow<List<City>>

    fun toggleShowOnlyFavorites()
    fun updateSearchQuery(query: String)
    fun toggleFavorite(cityId: Long)
    fun selectCity(city: City)
    fun loadMore()
}
