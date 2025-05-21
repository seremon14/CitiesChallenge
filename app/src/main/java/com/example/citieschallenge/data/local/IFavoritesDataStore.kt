package com.example.citieschallenge.data.local

import kotlinx.coroutines.flow.Flow

interface IFavoritesDataStore {
    val favoritesFlow: Flow<Set<Long>>
    suspend fun saveFavorite(cityId: Long)
    suspend fun removeFavorite(cityId: Long)
}
