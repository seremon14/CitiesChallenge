package com.example.citieschallenge.data.repository

import android.content.Context
import com.example.citieschallenge.domain.model.City
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.sortedBy

@Singleton
class CityRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : CityRepository {

    private var cityList: List<City>? = null

    override suspend fun loadCities(): List<City> = withContext(Dispatchers.IO) {
        cityList?.let { return@withContext it }

        val json = context.assets.open("cities.json")
            .bufferedReader()
            .use { it.readText() }

        cityList = parseCities(json).sortedBy { it.name.lowercase() }
        return@withContext cityList!!
    }

    private fun parseCities(json: String): List<City> {
        return Json.decodeFromString(json)
    }
}