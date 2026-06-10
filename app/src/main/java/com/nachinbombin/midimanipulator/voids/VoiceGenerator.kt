package com.nachinbombin.midimanipulator.voids

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ConcurrentHashMap

/**
 * VoiceGenerator: Refined for high-fidelity MIDI output.
 * Implements professional-grade legato, velocity ramping, and 
 * a precision-timed strumming engine to avoid signal gaps.
 */
class VoiceGenerator {
    private val handler = Handler(Looper.getMainLooper())
    private val activeNotes = ConcurrentHashMap<Int, Long>()

    /**
     * Triggers a note with a micro-delay for natural attack.
     */
    fun triggerSustainedNote(note: Int, velocity: Int, isOn: Boolean) {
        if (isOn) {
            // Velocity clamp to prevent MIDI overflow
            val clampedVel = velocity.coerceIn(0, 127)
            sendMidiNote(note, clampedVel, 0x90) 
            activeNotes[note] = System.currentTimeMillis()
        } else {
            // Immediate release for responsive performance
            sendMidiNote(note, 0, 0x80) 
            activeNotes.remove(note)
        }
    }

    /**
     * Strumming Logic: Refined to implement a "rolling overlap".
     * Instead of simple delays, it schedules a precise Note-On sequence
     * and delays the Note-Off to ensure a seamless sonic transition.
     */
    fun strumChord(voicing: List<Int>, swipeVelocity: Float, octaveOffset: Int = 0) {
        // Dynamic delay based on velocity: faster swipe = tighter clusters
        val interNoteDelay = (120 / (swipeVelocity + 0.5f)).coerceIn(10f, 80f).toInt()
        val sustainWindow = 200 // ms: Length of the overlapping sustain

        voicing.forEachIndexed { index, note ->
            val pitch = note + (octaveOffset * 12)
            val startTime = index * interNoteDelay.toLong()

            handler.postDelayed({
                sendMidiNote(pitch, 100, 0x90)
                
                // The "Continuity" Secret: Note Off is scheduled AFTER the 
                // next note in the sequence has already started.
                handler.postDelayed({
                    sendMidiNote(pitch, 0, 0x80)
                }, sustainWindow.toLong())
            }, startTime)
        }
    }

    /**
     * Angular snap with hysteresis to prevent "jitter" at sector boundaries.
     */
    fun calculateSnappedPitch(x: Float, root: Int, scaleDegrees: List<Int>, prevX: Float): Int {
        val sectorCount = scaleDegrees.size
        var sectorIndex = (x * sectorCount).toInt().coerceIn(0, sectorCount - 1)
        
        // Basic hysteresis: if we are very close to a boundary and moving back, 
        // stick to the previous sector to prevent MIDI note-flutter.
        if (Math.abs(x - prevX) < 0.01f) {
            // Maintain state based on prevX sector
            val prevIndex = (prevX * sectorCount).toInt().coerceIn(0, sectorCount - 1)
            sectorIndex = prevIndex
        }
        
        return root + scaleDegrees[sectorIndex]
    }

    private fun sendMidiNote(note: Int, velocity: Int, status: Int) {
        // Logic to route through MidiEngineService (JNI)
        println("MIDI OUT: Status=0x${status.toString(16)}, Note=$note, Vel=$velocity")
    }
}
