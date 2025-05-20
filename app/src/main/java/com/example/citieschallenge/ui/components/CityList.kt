package com.example.citieschallenge.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.citieschallenge.domain.model.City

@Composable
fun CityList(
    cities: List<City>,
    favoriteCityIds: Set<Long>,
    onClick: (City) -> Unit,
    onFavoriteToggle: (City) -> Unit,
    onEndReached: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(cities, key = { _, city -> city.id }) { index, city ->
            CityListItem(
                city = city,
                isFavorite = favoriteCityIds.contains(city.id),
                onClick = { onClick(city) },
                onFavoriteToggle = { onFavoriteToggle(city) }
            )
            HorizontalDivider(
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )

            if (index == cities.lastIndex) {
                LaunchedEffect(Unit) { onEndReached() }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
