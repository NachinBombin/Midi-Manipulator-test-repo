package com.nachinbombin.midimanipulator.voids

/**
 * ChordVoicingHelper: Generates MIDI note sets for professional harmonizations.
 * Handles everything from basic triads to complex extended voicings.
 */
class ChordVoicingHelper {
    
    // Voicing definitions as semitone offsets from root
    private val voicingMap = mapOf(
        "Major" to listOf(0, 4, 7),
        "Minor" to listOf(0, 3, 7),
        "Triad" to listOf(0, 4, 7), // Default to Major for generic "Triad"
        "7th" to listOf(0, 4, 7, 11),
        "9th" to listOf(0, 4, 7, 11, 14),
        "11th" to listOf(0, 4, 7, 11, 14, 17),
        "13th" to listOf(0, 4, 7, 11, 14, 17, 21),
        "sus2" to listOf(0, 2, 7),
        "sus4" to listOf(0, 5, 7),
        "Power" to listOf(0, 7),
        "add9" to listOf(0, 4, 7, 14),
        "maj7" to listOf(0, 4, 7, 11),
        "min7" to listOf(0, 3, 7, 10),
        "dim7" to listOf(0, 3, 6, 9),
        "aug" to listOf(0, 4, 8),
        "half-dim" to listOf(0, 3, 6, 10)
    )

    /**
     * Generates a full voicing for a specific chord type.
     * @param root The MIDI root note.
     * @param type The chord type (e.g., "maj7", "sus4").
     * @return A list of MIDI notes constituting the chord.
     */
    fun getVoicing(root: Int, type: String): List<Int> {
        val offsets = voicingMap[type] ?: voicingMap["Major"]!!
        return offsets.map { root + it }
    }

    /**
     * Generates a "Diatonic Stack" — builds a chord by stacking thirds 
     * restricted to the current active scale degrees.
     */
    fun getDiatonicStack(root: Int, scaleDegrees: List<Int>, stackSize: Int = 3): List<Int> {
        val voicing = mutableListOf<Int>()
        var currentDegreeIndex = 0
        
        // Find where the root sits in the scaleDegrees list
        val rootOffset = root % 12
        val startIndex = scaleDegrees.indexOfFirst { it == rootOffset }
        
        if (startIndex == -1) return getVoicing(root, "Major") // Fallback

        for (i in 0 until stackSize) {
            val degreeIndex = (startIndex + (i * 2)) % scaleDegrees.size
            val offset = scaleDegrees[degreeIndex]
            
            // Calculate octave jump
            val octave = (startIndex + (i * 2)) / scaleDegrees.size
            voicing.add(root - rootOffset + offset + (octave * 12))
        }
        
        return voicing
    }
}
