package com.example.citieschallenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.citieschallenge.data.FavoritesDataStore
import com.example.citieschallenge.model.City
import com.example.citieschallenge.repository.CityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var cityMapById: Map<Long, City> = emptyMap()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _visibleCount = MutableStateFlow(50)
    val visibleCount: StateFlow<Int> = _visibleCount

    private val _selectedCity = MutableStateFlow<City?>(null)
    val selectedCity: StateFlow<City?> = _selectedCity

    private val _favoriteCityIds = MutableStateFlow<Set<Long>>(emptySet())
    val favoriteCityIds: StateFlow<Set<Long>> = _favoriteCityIds

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites

    init {
        loadCities()

        viewModelScope.launch {
            FavoritesDataStore.getFavorites(getApplication()).collect { favorites ->
                _favoriteCityIds.value = favorites
            }
        }
    }

    private fun loadCities() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = CityRepository.loadCitiesFromAssets(getApplication())
                    .sortedBy { it.name.lowercase() }
                _cities.value = result
                cityMapById = result.associateBy { it.id }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCityById(id: Long): City? {
        return cityMapById[id]
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        _visibleCount.value = 50
    }

    fun loadMore() {
        _visibleCount.update { it + 50 }
    }

    fun selectCity(city: City) {
        _selectedCity.value = city
    }

    fun toggleFavorite(cityId: Long) {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            if (cityId in _favoriteCityIds.value) {
                FavoritesDataStore.removeFavorite(context, cityId)
            } else {
                FavoritesDataStore.saveFavorite(context, cityId)
            }
        }
    }

    fun toggleShowOnlyFavorites() {
        _showOnlyFavorites.value = !_showOnlyFavorites.value
    }

    val filteredCities: StateFlow<List<City>> = combine(
        _searchQuery, _cities, _favoriteCityIds, _showOnlyFavorites
    ) { query, allCities, favorites, showOnlyFavs ->

        var result = if (query.isBlank()) {
            allCities
        } else {
            allCities.filter {
                it.name.startsWith(query, ignoreCase = true)
            }
        }

        if (showOnlyFavs) {
            result = result.filter { it.id in favorites }
        }

        result

    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
