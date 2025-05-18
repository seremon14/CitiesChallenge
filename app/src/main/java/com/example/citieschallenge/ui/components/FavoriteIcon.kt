package com.example.citieschallenge.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color

@Composable
fun FavoriteIcon(
    isFavorite: Boolean,
    onToggle: () -> Unit
) {
    val gold = Color(0xFFFFC107)

    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.3f else 1f,
        animationSpec = tween(durationMillis = 300),
    )

    IconButton(onClick = onToggle) {
        Icon(
            imageVector = Icons.TwoTone.Star,
            contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
            tint = if (isFavorite) gold else Color.LightGray,
            modifier = Modifier.scale(scale)
        )
    }
}