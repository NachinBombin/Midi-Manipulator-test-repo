package com.nachinbombin.midimanipulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.nachinbombin.midimanipulator.ui.screens.PerformanceWindow
import com.nachinbombin.midimanipulator.viewmodels.PianoPerformanceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = remember { PianoPerformanceViewModel() }
            PerformanceWindow(viewModel)
        }
    }
}
