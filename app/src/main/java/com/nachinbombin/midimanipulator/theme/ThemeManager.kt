package com.nachinbombin.midimanipulator.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

data class ThemeTokens(
    val bg: Color,
    val bgVoices: Color,
    val bgElevated: Color,
    val accent: Color,
    val accentSoft: Color,
    val accentAlt: Color,
    val borderSubtle: Color,
    val textPrimary: Color,
    val textMuted: Color
)

object ThemePreset {
    val Default = ThemeTokens(
        bg = Color(0xFF171614), bgVoices = Color(0xFF111318), bgElevated = Color(0xFF1C1B19),
        accent = Color(0xFF4F9AA5), accentSoft = Color(0xFF01696F), accentAlt = Color(0xFF6DAA45),
        borderSubtle = Color(0xFF393836), textPrimary = Color(0xFFCDCCCA), textMuted = Color(0xFF7A7974)
    )
    val Vaporwave = ThemeTokens(
        bg = Color(0xFF050813), bgVoices = Color(0xFF0D0A20), bgElevated = Color(0xFF11152A),
        accent = Color(0xFFFF71CE), accentSoft = Color(0xFF01CDFE), accentAlt = Color(0xFF05FFA1),
        borderSubtle = Color(0xFF282B45), textPrimary = Color(0xFFF5F3FF), textMuted = Color(0xFFA4A3CF)
    )
    val WhiteWolf = ThemeTokens(
        bg = Color(0xFFF4F7FB), bgVoices = Color(0xFFFFFFFF), bgElevated = Color(0xFFE8EDF5),
        accent = Color(0xFF38BDF8), accentSoft = Color(0xFF22C55E), accentAlt = Color(0xFF0EA5E9),
        borderSubtle = Color(0xFFD0D7E3), textPrimary = Color(0xFF0F172A), textMuted = Color(0xFF6B7280)
    )
    // Other presets would be mapped here identically to the mirror spec
}

object ThemeManager {
    var currentTheme = mutableStateOf(ThemePreset.Default)
    
    fun applyTheme(preset: ThemeTokens) {
        currentTheme.value = preset
    }
}
