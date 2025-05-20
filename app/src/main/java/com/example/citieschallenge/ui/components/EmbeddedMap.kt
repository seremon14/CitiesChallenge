package com.example.citieschallenge.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun EmbeddedMap(cityName: String, lat: Float, lon: Float) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(lat, lon) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(lat.toDouble(), lon.toDouble()),
                14f
            ),
            durationMs = 1000
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = LatLng(lat.toDouble(), lon.toDouble())),
            title = cityName
        )
    }
}