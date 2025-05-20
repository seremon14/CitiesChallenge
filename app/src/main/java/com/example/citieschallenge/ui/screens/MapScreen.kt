package com.example.citieschallenge.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.citieschallenge.ui.components.CityDetailDialog
import com.example.citieschallenge.ui.components.EmbeddedMap
import com.example.citieschallenge.viewmodel.ICityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    cityId: Long,
    cityName: String,
    lat: Float,
    lon: Float,
    viewModel: ICityViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val city = uiState.selectedCity ?: return
    val isFavorite = cityId in uiState.favoriteCityIds
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cityName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(cityId) }) {
                        Icon(
                            imageVector = Icons.TwoTone.Star,
                            contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                            tint = if (isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSecondary
                        )
                    }

                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Info, contentDescription = "More info")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            EmbeddedMap(cityName, lat, lon)
        }

        if (showDialog) {
            CityDetailDialog(
                city = city,
                onDismiss = { showDialog = false }
            )
        }
    }
}
