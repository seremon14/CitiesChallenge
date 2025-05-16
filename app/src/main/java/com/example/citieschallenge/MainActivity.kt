package com.example.citieschallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.citieschallenge.navigation.NavGraph
import com.example.citieschallenge.theme.CitiesChallengeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CitiesChallengeTheme {
                NavGraph()
            }
        }
    }
}
