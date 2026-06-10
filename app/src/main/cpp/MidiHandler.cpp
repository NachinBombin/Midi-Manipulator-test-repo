#include <jni.h>
#include <mutex>

std::mutex midi_mutex;

extern "C" JNIEXPORT void JNICALL
Java_com_nachinbombin_midimanipulator_services_MidiEngineService_processMidiMessage(JNIEnv *env, jobject thiz, jint type, jint value) {
    std::lock_guard<std::mutex> lock(midi_mutex);
    // Low-latency MIDI processing happens here
}
