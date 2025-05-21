package com.example.citieschallenge.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.citieschallenge.data.local.IFavoritesDataStore
import com.example.citieschallenge.data.repository.CityRepository
import com.example.citieschallenge.domain.model.City
import com.example.citieschallenge.ui.screens.CityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val repository: CityRepository,
    private val favoritesDataStore: IFavoritesDataStore
) : ViewModel(), ICityViewModel {

    private val _uiState = MutableStateFlow(CityUiState())
    override val uiState: StateFlow<CityUiState> = _uiState.asStateFlow()

    private val _visibleCities = MutableStateFlow<List<City>>(emptyList())
    override val visibleCities: StateFlow<List<City>> = _visibleCities.asStateFlow()

    private val _filteredCities = MutableStateFlow<List<City>>(emptyList())
    override val filteredCities: StateFlow<List<City>> = _filteredCities.asStateFlow()

    private var citiesLoaded = false


    init {
        loadCities()

        viewModelScope.launch {
            favoritesDataStore.favoritesFlow.collect { favorites ->
                _uiState.update { it.copy(favoriteCityIds = favorites) }
                updateFilteredCities()
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun loadCities() {
        if (citiesLoaded) return

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val result = repository.loadCities()

                _uiState.update { it.copy(cities = result) }
                citiesLoaded = true
                updateFilteredCities()
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    override fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(searchQuery = query, visibleCount = 50)
        }
        updateFilteredCities()
    }

    override fun toggleShowOnlyFavorites() {
        _uiState.update {
            it.copy(showOnlyFavorites = !it.showOnlyFavorites)
        }
        updateFilteredCities()
    }

    override fun loadMore() {
        _uiState.update { it.copy(visibleCount = it.visibleCount + 50) }
        updateFilteredCities()
    }

    override fun selectCity(city: City) {
        _uiState.update { it.copy(selectedCity = city) }
    }

    override fun toggleFavorite(cityId: Long) {
        viewModelScope.launch {
            val isFavorite = cityId in uiState.value.favoriteCityIds
            if (isFavorite) {
                favoritesDataStore.removeFavorite(cityId)
            } else {
                favoritesDataStore.saveFavorite(cityId)
            }
        }
    }

    private fun updateFilteredCities() {
        val state = _uiState.value

        var result = if (state.searchQuery.isBlank()) {
            state.cities
        } else {
            state.cities.filter {
                it.name.startsWith(state.searchQuery, ignoreCase = true)
            }
        }

        if (state.showOnlyFavorites) {
            result = result.filter { it.id in state.favoriteCityIds }
        }

        _filteredCities.value = result
        _visibleCities.value = result.take(state.visibleCount)
    }
}
