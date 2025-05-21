package com.example.citieschallenge.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : IFavoritesDataStore {

    companion object {
        private val FAVORITES_KEY = stringSetPreferencesKey("favorite_city_ids")
    }

    override val favoritesFlow: Flow<Set<Long>> =
        dataStore.data.map { prefs ->
            prefs[FAVORITES_KEY]?.mapNotNull { it.toLongOrNull() }?.toSet() ?: emptySet()
        }

    override suspend fun saveFavorite(cityId: Long) {
        dataStore.edit { preferences ->
            val current = preferences[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
            current.add(cityId.toString())
            preferences[FAVORITES_KEY] = current
        }
    }

    override suspend fun removeFavorite(cityId: Long) {
        dataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
            current.remove(cityId.toString())
            prefs[FAVORITES_KEY] = current
        }
    }
}
