package com.hearingtrainer.app.core

/**
 * The result of comparing a played [Melody] against the notes the user answered with.
 *
 * @param perNoteCorrect whether each position in the answer matched the target, in order.
 * @param allCorrect true only if every position matched.
 */
data class ScoredAnswer(val perNoteCorrect: List<Boolean>, val allCorrect: Boolean)

/** Scores a completed answer against the melody that was played (see docs/SPEC.md). */
object AnswerScorer {

    /**
     * Compares [answer] to [target] position by position. Both lists must be the same length —
     * the UI only calls this once the user has played as many notes as the melody had.
     */
    fun score(target: List<Int>, answer: List<Int>): ScoredAnswer {
        require(target.isNotEmpty()) { "target must not be empty" }
        require(answer.size == target.size) {
            "answer length (${answer.size}) must match target length (${target.size})"
        }
        val perNoteCorrect = target.indices.map { i -> target[i] == answer[i] }
        return ScoredAnswer(perNoteCorrect, perNoteCorrect.all { it })
    }
}
