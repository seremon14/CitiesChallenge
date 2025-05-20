package com.example.citieschallenge.data.repository

import com.example.citieschallenge.domain.model.City

interface CityRepository {
    suspend fun loadCities(): List<City>
}
