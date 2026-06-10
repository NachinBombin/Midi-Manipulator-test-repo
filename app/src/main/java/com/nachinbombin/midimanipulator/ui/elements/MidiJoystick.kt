package com.nachinbombin.midimanipulator.ui.elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.nachinbombin.midimanipulator.theme.ThemeManager
import kotlin.math.*

/**
 * MidiJoystick: A professional-grade custom joystick for melodic and harmonic control.
 * Distance from center -> Velocity.
 * Angle -> Scale Degree / Chord Type.
 */
@Composable
fun MidiJoystick(
    label: String,
    sectorCount: Int,
    onValueChange: (angle: Float, velocity: Float) -> Unit
) {
    val theme = ThemeManager.currentTheme.value
    var joystickPos by remember { mutableStateOf(Offset.Zero) }
    val radius = 150f // Fixed visual radius in pixels

    Box(
        modifier = Modifier
            .size(300.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    val newPos = Offset(
                        x = (joystickPos.x + dragAmount.x).coerceIn(-radius, radius),
                        y = (joystickPos.y + dragAmount.y).coerceIn(-radius, radius)
                    )
                    joystickPos = newPos
                    
                    // Calculate Angle (0.0 to 1.0) and Velocity (0.0 to 1.0)
                    val angle = (atan2(newPos.y, newPos.x) * (180 / PI).toFloat()).let { 
                        if (it < 0) it + 360 else it 
                    } / 360f
                    
                    val dist = sqrt(newPos.x * newPos.x + newPos.y * newPos.y)
                    val velocity = (dist / radius).coerceIn(0f, 1f)
                    
                    onValueChange(angle, velocity)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            
            // 1. Draw outer ring (Sectors)
            drawCircle(
                color = theme.borderSubtle,
                radius = radius,
                style = Stroke(width = 2f)
            )
            
            // 2. Draw sector lines
            for (i in 0 until sectorCount) {
                val angle = (i * 360f / sectorCount) * (PI / 180f).toFloat()
                drawLine(
                    color = theme.borderSubtle,
                    start = center,
                    end = Offset(
                        center.x + cos(angle) * radius,
                        center.y + sin(angle) * radius
                    ),
                    strokeWidth = 1f
                )
            }

            // 3. Draw the Joystick handle
            drawCircle(
                color = theme.accent,
                radius = 20f,
                center = Offset(center.x + joystickPos.x, center.y + joystickPos.y)
            )
        }
    }
}
