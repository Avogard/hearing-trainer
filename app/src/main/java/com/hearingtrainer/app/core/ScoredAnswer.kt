package com.hearingtrainer.app.core

/**
 * How one position of a melody was answered. The first three are what scored mode produces;
 * CORRECT / WRONG are what free play logs (a free-play answer is edited before Check, so "first
 * try" means nothing there — see docs/SPEC.md, "Answer modes").
 *
 * [jsonName] is the value written to the attempt log.
 */
enum class PositionOutcome(val jsonName: String) {
    FIRST_TRY("first_try"),
    FOUND("found"),
    REVEALED("revealed"),
    CORRECT("correct"),
    WRONG("wrong"),
}

/** One wrong key press: at which position, what the melody had there, and what was pressed. */
data class WrongPress(val position: Int, val target: Int, val pressed: Int)

/**
 * What a key press did to a [ScoredAnswer]. [position] is the position the press was for;
 * [finished] is true when that press completed the melody.
 */
sealed class PressResult {
    abstract val position: Int
    abstract val finished: Boolean

    /** The note matched: the position is resolved and the answer moved on. */
    data class Correct(override val position: Int, override val finished: Boolean) : PressResult()

    /** The note didn't match; the same position stays open for another try. Never finishes. */
    data class Wrong(override val position: Int, val wrongPressesSoFar: Int) : PressResult() {
        override val finished: Boolean get() = false
    }

    /** Too many wrong presses: [correctNote] is revealed, the position is resolved as such, and the answer moved on. */
    data class Revealed(override val position: Int, val correctNote: Int, override val finished: Boolean) : PressResult()
}

/**
 * The result of a finished [ScoredAnswer]: one [PositionOutcome] per note, every wrong press,
 * and how long each position took to resolve.
 */
data class AnswerOutcome(
    val positions: List<PositionOutcome>,
    val wrongPresses: List<WrongPress>,
    val responseTimesMs: List<Long>,
) {
    val total: Int get() = positions.size
    val firstTryCount: Int get() = positions.count { it == PositionOutcome.FIRST_TRY }

    /** Every note was found at the first try. */
    val clean: Boolean get() = firstTryCount == total

    /**
     * One line per position that was found after wrong presses or revealed, about the music and
     * nothing else: "3rd note: you played E4, it was F4" (or "you played E4 and D4" after several
     * wrong presses). A position with no recorded press (impossible from [ScoredAnswer]) gets no line.
     */
    fun feedbackLines(): List<String> = positions.indices
        .filter { positions[it] == PositionOutcome.FOUND || positions[it] == PositionOutcome.REVEALED }
        .mapNotNull { position ->
            val presses = wrongPresses.filter { it.position == position }
            val target = presses.firstOrNull()?.target ?: return@mapNotNull null
            val played = presses.joinToString(" and ") { MidiNote.name(it.pressed) }
            "${ordinal(position + 1)} note: you played $played, it was ${MidiNote.name(target)}"
        }

    companion object {
        /** 1 -> "1st", 2 -> "2nd", 3 -> "3rd", 4 -> "4th", 11 -> "11th", 21 -> "21st". */
        fun ordinal(n: Int): String {
            val suffix = if (n % 100 in 11..13) "th" else when (n % 10) {
                1 -> "st"
                2 -> "nd"
                3 -> "rd"
                else -> "th"
            }
            return "$n$suffix"
        }
    }
}

/**
 * The state machine for answering one melody in scored mode (docs/SPEC.md, "Answer modes"):
 * every key press is final. A press either matches the note at the current [position] (resolved
 * as FIRST_TRY, or FOUND if there were wrong presses before it), or it doesn't — after
 * [maxWrongPressesPerNote] wrong presses the note is revealed and the position resolves as
 * REVEALED. Either way the answer moves on; there is no undo and no separate Check step.
 *
 * Replays of the melody are allowed [maxReplays] times, and only before the first press.
 *
 * Times are plain parameters (milliseconds from any monotonic clock); this class never reads a
 * clock itself. Each position's response time runs from the moment the keys became available for
 * it to the press that resolved it: for the first position that is the end of the melody's
 * playback (a replay restarts it), after a correct press it is that press, and after a reveal it
 * is the end of the reveal — the caller reports each of those with [inputOpened].
 */
