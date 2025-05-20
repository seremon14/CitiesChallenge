package com.example.citieschallenge.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.citieschallenge.domain.model.City

@Composable
fun CityDetailDialog(
    city: City,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = {
            Text("${city.name}, ${city.country}")
        },
        text = {
            Column {
                Text("Latitude: ${city.coordinate.lat}")
                Text("Longitude: ${city.coordinate.lon}")
                Text("ID: ${city.id}")
                // Agrega aquí más información si lo deseas
            }
        }
    )
}
