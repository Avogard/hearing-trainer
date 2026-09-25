package com.hearingtrainer.app.data

import android.content.Context
import android.util.Log
import com.hearingtrainer.app.core.AttemptLog
import com.hearingtrainer.app.core.AttemptRecord
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors

/**
 * Appends one JSON object per line to `attempts.jsonl` in the app's private files directory
 * (`Context.filesDir`: a folder only this app can read, kept until the app is uninstalled, no
 * permission needed). JSON lines because each melody is one self-contained line, appending is
 * one write, and a partially written last line can't corrupt earlier ones — see
 * docs/DECISIONS.md, "JSON-lines attempt log".
 *
 * [append] returns at once; the write happens on one background thread shared by every instance
 * in the process, so the caller never waits for the disk, lines can't interleave, and nothing
 * needs shutting down when a ViewModel goes away.
 */
class FileAttemptLog(context: Context) : AttemptLog {

    private val file = File(context.applicationContext.filesDir, FILE_NAME)

    override fun append(record: AttemptRecord) {
        val line = record.toJson() + "\n"
        writer.execute {
            try {
                file.appendText(line)
            } catch (e: IOException) {
                // A lost log line must never take the practice loop down with it.
                Log.e(TAG, "could not append to ${file.name}", e)
            }
        }
    }

    private companion object {
        const val TAG = "HearingTrainer"
        const val FILE_NAME = "attempts.jsonl"
        val writer = Executors.newSingleThreadExecutor { runnable ->
            Thread(runnable, "HearingTrainer-log").apply { isDaemon = true }
        }
    }
}
