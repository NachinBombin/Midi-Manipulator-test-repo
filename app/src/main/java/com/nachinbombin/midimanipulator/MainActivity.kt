package com.nachinbombin.midimanipulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.nachinbombin.midimanipulator.ui.screens.*
import com.nachinbombin.midimanipulator.viewmodels.PianoPerformanceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentScreen by remember { mutableStateOf("performance") }
            val viewModel = remember { PianoPerformanceViewModel() }

            // Simple navigation shell
            when (currentScreen) {
                "performance" -> PerformanceWindow(viewModel)
                "routing" -> RoutingWindow()
                "gamepad" -> GamepadMappingWindow()
            }
            
            // Simplified Floating Nav for demo
            androidx.compose.material3.FloatingActionButton(
                onClick = {
                    currentScreen = when(currentScreen) {
                        "performance" -> "routing"
                        "routing" -> "gamepad"
                        else -> "performance"
                    }
                },
                modifier = androidx.compose.ui.Modifier.align(androidx.compose.ui.Alignment.BottomEnd)
            ) {
                androidx.compose.material3.Text("NAV")
            }
        }
    }
}
