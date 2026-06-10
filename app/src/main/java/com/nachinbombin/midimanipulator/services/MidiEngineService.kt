package com.nachinbombin.midimanipulator.services
import android.app.Service
import android.content.Intent
import android.os.IBinder

class MidiEngineService : Service() {
    external fun processMidiMessage(type: Int, value: Int)
    
    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        init { System.loadLibrary("midihandler") }
    }
}
