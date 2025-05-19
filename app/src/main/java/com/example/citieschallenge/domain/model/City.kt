package com.example.citieschallenge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class City(
    @SerialName("_id")
    val id: Long,
    val name: String,
    val country: String,
    @SerialName("coord")
    val coordinate: Coordinate,
    val isFavorite: Boolean = false
)