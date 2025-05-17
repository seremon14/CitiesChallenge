package com.example.citieschallenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.citieschallenge.model.City
import com.example.citieschallenge.repository.CityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

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
            } finally {
                _isLoading.value = false
            }
        }
    }
}
