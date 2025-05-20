package com.example.citieschallenge.data.repository

import android.content.Context
import android.content.res.AssetManager
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class CityRepositoryImplTest {

    private lateinit var repository: CityRepositoryImpl

    private val mockContext = mockk<Context>(relaxed = true)
    private val mockAssets = mockk<AssetManager>(relaxed = true)

    @Before
    fun setup() {
        every { mockContext.assets } returns mockAssets
    }

    @Test
    fun `loadCities parses and sorts cities by name`() = runTest {
        val fakeJson = """
            [
                {"_id": 1, "name": "Zurich", "country": "Switzerland", "coord": {"lat": 47.37, "lon": 8.54}},
                {"_id": 2, "name": "Amsterdam", "country": "Netherlands", "coord": {"lat": 52.37, "lon": 4.89}},
                {"_id": 3, "name": "Berlin", "country": "Germany", "coord": {"lat": 52.52, "lon": 13.40}}
            ]
        """.trimIndent()

        val inputStream = ByteArrayInputStream(fakeJson.toByteArray())
        every { mockAssets.open("cities.json") } returns inputStream

        repository = CityRepositoryImpl(mockContext)

        val result = repository.loadCities()

        assertEquals(3, result.size)
        assertEquals("Amsterdam", result[0].name)
        assertEquals("Berlin", result[1].name)
        assertEquals("Zurich", result[2].name)
    }

    @Test
    fun `loadCities throws exception on malformed JSON`() = runTest {
        val invalidJson = """ { "invalid": [ } """
        val inputStream = ByteArrayInputStream(invalidJson.toByteArray())

        every { mockAssets.open("cities.json") } returns inputStream
        repository = CityRepositoryImpl(mockContext)

        assertFailsWith<SerializationException> {
            repository.loadCities()
        }
    }
}
