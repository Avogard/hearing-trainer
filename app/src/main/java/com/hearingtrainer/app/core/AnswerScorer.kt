package com.hearingtrainer.app.core

/**
 * The result of comparing a played [Melody] against the notes the user answered with.
 *
 * @param perNoteCorrect whether each position in the answer matched the target, in order.
 * @param allCorrect true only if every position matched.
 */
data class AnswerScore(val perNoteCorrect: List<Boolean>, val allCorrect: Boolean)

/**
 * Scores a completed answer against the melody that was played, all at once: this is free play's
 * Check button (docs/SPEC.md, "Answer modes"). Scored mode judges press by press in [ScoredAnswer].
 */
object AnswerScorer {

    /**
     * Compares [answer] to [target] position by position. Both lists must be the same length —
     * the UI only calls this once the user has played as many notes as the melody had.
     */
    fun score(target: List<Int>, answer: List<Int>): AnswerScore {
        require(target.isNotEmpty()) { "target must not be empty" }
        require(answer.size == target.size) {
            "answer length (${answer.size}) must match target length (${target.size})"
        }
        val perNoteCorrect = target.indices.map { i -> target[i] == answer[i] }
        return AnswerScore(perNoteCorrect, perNoteCorrect.all { it })
    }
}
