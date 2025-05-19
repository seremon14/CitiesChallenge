package com.example.citieschallenge.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "favorites")

class FavoritesDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FavoritesDataStore {

    companion object {
        private val FAVORITES_KEY = stringSetPreferencesKey("favorite_city_ids")
    }

    override val favoritesFlow: Flow<Set<Long>> =
        context.dataStore.data.map { prefs ->
            prefs[FAVORITES_KEY]?.mapNotNull { it.toLongOrNull() }?.toSet() ?: emptySet()
        }

    override suspend fun saveFavorite(cityId: Long) {
        context.dataStore.edit { preferences ->
            val current = preferences[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
            current.add(cityId.toString())
            preferences[FAVORITES_KEY] = current
        }
    }

    override suspend fun removeFavorite(cityId: Long) {
        context.dataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
            current.remove(cityId.toString())
            prefs[FAVORITES_KEY] = current
        }
    }
}