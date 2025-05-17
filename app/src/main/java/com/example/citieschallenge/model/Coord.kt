package com.example.citieschallenge.model

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(
    val lon: Float,
    val lat: Float
)
