package com.example.citieschallenge.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0066CC),
    onPrimary = Color.White,
    background = Color(0xFFFDFDFD),
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
    tertiary = Color(0xFFFFC107),
    onSecondary = Color.LightGray
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF66A9FF),
    onPrimary = Color.Black,
    background = Color(0xFF121212),
    surface = Color(0xFF1F1F1F),
    onSurface = Color(0xFFE0E0E0),
    tertiary = Color(0xFFFFC107),
    onSecondary = Color.LightGray
)
