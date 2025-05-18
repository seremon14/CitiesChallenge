package com.example.citieschallenge.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.citieschallenge.model.City

@Composable
fun CityListItem(
    city: City,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onMapClick: (() -> Unit)? = null
) {
    ListItem(
        headlineContent = { Text("${city.name}, ${city.country}") },
        supportingContent = {
            Text("Lat: ${city.coordinate.lat}, Lon: ${city.coordinate.lon}")
        },
        trailingContent = {
            Row {
                FavoriteIcon(
                    isFavorite = isFavorite,
                    onToggle = onFavoriteToggle
                )
                onMapClick?.let {
                    IconButton(onClick = it) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Ver en mapa"
                        )
                    }
                }
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

