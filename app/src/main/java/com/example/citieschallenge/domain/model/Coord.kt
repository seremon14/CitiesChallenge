package com.example.citieschallenge.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(
    val lon: Float,
    val lat: Float
)
