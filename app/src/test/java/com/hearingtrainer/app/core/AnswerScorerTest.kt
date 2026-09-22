package com.hearingtrainer.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class AnswerScorerTest {

    @Test
    fun `a fully correct answer is all correct`() {
        val result = AnswerScorer.score(target = listOf(60, 62, 64), answer = listOf(60, 62, 64))
        assertTrue(result.allCorrect)
        assertEquals(listOf(true, true, true), result.perNoteCorrect)
    }

    @Test
    fun `wrong notes are flagged at their position, right notes stay correct`() {
        val result = AnswerScorer.score(target = listOf(60, 62, 64), answer = listOf(60, 63, 64))
        assertEquals(listOf(true, false, true), result.perNoteCorrect)
        assertTrue(!result.allCorrect)
    }

    @Test
    fun `a completely wrong answer is all incorrect`() {
        val result = AnswerScorer.score(target = listOf(60, 62, 64), answer = listOf(61, 63, 65))
        assertEquals(listOf(false, false, false), result.perNoteCorrect)
        assertTrue(!result.allCorrect)
    }

    @Test
    fun `rejects an answer of the wrong length`() {
        assertThrows(IllegalArgumentException::class.java) {
            AnswerScorer.score(target = listOf(60, 62, 64), answer = listOf(60, 62))
        }
    }

    @Test
    fun `rejects an empty target`() {
        assertThrows(IllegalArgumentException::class.java) {
            AnswerScorer.score(target = emptyList(), answer = emptyList())
        }
    }
}
