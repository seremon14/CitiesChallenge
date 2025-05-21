package com.example.citieschallenge.viewmodel

import com.example.citieschallenge.data.local.IFavoritesDataStore
import com.example.citieschallenge.data.repository.ICityRepository
import com.example.citieschallenge.domain.model.City
import com.example.citieschallenge.domain.model.Coordinate
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CityViewModelTest {

    private lateinit var viewModel: CityViewModel

    @MockK
    private lateinit var repository: ICityRepository

    @MockK
    private lateinit var favoritesDataStore: IFavoritesDataStore

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(UnconfinedTestDispatcher())

        val cities = listOf(
            City(id = 1L, name = "Medellín", country = "Colombia", coordinate = Coordinate(lat = 0.0f, lon = 0.0f)),
            City(id = 2L, name = "Bogotá", country = "Colombia", coordinate = Coordinate(lat = 0.0f, lon = 0.0f))
        )
        every { favoritesDataStore.favoritesFlow } returns MutableStateFlow(emptySet())
        coEvery { repository.loadCities() } returns cities


        viewModel = CityViewModel(repository, favoritesDataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateSearchQuery updates searchQuery and resets visibleCount`() = runTest {
        // Given
        val query = "Medellín"

        // When
        viewModel.updateSearchQuery(query)

        // Then
        val state = viewModel.uiState.value
        assertEquals(query, state.searchQuery)
        assertEquals(50, state.visibleCount)
    }

    @Test
    fun `updateSearchQuery with partial text matches multiple cities`() = runTest {
        // Given
        val cities = listOf(
            City(id = 1L, name = "Medellín", country = "CO", coordinate = Coordinate(0f, 0f)),
            City(id = 2L, name = "Melbourne", country = "AU", coordinate = Coordinate(0f, 0f)),
            City(id = 3L, name = "Bogotá", country = "CO", coordinate = Coordinate(0f, 0f))
        )
        coEvery { repository.loadCities() } returns cities
        every { favoritesDataStore.favoritesFlow } returns MutableStateFlow(emptySet())

        viewModel = CityViewModel(repository, favoritesDataStore)
        advanceUntilIdle()

        // When
        viewModel.updateSearchQuery("me")

        // Then
        val resultNames = viewModel.filteredCities.value.map { it.name }

        assertEquals(listOf("Medellín", "Melbourne"), resultNames)
    }


    @Test
    fun `loadMore increases visibleCount by 50`() = runTest {
        // Given
        val initial = viewModel.uiState.value.visibleCount

        // When
        viewModel.loadMore()

        // Then
        val updated = viewModel.uiState.value.visibleCount
        assertEquals(initial + 50, updated)
    }

    @Test
    fun `selectCity updates selectedCity in uiState`() = runTest {
        // Given
        val city = City(id = 1L, name = "Medellín", country = "Colombia", coordinate = Coordinate(lat = 0.0f, lon = 0.0f))

        // When
        viewModel.selectCity(city)

        // Then
        val selected = viewModel.uiState.value.selectedCity
        assertEquals(city, selected)
    }

    @Test
    fun `toggleShowOnlyFavorites toggles the flag correctly`() = runTest {
        // Given
        val initial = viewModel.uiState.value.showOnlyFavorites

        val cityId = 1L
        val favoritesFlow = MutableStateFlow(setOf(cityId)) // Incluye cityId como favorito

        every { favoritesDataStore.favoritesFlow } returns favoritesFlow
        viewModel = CityViewModel(repository, favoritesDataStore)

        // When
        viewModel.toggleShowOnlyFavorites()

        // Then
        val updated = viewModel.uiState.value.showOnlyFavorites
        assertEquals(!initial, updated)
    }

    @Test
    fun `toggleFavorite adds city to favorites when it's not already a favorite`() = runTest {
        // Given
        val cityId = 1L
        every { favoritesDataStore.favoritesFlow } returns MutableStateFlow(emptySet())
        coEvery { favoritesDataStore.saveFavorite(cityId) } just Runs

        // When
        viewModel.toggleFavorite(cityId)

        // Then
        coVerify { favoritesDataStore.saveFavorite(cityId) }
    }

    @Test
    fun `toggleFavorite removes city from favorites when it's already a favorite`() = runTest {
        val cityId = 1L
        val favoritesFlow = MutableStateFlow(setOf(cityId)) // Incluye cityId como favorito

        every { favoritesDataStore.favoritesFlow } returns favoritesFlow
        coEvery { repository.loadCities() } returns listOf(mockk<City>())
        coEvery { favoritesDataStore.removeFavorite(cityId) } just Runs

        // Crear ViewModel con el flujo de favoritos que ya contiene el cityId
        viewModel = CityViewModel(repository, favoritesDataStore)

        advanceUntilIdle() // Espera a que se recoja el valor inicial del flow

        viewModel.toggleFavorite(cityId)

        // Verificamos que se haya llamado removeFavorite
        coVerify { favoritesDataStore.removeFavorite(cityId) }
    }

    @Test
    fun `loadCities updates uiState with cities and handles loading state`() = runTest {
        // Given
        val cities = listOf(
            City(id = 1L, name = "Bogotá", country = "Colombia", coordinate = Coordinate(lat = 0.0f, lon = 0.0f)),
            City(id = 2L, name = "Medellín", country = "Colombia", coordinate = Coordinate(lat = 0.0f, lon = 0.0f))
        )
        every { favoritesDataStore.favoritesFlow } returns MutableStateFlow(emptySet())
        coEvery { repository.loadCities() } returns cities

        viewModel = CityViewModel(repository, favoritesDataStore)

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(cities.sortedBy { it.name.lowercase() }, state.cities)
    }

    @Test
    fun `updateSearchQuery with no match results in empty filteredCities`() = runTest {
        // Given
        val query = "NoExiste"

        // When
        viewModel.updateSearchQuery(query)

        // Then
        val state = viewModel.uiState.value
        assertEquals(query, state.searchQuery)
        assertEquals(0, viewModel.filteredCities.value.size)
    }

    @Test
    fun `loadCities is only called once`() = runTest {
        // When
        viewModel.loadCities()
        viewModel.loadCities() // llamada extra que no debería tener efecto

        // Then
        coVerify(exactly = 1) { repository.loadCities() }
    }

    @Test
    fun `filteredCities contains only favorites when filter is active`() = runTest {
        // Given
        val cityId = 1L
        val favoritesFlow = MutableStateFlow(setOf(cityId))
        every { favoritesDataStore.favoritesFlow } returns favoritesFlow

        viewModel = CityViewModel(repository, favoritesDataStore)
        advanceUntilIdle()

        // When
        viewModel.toggleShowOnlyFavorites()

        // Then
        assertEquals(1, viewModel.filteredCities.value.size)
        assertEquals(cityId, viewModel.filteredCities.value.first().id)
    }
}
