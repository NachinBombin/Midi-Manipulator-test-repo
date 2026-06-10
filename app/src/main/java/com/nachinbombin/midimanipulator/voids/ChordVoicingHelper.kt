package com.nachinbombin.midimanipulator.voids
class ChordVoicingHelper {
    fun getVoicing(root: Int, type: String): List<Int> {
        return listOf(root, root + 4, root + 7) // Basic major triad
    }
}
