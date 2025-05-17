package com.example.citieschallenge.repository

import android.content.Context
import com.example.citieschallenge.model.City
import kotlinx.serialization.json.Json

object CityRepository {

    private var cityList: List<City>? = null

    suspend fun loadCitiesFromAssets(context: Context): List<City> {
        if (cityList != null) return cityList!!

        val json = context.assets.open("cities.json")
            .bufferedReader()
            .use { it.readText() }

        cityList = Json.decodeFromString(json)
        return cityList!!
    }
}