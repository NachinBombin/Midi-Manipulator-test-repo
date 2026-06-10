package com.nachinbombin.midimanipulator.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*
import kotlinx.coroutines.*

/**
 * MidiEngineService: The core, persistent background service responsible for 
 * real-time MIDI data streaming, routing, and feeding the State Machine.
 * This simulates continuous operation on a dedicated low-latency thread.
 */
class MidiEngineService : Service() {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var midiProcessingJob: Job? = null

    // --- STATE MANAGEMENT (Simulated/Observable via ViewModel) ---
    var currentInputBuffer: MutableList<Int> = mutableListOf() 
    val isInitialized: Boolean get() = true
        
    // Tracks the last processed state for UI updates
    @Volatile private var latestRootNote: Int = 0
    @Volatile private var latestScale: String = "Unknown"

    companion object {
        init { System.loadLibrary("midihandler") }
    }
    
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startProcessingLoop()
    }

    override fun onDestroy() {
        super.onDestroy()
        midiProcessingJob?.cancel() // Clean up the dedicated background thread/coroutine
    }

    /**
     * Starts a coroutine job to continuously monitor and process incoming MIDI data.
     */
    private fun startProcessingLoop() {
        midiProcessingJob = scope.launch(Dispatchers.IO) {
            while (isActive) {
                // --- SIMULATION STEP: In reality, this is triggered by the MidiManager listener ---
                delay(200) // Simulate buffering/polling interval
                processInputBufferSnapshot() 
                // The actual communication back to the ViewModel would happen here 
                // via Flow/LiveData, but for simplicity, we simulate a call.
            }
        }

        println("MIDI Engine Service started and is listening on dedicated background thread.")
    }


    /**
     * CRITICAL PATH: Handles any raw incoming MIDI message (Note On/Off).
     */
    fun processRawMidiMessage(type: Int, value: Int) {
        synchronized(this) {
            val note = if (type == 0x90 && value >= 32) value else -1 // Simple Note tracking
            if (note != -1) {
                currentInputBuffer.add(note)
                // Notify listeners/ViewModel immediately for live feedback
                processInputBufferSnapshot() 
            }
        }
    }

    /**
     * Takes a snapshot of the buffer and performs all intelligence updates: 
	 * 1. Analysis (PitchClassifier).
	 * 2. State update (latestRootNote, latestScale).
	* @param forceProcess If true, processes regardless of timing gap.
	*/
    private fun processInputBufferSnapshot(forceProcess: Boolean = false) {
        if (!isInitialized || currentInputBuffer.isEmpty()) return

        // 1. DETECT CONTEXT (The "Brain" call)
        val classifier = com.nachinbombin.midimanipulator.voids.PitchClassifier()
        val noteBufferSnapshot = currentInputBuffer.toList() // Copy for safe analysis
        val (root, scale) = classifier.analyze(noteBufferSnapshot)
        
        // Update global state
        latestRootNote = root 
        latestScale = scale

        println("DEBUG: Context updated -> Root=$root, Scale=$scale")

        // 2. ROUTE AND GENERATE (The "Muscles" call)
        if (!forceProcess && noteBufferSnapshot.isNotEmpty()) {
            // Here, we would check the current UI state (from ViewModel/Gamepad Mapping)
            // to see if ANY voice generation is active before proceeding.

            // Example: If the system detects a Note On and no joystick control was initiated, 
            // generate a basic harmonizing chord as supplemental audio.
            val chordHelper = com.nachinbombin.midimanipulator.voids.ChordVoicingHelper()
            try {
                 // Simulate generating a triad if we hit an input note on its root pitch class
                if (noteBufferSnapshot.contains(root) && scale != "Chromatic") {
                    val triad = chordHelper.getVoicing(root, "Triad")
                    println("MIDI GENERATED: Sending Triad for live fill to output channels.")
                    // Actual implementation would call VoiceGenerator here to route the chord.
                }
            } catch (e: Exception) {
                 // Handle failed harmonic generation gracefully.
            }
        }

        // 3. CLEANUP: Remove analyzed notes from the buffer after a delay window passes
        if (!forceProcess && currentInputBuffer.size > 10) {
             currentInputBuffer.removeAt(0) // Simple FIFO cleanup for simulation
        }
    }
}
