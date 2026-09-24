package com.hearingtrainer.app.audio

import android.content.Context
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * All piano samples decoded into memory as floats in [-1, 1), keyed by MIDI note. About 4 MB in
 * total for the C3..C6 set, loaded once (roughly 50-100 ms on a phone, on the audio thread
 * before it starts producing sound).
 */
internal class SampleBank private constructor(
    private val samples: Map<Int, FloatArray>,
    val sampleRate: Int,
) {
    operator fun get(note: Int): FloatArray? = samples[note]

    companion object {
        fun load(context: Context): SampleBank {
            var sampleRate = 0
            val samples = HashMap<Int, FloatArray>()
            for ((note, resId) in PianoSamples.RES_IDS) {
                val bytes = context.resources.openRawResource(resId).use { it.readBytes() }
                val wav = WavDecoder.decode(bytes)
                check(wav.channels == 1) { "piano sample for note $note must be mono, has ${wav.channels} channels" }
                check(sampleRate == 0 || sampleRate == wav.sampleRate) {
                    "piano samples must all share one sample rate: $sampleRate vs ${wav.sampleRate} (note $note)"
                }
                sampleRate = wav.sampleRate
                samples[note] = wav.samples
            }
            return SampleBank(samples, sampleRate)
        }
    }
}

/** A minimal RIFF/WAVE reader: 16-bit PCM only, which is what tools/render_piano_samples.py writes. */
internal object WavDecoder {

    class Decoded(val sampleRate: Int, val channels: Int, val samples: FloatArray)

    fun decode(bytes: ByteArray): Decoded {
        val buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        require(bytes.size >= 12 && tag(bytes, 0) == "RIFF" && tag(bytes, 8) == "WAVE") { "not a WAV file" }

        var channels = 0
        var sampleRate = 0
        var bitsPerSample = 0
        var samples: FloatArray? = null

        // A WAV is a list of chunks: 4-byte id, 4-byte size, payload (padded to an even size).
        var pos = 12
        while (pos + 8 <= bytes.size) {
            val id = tag(bytes, pos)
            val size = buf.getInt(pos + 4)
            val body = pos + 8
            when (id) {
                "fmt " -> {
                    val format = buf.getShort(body).toInt()
                    require(format == 1) { "only PCM WAV is supported, got format $format" }
                    channels = buf.getShort(body + 2).toInt()
                    sampleRate = buf.getInt(body + 4)
                    bitsPerSample = buf.getShort(body + 14).toInt()
                }
                "data" -> {
                    require(bitsPerSample == 16) { "only 16-bit WAV is supported, got $bitsPerSample-bit" }
                    val count = minOf(size, bytes.size - body) / 2
                    val out = FloatArray(count)
                    for (i in 0 until count) {
                        out[i] = buf.getShort(body + 2 * i) / 32768f
                    }
                    samples = out
                }
            }
            pos = body + size + (size and 1)
        }
        return Decoded(
            sampleRate = sampleRate,
            channels = channels,
            samples = requireNotNull(samples) { "WAV has no data chunk" },
        )
    }

    private fun tag(bytes: ByteArray, at: Int): String = String(bytes, at, 4, Charsets.US_ASCII)
}
