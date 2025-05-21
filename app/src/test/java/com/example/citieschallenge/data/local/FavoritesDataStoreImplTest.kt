package com.example.citieschallenge.data.local

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File

class FavoritesDataStoreImplSimpleTest {

    private lateinit var dataStore: androidx.datastore.core.DataStore<Preferences>
    private lateinit var favoritesDataStore: FavoritesDataStore

    @Before
    fun setup() {
        val file = File.createTempFile("test_prefs", ".preferences_pb").apply { deleteOnExit() }
        dataStore = PreferenceDataStoreFactory.create(
            produceFile = { file }
        )
        favoritesDataStore = FavoritesDataStore(dataStore)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `saveFavorite stores the city id`() = runTest {
        favoritesDataStore.saveFavorite(10L)

        val result = favoritesDataStore.favoritesFlow.first()
        assertTrue(result.contains(10L))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `removeFavorite deletes the city id`() = runTest {
        favoritesDataStore.saveFavorite(20L)
        favoritesDataStore.saveFavorite(30L)

        favoritesDataStore.removeFavorite(20L)

        val result = favoritesDataStore.favoritesFlow.first()
        assertFalse(result.contains(20L))
        assertTrue(result.contains(30L))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `favoritesFlow returns empty if no data`() = runTest {
        val result = favoritesDataStore.favoritesFlow.first()
        assertTrue(result.isEmpty())
    }
}
