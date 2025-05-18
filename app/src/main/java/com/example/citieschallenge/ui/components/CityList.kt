package com.example.citieschallenge.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.citieschallenge.model.City

@Composable
fun CityList(
    cities: List<City>,
    favoriteCityIds: Set<Long>,
    onClick: (City) -> Unit,
    onFavoriteToggle: (City) -> Unit,
    onMapClick: ((City) -> Unit)? = null,
    onEndReached: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(cities, key = { _, city -> city.id }) { index, city ->
            CityListItem(
                city = city,
                isFavorite = favoriteCityIds.contains(city.id),
                onClick = { onClick(city) },
                onFavoriteToggle = { onFavoriteToggle(city) },
                onMapClick = onMapClick?.let { { it(city) } }
            )
            HorizontalDivider()

            if (index == cities.lastIndex) {
                LaunchedEffect(Unit) { onEndReached() }
            }
        }
    }
}