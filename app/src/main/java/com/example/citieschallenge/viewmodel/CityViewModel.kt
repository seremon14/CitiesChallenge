package com.example.citieschallenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.citieschallenge.data.local.FavoritesDataStore
import com.example.citieschallenge.data.repository.CityRepository
import com.example.citieschallenge.domain.model.City
import com.example.citieschallenge.ui.screens.CityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val repository: CityRepository,
    private val favoritesDataStore: FavoritesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(CityUiState())
    val uiState: StateFlow<CityUiState> = _uiState.asStateFlow()

    private var cityMapById: Map<Long, City> = emptyMap()

    init {
        loadCities()

        viewModelScope.launch {
            favoritesDataStore.favoritesFlow.collect { favorites ->
                _uiState.update { it.copy(favoriteCityIds = favorites) }
            }
        }
    }

    private fun loadCities() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val result = repository.loadCities()
                    .sortedBy { it.name.lowercase() }

                _uiState.update {
                    it.copy(cities = result)
                }
            } finally {
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }


    fun getCityById(id: Long): City? = cityMapById[id]

    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(searchQuery = query, visibleCount = 50)
        }
    }

    fun loadMore() {
        _uiState.update { it.copy(visibleCount = it.visibleCount + 50) }
    }

    fun selectCity(city: City) {
        _uiState.update { it.copy(selectedCity = city) }
    }

    fun toggleFavorite(cityId: Long) {
        viewModelScope.launch {
            val isFavorite = cityId in uiState.value.favoriteCityIds
            if (isFavorite) {
                favoritesDataStore.removeFavorite(cityId)
            } else {
                favoritesDataStore.saveFavorite(cityId)
            }
        }
    }

    fun toggleShowOnlyFavorites() {
        _uiState.update { it.copy(showOnlyFavorites = !it.showOnlyFavorites) }
    }
}