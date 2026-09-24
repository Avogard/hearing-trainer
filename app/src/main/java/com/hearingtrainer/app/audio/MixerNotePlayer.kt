package com.hearingtrainer.app.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Process
import android.util.Log
import com.hearingtrainer.app.core.Config
import com.hearingtrainer.app.core.TimedNote
import kotlin.math.abs
import kotlin.math.roundToLong
import kotlin.math.tanh

/**
 * A small software mixer on top of [AudioTrack] — the Android equivalent of "open the audio
 * device and feed it PCM from a callback thread". It replaces SoundPool (see docs/DECISIONS.md,
 * "Per-note rendered samples + AudioTrack mixer").
 *
 * How it works:
 *  - One audio thread loops forever: mix the active voices into a block of 240 frames (5 ms),
 *    hand it to [AudioTrack.write], which blocks until the hardware has room for it. That's the
 *    whole scheduler; the write's back-pressure is the clock.
 *  - A voice is one sample being read from a position, with an optional release: from
 *    [Voice.releaseFrame] on, its gain ramps to zero over [Config.NOTE_RELEASE_SECONDS] and the
 *    voice is dropped.
 *  - Everything is timed in *frames* (sample-rate ticks since the thread started), so a melody
 *    scheduled with [playSequence] lands on exact sample positions regardless of UI-thread
 *    jitter. `play` just schedules a voice for the next block.
 *  - Callers and the audio thread share [voices] and [pending] under [lock]. The audio thread
 *    holds the lock only while mixing a 5 ms block (a few microseconds of work), never while
 *    blocked in `write`, so the UI thread never waits on the audio hardware.
 *
 * Per-voice level and the soft clipper mean two full notes at once stay clean and a pile of
 * notes gets gently compressed instead of crackling.
 */
class MixerNotePlayer(context: Context) : NotePlayer {

    private class Voice(val samples: FloatArray, val startFrame: Long, var releaseFrame: Long) {
        var position = 0 // next sample to read
        var gain = 1f // release envelope; the voice is dropped once this reaches 0
    }

    private val appContext = context.applicationContext
    private val lock = Any()
    private val voices = ArrayList<Voice>()
    private val pending = ArrayList<Voice>()

    /** Frames handed to the track so far; also "the start of the next block" for scheduling. */
    private var framePosition = 0L

    @Volatile private var bank: SampleBank? = null
    @Volatile private var running = true
    @Volatile private var active = true

    private var sampleRate = DEFAULT_SAMPLE_RATE
    private var releaseStep = 1f / (Config.NOTE_RELEASE_SECONDS * DEFAULT_SAMPLE_RATE).toFloat()

    private val thread = Thread(::audioThread, "HearingTrainer-audio").apply { start() }

    // --- NotePlayer ---------------------------------------------------------------------------

    override fun play(note: Int) {
        val samples = bank?.get(note) ?: return // not loaded yet, or outside the sampled range
        synchronized(lock) {
            schedule(Voice(samples, startFrame = framePosition, releaseFrame = Long.MAX_VALUE))
        }
    }

    override fun playSequence(sequence: List<TimedNote>) {
        val bank = bank ?: return
        synchronized(lock) {
            releaseAllLocked()
            val base = framePosition + BLOCK_FRAMES
            for (timed in sequence) {
                val samples = bank[timed.note] ?: continue
                val start = base + (timed.startSeconds * sampleRate).roundToLong()
                val release = start + (timed.durationSeconds * sampleRate).roundToLong()
                schedule(Voice(samples, startFrame = start, releaseFrame = release))
            }
        }
    }

    override fun stop() {
        synchronized(lock) { releaseAllLocked() }
    }

    override fun setActive(active: Boolean) {
        if (!active) stop()
        this.active = active
    }

    override fun release() {
        running = false
        active = true // let a paused audio thread wake up and exit
        thread.join(1000)
    }

    // --- scheduling (call with lock held) ---------------------------------------------------

    private fun schedule(voice: Voice) {
        if (voices.size + pending.size >= Config.MAX_VOICES) {
            // Steal the oldest sounding voice by releasing it now; it fades over the release time.
            voices.minByOrNull { it.startFrame }?.let { it.releaseFrame = minOf(it.releaseFrame, framePosition) }
        }
        pending += voice
    }

    private fun releaseAllLocked() {
        pending.clear()
        for (voice in voices) voice.releaseFrame = minOf(voice.releaseFrame, framePosition)
    }

    // --- audio thread ----------------------------------------------------------------------

