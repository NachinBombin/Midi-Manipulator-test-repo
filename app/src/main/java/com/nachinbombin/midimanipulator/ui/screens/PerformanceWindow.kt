package com.nachinbombin.midimanipulator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.nachinbombin.midimanipulator.theme.ThemeManager
import com.nachinbombin.midimanipulator.ui.elements.MidiJoystick
import com.nachinbombin.midimanipulator.viewmodels.PianoPerformanceViewModel

@Composable
fun PerformanceWindow(viewModel: PianoPerformanceViewModel) {
    val theme = ThemeManager.currentTheme.value
    var isHardlocked by remember { mutableStateOf(false) }
    var isHolding by remember { mutableStateOf(false) }
    var gateValue by remember { mutableStateOf(0.5f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.bg)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // --- Header Bar ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(theme.bgVoices)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("ROOT: ${viewModel.currentRootNote}", color = theme.textPrimary, fontSize = 14.sp)
                Text("SCALE: ${viewModel.currentScale}", color = theme.accent, fontSize = 14.sp)
                Text("CTX: ii-V-I", color = theme.textMuted, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Reference Controls ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { isHardlocked = !isHardlocked },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (isHardlocked) theme.accentAlt else theme.accentSoft
                    )
                ) {
                    Text("SELECT NOTE")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { isHolding = !isHolding },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (isHolding) theme.accentAlt else theme.accentSoft
                    )
                ) {
                    Text("HOLD NOTE")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Joystick Section ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Melodic Joystick (Left)
                MidiJoystick(
                    label = "Melodic",
                    sectorCount = 7,
                    onValueChange = { angle, velocity ->
                        // Integrate with VoiceGenerator and ViewModel
                    }
                )

                // Portamento Slider (Center)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("PORTAMENTO", color = theme.textMuted, fontSize = 10.sp)
                    Slider(
                        value = gateValue,
                        onValueChange = { gateValue = it },
                        modifier = Modifier.width(80.dp)
                    )
                }

                // Harmonic Joystick (Right)
                MidiJoystick(
                    label = "Harmonic",
                    sectorCount = 12,
                    onValueChange = { angle, velocity ->
                        // Integrate with ChordVoicingHelper
                    }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- Strum Strips (Conceptual Layout) ---
            Text("STRUM STRIPS", color = theme.textMuted, fontSize = 12.sp)
            Column(modifier = Modifier.fillMaxWidth()) {
                repeat(6) { i ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(vertical = 4.dp)
                            .background(theme.bgElevated)
                    ) {
                        Text("Chord Type ${i+1}", color = theme.textPrimary, modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // --- System Wheels (Bottom) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("PITCH", color = theme.textMuted, fontSize = 10.sp)
                    Slider(value = 0.5f, onValueChange = {}, modifier = Modifier.width(60.dp))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("MOD", color = theme.textMuted, fontSize = 10.sp)
                    Slider(value = 0.5f, onValueChange = {}, modifier = Modifier.width(60.dp))
                }
            }
        }
    }
}
