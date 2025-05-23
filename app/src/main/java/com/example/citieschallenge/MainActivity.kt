package com.example.citieschallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.citieschallenge.navigation.NavGraph
import com.example.citieschallenge.ui.theme.CitiesChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CitiesChallengeTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
