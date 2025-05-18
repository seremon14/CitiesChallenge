package com.example.citieschallenge.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListTopBar(
    isFilteringFavorites: Boolean,
    onToggleFavorites: () -> Unit,
) {
    Column {
        TopAppBar(
            title = { Text("Cities") },
            actions = {
                IconButton(onClick = onToggleFavorites) {
                    Icon(
                        imageVector = if (isFilteringFavorites) Icons.Filled.Star else Icons.TwoTone.Star,
                        contentDescription = "Toggle Favorites"
                    )
                }
            }
        )
    }
}
