package com.nachinbombin.midimanipulator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nachinbombin.midimanipulator.theme.ThemeManager

data class GamepadInput(val name: String, val type: InputType, var mapping: String = "Unassigned")
enum class InputType { BUTTON, AXIS }

@Composable
fun GamepadMappingWindow() {
    val theme = ThemeManager.currentTheme.value
    var detectedInputs by remember { 
        mutableStateOf(listOf(
            GamepadInput("L-Stick X", InputType.AXIS),
            GamepadInput("L-Stick Y", InputType.AXIS),
            GamepadInput("Button A", InputType.BUTTON),
            GamepadInput("Button B", InputType.BUTTON),
            GamepadInput("R-Trigger", InputType.AXIS)
        ))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.bg)
            .padding(16.dp)
    ) {
        Column {
            Text("GAMEPAD MAPPING", color = theme.textPrimary, fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

            Text("Detected Controller: DualSense / Xbox Core", color = theme.textMuted, fontSize = 12.sp)
            
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(detectedInputs) { input ->
                    MappingRow(input, theme)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TEST MODE", color = theme.textPrimary)
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = true, onCheckedChange = {})
            }
        }
    }
}

@Composable
fun MappingRow(input: GamepadInput, theme: com.nachinbombin.midimanipulator.theme.ThemeTokens) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(theme.bgElevated)
            .clickable { /* Open Mapping Picker */ }
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(input.name, color = theme.textPrimary, fontSize = 14.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(input.mapping, color = theme.accent, fontSize = 14.sp)
        }
    }
}