    private fun audioThread() {
        try {
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
        } catch (e: SecurityException) {
            Log.w(TAG, "audio thread priority not granted; continuing at normal priority", e)
        }

        val loaded = try {
            SampleBank.load(appContext)
        } catch (e: Exception) {
            Log.e(TAG, "could not load piano samples; audio disabled", e)
            return
        }
        sampleRate = loaded.sampleRate
        releaseStep = 1f / (Config.NOTE_RELEASE_SECONDS * sampleRate).toFloat()
        val track = createTrack(sampleRate) ?: return
        bank = loaded

        val block = FloatArray(BLOCK_FRAMES)
        var playing = false
        try {
            while (running) {
                if (!active) {
                    if (playing) {
                        track.pause()
                        track.flush()
                        playing = false
                        // Whatever was sounding is gone with the flush; forget it so it doesn't
                        // come back as a ghost fade-out when the output resumes.
                        synchronized(lock) {
                            voices.clear()
                            pending.clear()
                        }
                    }
                    Thread.sleep(20)
                    continue
                }
                if (!playing) {
                    track.play()
                    playing = true
                }
                mixBlock(block)
                val written = track.write(block, 0, BLOCK_FRAMES, AudioTrack.WRITE_BLOCKING)
                if (written < 0) {
                    Log.e(TAG, "AudioTrack.write failed ($written); audio disabled")
                    break
                }
            }
        } catch (e: InterruptedException) {
            // release() is the only thing that stops this thread; fall through and clean up
        } finally {
            try {
                track.pause()
                track.flush()
                track.release()
            } catch (e: IllegalStateException) {
                Log.w(TAG, "releasing AudioTrack", e)
            }
        }
    }

    private fun mixBlock(block: FloatArray) {
        block.fill(0f)
        synchronized(lock) {
            val blockStart = framePosition
            val blockEnd = blockStart + BLOCK_FRAMES

            val due = pending.iterator()
            while (due.hasNext()) {
                val voice = due.next()
                if (voice.startFrame < blockEnd) {
                    voices += voice
                    due.remove()
                }
            }

            val sounding = voices.iterator()
            while (sounding.hasNext()) {
                val voice = sounding.next()
                var i = maxOf(0L, voice.startFrame - blockStart).toInt()
                var frame = blockStart + i
                var position = voice.position
                val samples = voice.samples
                var gain = voice.gain
                while (i < BLOCK_FRAMES && position < samples.size && gain > 0f) {
                    if (frame >= voice.releaseFrame) gain -= releaseStep
                    block[i] += samples[position] * (if (gain > 0f) gain else 0f) * Config.VOICE_GAIN
                    i++
                    frame++
                    position++
                }
                voice.position = position
                voice.gain = gain
                if (position >= samples.size || gain <= 0f) sounding.remove()
            }

            framePosition = blockEnd
        }
        softClip(block)
    }

    /** Linear up to [CLIP_KNEE], then a tanh curve that never reaches 1.0: overloads get squashed, not cracked. */
    private fun softClip(block: FloatArray) {
        for (i in block.indices) {
            val x = block[i]
            val magnitude = abs(x)
            if (magnitude > CLIP_KNEE) {
                val over = (magnitude - CLIP_KNEE) / (1f - CLIP_KNEE)
                val squashed = CLIP_KNEE + (1f - CLIP_KNEE) * tanh(over)
                block[i] = if (x < 0f) -squashed else squashed
            }
        }
    }

    private fun createTrack(sampleRate: Int): AudioTrack? {
        val minBytes = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_FLOAT)
        if (minBytes <= 0) {
            Log.e(TAG, "AudioTrack.getMinBufferSize failed ($minBytes); audio disabled")
            return null
        }
        return try {
            AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setTransferMode(AudioTrack.MODE_STREAM)
                .setPerformanceMode(AudioTrack.PERFORMANCE_MODE_LOW_LATENCY)
                .setBufferSizeInBytes(maxOf(minBytes, 2 * BLOCK_FRAMES * BYTES_PER_FLOAT))
                .build()
        } catch (e: Exception) {
            Log.e(TAG, "could not create AudioTrack; audio disabled", e)
            null
        }
    }

    private companion object {
        const val TAG = "HearingTrainer"
        const val DEFAULT_SAMPLE_RATE = 48_000
        const val BLOCK_FRAMES = 240 // 5 ms at 48 kHz
        const val BYTES_PER_FLOAT = 4
        const val CLIP_KNEE = 0.7f
    }
}
