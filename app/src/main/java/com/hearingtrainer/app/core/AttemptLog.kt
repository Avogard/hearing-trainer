package com.hearingtrainer.app.core

/**
 * Where finished melodies are recorded. `ui/` only sees this interface; the Android
 * implementation (`data/FileAttemptLog`) appends JSON lines to a file. The adaptive engine will
 * read these back once it exists (docs/PLAN.md); until then the log is only written.
 */
interface AttemptLog {
    fun append(record: AttemptRecord)
}
