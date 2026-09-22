package com.hearingtrainer.app.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.hearingtrainer.app.R
import kotlin.math.abs
import kotlin.math.pow

/**
 * Plays notes with SoundPool from three synthesized one-shot samples (`res/raw/piano_c*.wav`,
 * see docs/DECISIONS.md), pitch-shifted with SoundPool's playback rate to cover the notes in
 * between. This keeps the asset count small without a real sample library, per the "Sampled
 * piano via SoundPool first" decision.
 *
 * The three references are an octave apart, so no note is ever more than 6 semitones from its
 * nearest one — well inside SoundPool's supported 0.5x-2x rate range, which covers a full
 * octave (±12 semitones) either way.
 */
class SoundPoolNotePlayer(context: Context) : NotePlayer {

    private data class Reference(val note: Int, val resId: Int)

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_STREAMS)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val references = listOf(
        Reference(48, R.raw.piano_c3), // C3
        Reference(60, R.raw.piano_c4), // C4, middle C
        Reference(72, R.raw.piano_c5), // C5
    )

    // load() returns a sample id immediately, but the sample isn't playable until the async
    // load actually finishes — soundIds are known right away, loadedSampleIds fills in as
    // onLoadComplete fires for each one.
    private val soundIds: Map<Int, Int> = references.associate { it.note to soundPool.load(context, it.resId, 1) }
    private val loadedSampleIds = mutableSetOf<Int>()

    init {
        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) loadedSampleIds += sampleId
        }
    }

    override fun play(note: Int) {
        val reference = references.minByOrNull { abs(it.note - note) } ?: return
        val soundId = soundIds.getValue(reference.note)
        if (soundId !in loadedSampleIds) return // sample not finished loading yet; drop it

        val semitoneOffset = note - reference.note
        val rate = 2.0.pow(semitoneOffset / 12.0).toFloat().coerceIn(0.5f, 2.0f)
        soundPool.play(soundId, VOLUME, VOLUME, /* priority = */ 1, /* loop = */ 0, rate)
    }

    override fun release() {
        soundPool.release()
    }

    private companion object {
        const val MAX_STREAMS = 6
        const val VOLUME = 1.0f
    }
}