class ScoredAnswer(
    private val target: List<Int>,
    private val maxReplays: Int,
    private val maxWrongPressesPerNote: Int,
) {
    init {
        require(target.isNotEmpty()) { "target must not be empty" }
        require(maxReplays >= 0) { "maxReplays must not be negative, was $maxReplays" }
        require(maxWrongPressesPerNote >= 1) { "maxWrongPressesPerNote must be at least 1, was $maxWrongPressesPerNote" }
    }

    /** Index of the note the next press answers; equals the melody length once [isFinished]. */
    var position: Int = 0
        private set

    val isFinished: Boolean get() = position == target.size

    var replaysUsed: Int = 0
        private set

    /** Wrong presses so far at the current position (0 again after each position resolves). */
    var wrongPressesAtPosition: Int = 0
        private set

    private var pressed = false
    private var positionStartMillis: Long? = null
    private val outcomes = ArrayList<PositionOutcome>()
    private val wrongPresses = ArrayList<WrongPress>()
    private val responseTimesMs = ArrayList<Long>()

    /** The outcome of every position resolved so far, in order (the strip's colours while answering). */
    val resolvedOutcomes: List<PositionOutcome> get() = outcomes.toList()

    /** Replays not yet used. Only meaningful for the button label while [canReplay]. */
    val replaysLeft: Int get() = maxReplays - replaysUsed

    /** A replay is allowed until the first press, as long as some are left. */
    fun canReplay(): Boolean = !pressed && replaysLeft > 0

    /** Records a replay. Only valid while [canReplay]. */
    fun noteReplay() {
        check(canReplay()) { "replay not allowed: pressed=$pressed, used $replaysUsed of $maxReplays" }
        replaysUsed++
    }

    /**
     * The keys just became available at [nowMillis] — the melody or a replay stopped sounding, a
     * reveal ended, the screen came back — so the current position's response time counts from
     * here. Harmless once finished.
     */
    fun inputOpened(nowMillis: Long) {
        positionStartMillis = nowMillis
    }

    /** A key was pressed at [nowMillis]. Only valid while not [isFinished]. */
    fun press(note: Int, nowMillis: Long): PressResult {
        check(!isFinished) { "the melody is already finished" }
        pressed = true
        val current = position
        val expected = target[current]
        if (note == expected) {
            val outcome = if (wrongPressesAtPosition == 0) PositionOutcome.FIRST_TRY else PositionOutcome.FOUND
            resolve(outcome, nowMillis)
            return PressResult.Correct(current, finished = isFinished)
        }
        wrongPressesAtPosition++
        wrongPresses += WrongPress(position = current, target = expected, pressed = note)
        if (wrongPressesAtPosition < maxWrongPressesPerNote) {
            return PressResult.Wrong(current, wrongPressesSoFar = wrongPressesAtPosition)
        }
        resolve(PositionOutcome.REVEALED, nowMillis)
        return PressResult.Revealed(current, correctNote = expected, finished = isFinished)
    }

    /** The outcome. Only valid once [isFinished]. */
    fun result(): AnswerOutcome {
        check(isFinished) { "the melody is not finished: at position $position of ${target.size}" }
        return AnswerOutcome(outcomes.toList(), wrongPresses.toList(), responseTimesMs.toList())
    }

    private fun resolve(outcome: PositionOutcome, nowMillis: Long) {
        outcomes += outcome
        // No origin means the caller never reported the keys opening; 0 ms (no human is that fast)
        // is better than a crash mid-practice or a time from some unrelated earlier moment.
        responseTimesMs += nowMillis - (positionStartMillis ?: nowMillis)
        positionStartMillis = nowMillis
        position++
        wrongPressesAtPosition = 0
    }
}
