package com.nachinbombin.midimanipulator.voids

import java.util.*

/**
 * PitchClassifier: Analyzes a stream of MIDI notes to infer the most probable 
 * Root Note and Musical Scale.
 */
class PitchClassifier {
    // Pitch class sets for common scales (semitone offsets from root)
    private val scaleDefinitions = mapOf(
        "Major" to listOf(0, 2, 4, 5, 7, 9, 11),
        "Natural Minor" to listOf(0, 2, 3, 5, 7, 8, 10),
        "Harmonic Minor" to listOf(0, 2, 3, 5, 7, 8, 11),
        "Melodic Minor" to listOf(0, 2, 3, 5, 7, 9, 11),
        "Dorian" to listOf(0, 2, 3, 5, 7, 9, 10),
        "Phrygian" to listOf(0, 1, 3, 5, 7, 8, 10),
        "Lydian" to listOf(0, 2, 4, 6, 7, 9, 11),
        "Mixolydian" to listOf(0, 2, 4, 5, 7, 9, 10),
        "Locrian" to listOf(0, 1, 3, 5, 6, 8, 10),
        "Major Pentatonic" to listOf(0, 2, 4, 7, 9),
        "Minor Pentatonic" to listOf(0, 3, 5, 7, 10),
        "Blues" to listOf(0, 3, 5, 6, 7, 10),
        "Whole Tone" to listOf(0, 2, 4, 6, 8, 10),
        "Diminished" to listOf(0, 2, 3, 5, 6, 8, 9, 11)
    )

    /**
     * Analyzes a rolling buffer of MIDI notes to find the best fitting scale.
     * @param noteBuffer A list of recent MIDI notes.
     * @return Pair of (Root Note, Scale Name)
     */
    fun analyze(noteBuffer: List<Int>): Pair<Int, String> {
        if (noteBuffer.isEmpty()) return Pair(60, "Chromatic")

        // 1. Convert all notes to pitch classes (0-11)
        val pitchClasses = noteBuffer.map { it % 12 }.distinct().sorted()
        
        var bestRoot = 60
        var bestScale = "Chromatic"
        var maxMatches = -1

        // 2. Iterate through every possible root (0-11)
        for (root in 0..11) {
            // 3. Compare the captured pitch classes against every known scale definition
            for ((scaleName, offsets) in scaleDefinitions) {
                val scaleSet = offsets.map { (it + root) % 12 }.toSet()
                val matches = pitchClasses.count { it in scaleSet }
                
                if (matches > maxMatches) {
                    maxMatches = matches
                    bestRoot = root
                    bestScale = scaleName
                }
            }
        }

        // Convert root pitch class back to a MIDI note (defaulting to Octave 4 / Middle C range)
        val midiRoot = bestRoot + 60 
        return Pair(midiRoot, bestScale)
    }
}
