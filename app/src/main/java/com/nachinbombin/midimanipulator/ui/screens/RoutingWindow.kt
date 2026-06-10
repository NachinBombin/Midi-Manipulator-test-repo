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

data class MidiPort(val name: String, val id: String, val type: PortType, var isSelected: Boolean = false)
enum class PortType { USB, BLE, VIRTUAL }

@Composable
fun RoutingWindow() {
    val theme = ThemeManager.currentTheme.value
    var inputs by remember { mutableStateOf(listOf<MidiPort>()) }
    var outputs by remember { mutableStateOf(listOf<MidiPort>()) }
    var virtualPortEnabled by remember { mutableStateOf(false) }

    // Simulate discovery of ports
    LaunchedEffect(Unit) {
        inputs = listOf(
            MidiPort("USB Keyboard", "usb_1", PortType.USB),
            MidiPort("BLE Controller", "ble_1", PortType.BLE),
            MidiPort("Internal Virtual", "virt_1", PortType.VIRTUAL)
        )
        outputs = listOf(
            MidiPort("System Default", "out_1", PortType.USB),
            MidiPort("DAW Virtual Port", "out_virt", PortType.VIRTUAL)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.bg)
            .padding(16.dp)
    ) {
        Column {
            Text("MIDI ROUTING", color = theme.textPrimary, fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

            // --- Input Section ---
            Text("INPUTS", color = theme.textMuted, fontSize = 12.sp)
            LazyColumn(modifier = Modifier.height(200.dp).fillMaxWidth()) {
                items(inputs) { port ->
                    PortItem(port, theme) { 
                        inputs = inputs.map { if(it.id == port.id) it.copy(isSelected = !it.isSelected) else it }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Output Section ---
            Text("OUTPUTS", color = theme.textMuted, fontSize = 12.sp)
            LazyColumn(modifier = Modifier.height(200.dp).fillMaxWidth()) {
                items(outputs) { port ->
                    PortItem(port, theme) { 
                        outputs = outputs.map { if(it.id == port.id) it.copy(isSelected = !it.isSelected) else it }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Virtual Port Toggle ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Expose Virtual Port", color = theme.textPrimary)
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = virtualPortEnabled,
                    onCheckedChange = { virtualPortEnabled = it }
                )
            }
            
            Button(
                onClick = { /* Trigger BLE Scan */ },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = theme.accent)
            ) {
                Text("SCAN FOR BLE MIDI DEVICES")
            }
        }
    }
}

@Composable
fun PortItem(port: MidiPort, theme: com.nachinbombin.midimanipulator.theme.ThemeTokens, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(if (port.isSelected) theme.accentSoft else theme.bgElevated)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${port.type}: ${port.name}", 
                color = if (port.isSelected) Color.White else theme.textPrimary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            if (port.isSelected) {
                Text("ACTIVE", color = theme.accentAlt, fontSize = 10.sp)
            }
        }
    }
}
