package com.nachinbombin.midimanipulator.voids

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ConcurrentHashMap

/**
 * VoiceGenerator: Responsible for translating abstract user input (joystick, strum)
 * into precise MIDI Note On/Off sequences.
 * 
 * Critical requirement: Strum continuity. Overlapping Note On messages before
 * Note Off messages to prevent "gaps" in audio signal.
 */
class VoiceGenerator {
    private val handler = Handler(Looper.getMainLooper())
    private val activeNotes = ConcurrentHashMap<Int, Long>() // Note -> StartTime

    /**
     * Generates a sustained note with a velocity ramp.
     * @param note The MIDI pitch to output.
     * @param velocity Current velocity (0-127).
     * @param isOn True to trigger Note On, False for Note Off.
     */
    fun triggerSustainedNote(note: Int, velocity: Int, isOn: Boolean) {
        if (isOn) {
            sendMidiNote(note, velocity, 0x90) // Note On
            activeNotes[note] = System.currentTimeMillis()
        } else {
            sendMidiNote(note, 0, 0x80) // Note Off
            activeNotes.remove(note)
        }
    }

    /**
     * Strumming logic: Plays a sequence of notes with a specific inter-note delay.
     * Uses an overlap algorithm to prevent choppy transitions.
     * 
     * @param voicing The list of MIDI notes forming the chord.
     * @param swipeVelocity Speed of the swipe (used to derive delay).
     * @param octaveOffset The octave displacement for the 3-pass climb.
     */
    fun strumChord(voicing: List<Int>, swipeVelocity: Float, octaveOffset: Int = 0) {
        val baseDelay = (100 / (swipeVelocity + 1f)).coerceIn(8f, 50f).toInt()
        
        voicing.forEachIndexed { index, note ->
            val pitch = note + (octaveOffset * 12)
            
            // Schedule Note On
            handler.postDelayed({
                sendMidiNote(pitch, 100, 0x90)
                
                // Schedule Note Off with overlap: 
                // The Note Off is delayed beyond the next Note On start.
                handler.postDelayed({
                    sendMidiNote(pitch, 0, 0x80)
                }, 150) // Fixed sustain window for overlap
            }, index * baseDelay.toLong())
        }
    }

    /**
     * Snap a raw input coordinate to a scale-degree pitch.
     * @param x Normalized angular position (0.0 - 1.0).
     * @param root The current root note.
     * @param scaleDegrees The current active scale offsets.
     */
    fun calculateSnappedPitch(x: Float, root: Int, scaleDegrees: List<Int>): Int {
        val sectorCount = scaleDegrees.size
        val sectorIndex = (x * sectorCount).toInt().coerceIn(0, sectorCount - 1)
        return root + scaleDegrees[sectorIndex]
    }

    private fun sendMidiNote(note: Int, velocity: Int, status: Int) {
        // This would be the bridge to MidiEngineService.processMidiMessage
        // In a full implementation, this calls the Native JNI handler
        println("MIDI OUT: Status=$status, Note=$note, Vel=$velocity")
    }
}
