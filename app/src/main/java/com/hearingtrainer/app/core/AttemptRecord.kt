package com.hearingtrainer.app.core

/**
 * Everything worth remembering about one finished melody, in both modes. One of these is appended
 * to the attempt log per melody; the adaptive engine planned in docs/PLAN.md is built from them.
 *
 * Free play records (mode = FREE) exist as a trace only: they never count toward a score or a
 * statistic, [positions] use CORRECT / WRONG, [firstTryCount] is 0 and [responseTimesMs] is empty.
 *
 * @param timestampMillis wall-clock time the melody finished, ms since the Unix epoch.
 * @param keyRoot the melody's key, as the root's MIDI note (60 = C major around middle C).
 * @param melody the target notes, MIDI numbers.
 * @param cadencePlayed whether a cadence sounded before the melody.
 * @param replaysUsed how many times the melody was replayed before answering.
 * @param positions the outcome at each position, same length as [melody].
 * @param wrongPresses every wrong press, in order.
 * @param responseTimesMs per position, from the moment the keys became available for it (playback end,
 *   the previous position resolving, or the end of a reveal) to the press that resolved it.
 * @param firstTryCount how many positions were answered right at the first press.
 * @param total number of notes; the score is [firstTryCount] over this.
 * @param silentKeyboard whether keys were silent while answering (Features.SILENT_SCORED_KEYBOARD).
 */
data class AttemptRecord(
    val timestampMillis: Long,
    val mode: AnswerMode,
    val keyRoot: Int,
    val melody: List<Int>,
    val tempoBpm: Int,
    val cadencePlayed: Boolean,
    val replaysUsed: Int,
    val positions: List<PositionOutcome>,
    val wrongPresses: List<WrongPress>,
    val responseTimesMs: List<Long>,
    val firstTryCount: Int,
    val total: Int,
    val silentKeyboard: Boolean,
) {
    /** One JSON object on one line, no trailing newline. */
    fun toJson(): String = Json.obj(
        "timestampMillis" to timestampMillis.toString(),
        "mode" to Json.string(mode.jsonName),
        "keyRoot" to keyRoot.toString(),
        "melody" to Json.numberArray(melody),
        "tempoBpm" to tempoBpm.toString(),
        "cadencePlayed" to cadencePlayed.toString(),
        "replaysUsed" to replaysUsed.toString(),
        "positions" to Json.stringArray(positions.map { it.jsonName }),
        "wrongPresses" to wrongPresses.joinToString(",", "[", "]") { press ->
            Json.obj(
                "position" to press.position.toString(),
                "target" to press.target.toString(),
                "pressed" to press.pressed.toString(),
            )
        },
        "responseTimesMs" to Json.numberArray(responseTimesMs),
        "firstTryCount" to firstTryCount.toString(),
        "total" to total.toString(),
        "silentKeyboard" to silentKeyboard.toString(),
    )

    companion object {
        /** The record for a scored melody, from its [ScoredAnswer.result]. */
        fun scored(
            timestampMillis: Long,
            keyRoot: Int,
            melody: List<Int>,
            tempoBpm: Int,
            cadencePlayed: Boolean,
            replaysUsed: Int,
            outcome: AnswerOutcome,
            silentKeyboard: Boolean,
        ): AttemptRecord = AttemptRecord(
            timestampMillis = timestampMillis,
            mode = AnswerMode.SCORED,
            keyRoot = keyRoot,
            melody = melody,
            tempoBpm = tempoBpm,
            cadencePlayed = cadencePlayed,
            replaysUsed = replaysUsed,
            positions = outcome.positions,
            wrongPresses = outcome.wrongPresses,
            responseTimesMs = outcome.responseTimesMs,
            firstTryCount = outcome.firstTryCount,
            total = outcome.total,
            silentKeyboard = silentKeyboard,
        )

        /** The record for a free-play melody, from what Check compared. Never counts toward anything. */
        fun free(
            timestampMillis: Long,
            keyRoot: Int,
            melody: List<Int>,
            answer: List<Int>,
            tempoBpm: Int,
            cadencePlayed: Boolean,
            replaysUsed: Int,
        ): AttemptRecord {
            val score = AnswerScorer.score(melody, answer)
            return AttemptRecord(
                timestampMillis = timestampMillis,
                mode = AnswerMode.FREE,
                keyRoot = keyRoot,
                melody = melody,
                tempoBpm = tempoBpm,
                cadencePlayed = cadencePlayed,
                replaysUsed = replaysUsed,
                positions = score.perNoteCorrect.map { if (it) PositionOutcome.CORRECT else PositionOutcome.WRONG },
                wrongPresses = melody.indices
                    .filter { !score.perNoteCorrect[it] }
                    .map { WrongPress(position = it, target = melody[it], pressed = answer[it]) },
                responseTimesMs = emptyList(),
                firstTryCount = 0,
                total = melody.size,
                silentKeyboard = false,
            )
        }
    }
}
