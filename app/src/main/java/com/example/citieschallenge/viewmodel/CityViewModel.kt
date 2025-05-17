package com.example.citieschallenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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

    // Mapa para búsqueda rápida por ID
    private var cityMapById: Map<Long, City> = emptyMap()

    init {
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = CityRepository.loadCitiesFromAssets(getApplication())
                    .sortedBy { it.name.lowercase() } // orden alfabético por nombre
                _cities.value = result
                // Construir el mapa para acceso rápido por id
                cityMapById = result.associateBy { it.id }
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Obtener ciudad por ID usando el mapa para acceso O(1)
    fun getCityById(id: Long): City? {
        return cityMapById[id]
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Lista filtrada en tiempo real según el texto ingresado
    val filteredCities: StateFlow<List<City>> = combine(_searchQuery, _cities) { query, cities ->
        if (query.isBlank()) {
            cities
        } else {
            cities.filter {
                it.name.startsWith(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        _visibleCount.value = 50 // Reinicia el contador de visibles
    }

    private val _visibleCount = MutableStateFlow(50)
    val visibleCount: StateFlow<Int> = _visibleCount

    fun loadMore() {
        _visibleCount.update { current -> current + 50 }
    }

    private val _selectedCity = MutableStateFlow<City?>(null)
    val selectedCity: StateFlow<City?> = _selectedCity

    fun selectCity(city: City) {
        _selectedCity.value = city
    }
}
