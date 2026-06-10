package com.nachinbombin.midimanipulator.voids
class VoiceGenerator {
    fun generateHarmonies(inputNote: Int): List<Int> {
        return listOf(inputNote + 3, inputNote + 7)
    }
}
