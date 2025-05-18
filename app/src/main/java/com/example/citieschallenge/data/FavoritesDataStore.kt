package com.example.citieschallenge.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "favorites_prefs"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

object FavoritesDataStore {
    private val FAVORITES_KEY = stringSetPreferencesKey("favorite_city_ids")

    fun getFavorites(context: Context): Flow<Set<Long>> {
        return context.dataStore.data.map { preferences ->
            preferences[FAVORITES_KEY]?.mapNotNull { it.toLongOrNull() }?.toSet() ?: emptySet()
        }
    }

    suspend fun saveFavorite(context: Context, id: Long) {
        context.dataStore.edit { preferences ->
            val current = preferences[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
            current.add(id.toString())
            preferences[FAVORITES_KEY] = current
        }
    }

    suspend fun removeFavorite(context: Context, id: Long) {
        context.dataStore.edit { preferences ->
            val current = preferences[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
            current.remove(id.toString())
            preferences[FAVORITES_KEY] = current
        }
    }
}