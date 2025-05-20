package com.example.citieschallenge.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.citieschallenge.domain.model.City

@Composable
fun CityListItem(
    city: City,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = "${city.name}, ${city.country}",
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                text = "Lat: ${city.coordinate.lat}, Lon: ${city.coordinate.lon}",
                style = MaterialTheme.typography.bodySmall
            )
        },
        trailingContent = {
            FavoriteIcon(
                isFavorite = isFavorite,
                onToggle = onFavoriteToggle
            )
        },
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}


