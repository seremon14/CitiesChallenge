package com.example.citieschallenge.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchBar_showsInitialQuery() {
        val initialQuery = "Madrid"

        composeTestRule.setContent {
            SearchBar(query = initialQuery, onQueryChange = {})
        }

        composeTestRule.onNodeWithText(initialQuery).assertIsDisplayed()
    }

    @Test
    fun searchBar_trailingIconAppearsWhenTextIsNotEmpty() {
        var text = "Test"
        composeTestRule.setContent {
            SearchBar(query = text, onQueryChange = { text = it })
        }

        composeTestRule.onNodeWithContentDescription("Limpiar búsqueda").assertIsDisplayed()
    }

    @Test
    fun searchBar_trailingIconClearsText() {
        var text = "Test"
        composeTestRule.setContent {
            SearchBar(query = text, onQueryChange = { text = it })
        }

        composeTestRule.onNodeWithText("Test").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Limpiar búsqueda").performClick()
    }

    @Test
    fun searchBar_trailingIconDoesNotAppearWhenTextIsEmpty() {
        composeTestRule.setContent {
            SearchBar(query = "", onQueryChange = {})
        }

        composeTestRule.onNodeWithContentDescription("Limpiar búsqueda").assertDoesNotExist()
    }
}



