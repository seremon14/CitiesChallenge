package com.example.citieschallenge.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.citieschallenge.domain.model.City
import com.example.citieschallenge.domain.model.Coordinate
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CityListItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleCity = City(
        id = 1,
        name = "Zurich",
        country = "Switzerland",
        coordinate = Coordinate(47.3769f, 8.5417f)
    )

    @Test
    fun cityListItem_displaysCityNameAndCountry() {
        composeTestRule.setContent {
            CityListItem(
                city = sampleCity,
                isFavorite = false,
                onClick = {},
                onFavoriteToggle = {}
            )
        }

        composeTestRule.onNodeWithText("Zurich, Switzerland").assertIsDisplayed()
    }

    @Test
    fun cityListItem_displaysCoordinates() {
        composeTestRule.setContent {
            CityListItem(
                city = sampleCity,
                isFavorite = false,
                onClick = {},
                onFavoriteToggle = {}
            )
        }

        composeTestRule.onNodeWithText("Lat: 8.5417, Lon: 47.3769").assertIsDisplayed()
    }

    @Test
    fun cityListItem_favoriteIconCallsToggle() {
        var toggled = false

        composeTestRule.setContent {
            CityListItem(
                city = sampleCity,
                isFavorite = false,
                onClick = {},
                onFavoriteToggle = { toggled = true }
            )
        }

        composeTestRule.onNode(hasContentDescription("Agregar a favoritos")).performClick()

        assert(toggled)
    }

    @Test
    fun cityListItem_clickCallsOnClick() {
        var clicked = false

        composeTestRule.setContent {
            CityListItem(
                city = sampleCity,
                isFavorite = false,
                onClick = { clicked = true },
                onFavoriteToggle = {}
            )
        }

        composeTestRule.onNode(hasText("Zurich, Switzerland")).performClick()

        assert(clicked)
    }
}
