package com.hearingtrainer.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class ScoredAnswerTest {

    private val target = listOf(60, 62, 64, 65) // C4 D4 E4 F4

    private fun answer(maxReplays: Int = 2, maxWrong: Int = 2) =
        ScoredAnswer(target, maxReplays = maxReplays, maxWrongPressesPerNote = maxWrong)

    @Test
    fun `all first try is clean, every position FIRST_TRY, no wrong presses`() {
        val answer = answer()
        answer.inputOpened(1000)
        assertEquals(PressResult.Correct(0, finished = false), answer.press(60, 1500))
        assertEquals(PressResult.Correct(1, finished = false), answer.press(62, 2000))
        assertEquals(PressResult.Correct(2, finished = false), answer.press(64, 2500))
        assertEquals(PressResult.Correct(3, finished = true), answer.press(65, 3000))
        assertTrue(answer.isFinished)

        val outcome = answer.result()
        assertEquals(List(4) { PositionOutcome.FIRST_TRY }, outcome.positions)
        assertEquals(4, outcome.firstTryCount)
        assertEquals(4, outcome.total)
        assertTrue(outcome.clean)
        assertTrue(outcome.wrongPresses.isEmpty())
        assertTrue(outcome.feedbackLines().isEmpty())
    }

    @Test
    fun `wrong then correct at one position is FOUND and stays at that position in between`() {
        val answer = answer()
        answer.inputOpened(0)
        assertEquals(PressResult.Correct(0, finished = false), answer.press(60, 10))
        assertEquals(PressResult.Wrong(1, wrongPressesSoFar = 1), answer.press(64, 20))
        assertEquals(1, answer.position)
        assertEquals(1, answer.wrongPressesAtPosition)
        assertEquals(PressResult.Correct(1, finished = false), answer.press(62, 30))
        assertEquals(2, answer.position)
        assertEquals(0, answer.wrongPressesAtPosition)
        assertEquals(listOf(PositionOutcome.FIRST_TRY, PositionOutcome.FOUND), answer.resolvedOutcomes)
        answer.press(64, 40)
        answer.press(65, 50)

        val outcome = answer.result()
        assertEquals(
            listOf(PositionOutcome.FIRST_TRY, PositionOutcome.FOUND, PositionOutcome.FIRST_TRY, PositionOutcome.FIRST_TRY),
            outcome.positions,
        )
        assertEquals(3, outcome.firstTryCount)
        assertFalse(outcome.clean)
        assertEquals(listOf(WrongPress(position = 1, target = 62, pressed = 64)), outcome.wrongPresses)
        assertEquals(listOf("2nd note: you played E4, it was D4"), outcome.feedbackLines())
    }

    @Test
    fun `two wrong presses reveal the note and advance`() {
        val answer = answer(maxWrong = 2)
        answer.inputOpened(0)
        answer.press(60, 10)
        assertEquals(PressResult.Wrong(1, wrongPressesSoFar = 1), answer.press(60, 20))
        assertEquals(PressResult.Revealed(1, correctNote = 62, finished = false), answer.press(64, 30))
        assertEquals(2, answer.position)
        answer.press(64, 40)
        answer.press(65, 50)

        val outcome = answer.result()
        assertEquals(PositionOutcome.REVEALED, outcome.positions[1])
        assertEquals(
            listOf(WrongPress(1, target = 62, pressed = 60), WrongPress(1, target = 62, pressed = 64)),
            outcome.wrongPresses,
        )
        assertEquals(listOf("2nd note: you played C4 and E4, it was D4"), outcome.feedbackLines())
    }

    @Test
    fun `a single allowed wrong press reveals immediately`() {
        val answer = answer(maxWrong = 1)
        assertEquals(PressResult.Revealed(0, correctNote = 60, finished = false), answer.press(61, 0))
    }

    @Test
    fun `finished flag is set only by the press that resolves the last position`() {
        val answer = answer(maxWrong = 2)
        answer.press(60, 0)
        answer.press(62, 0)
        answer.press(64, 0)
        assertFalse(answer.isFinished)
        assertEquals(PressResult.Wrong(3, wrongPressesSoFar = 1), answer.press(60, 0))
        assertFalse(answer.isFinished)
        assertEquals(PressResult.Revealed(3, correctNote = 65, finished = true), answer.press(60, 0))
        assertTrue(answer.isFinished)
        assertThrows(IllegalStateException::class.java) { answer.press(65, 0) }
    }

    @Test
    fun `result is refused before the melody is finished`() {
        val answer = answer()
        answer.press(60, 0)
        assertThrows(IllegalStateException::class.java) { answer.result() }
    }

    @Test
    fun `response times run from playback end, then from each resolved position`() {
        val answer = answer(maxWrong = 2)
        answer.inputOpened(1000)
        answer.press(60, 1400) // first try: 400 ms after the melody ended
        answer.press(64, 1500) // wrong
        answer.press(62, 2100) // found: 700 ms after position 0 resolved (wrong presses don't reset it)
        answer.press(61, 2200) // wrong
        answer.press(63, 2300) // wrong -> revealed: 200 ms after position 1 resolved
        answer.press(65, 2350) // first try: 50 ms after the reveal
        assertEquals(listOf(400L, 700L, 200L, 50L), answer.result().responseTimesMs)
    }

    @Test
    fun `the keys opening again resets the current position's clock (replay end, reveal end)`() {
        val answer = answer(maxWrong = 2)
        answer.inputOpened(1000)
        answer.noteReplay()
        answer.inputOpened(5000) // the replay ended: position 0 counts from here
        answer.press(60, 5250) // 250
        answer.press(61, 5300) // wrong
        answer.press(63, 5400) // wrong -> revealed: 150 after position 0 resolved
        answer.inputOpened(6100) // the reveal's sound and highlight are over; keys available again
        answer.press(64, 6400) // 300, not 1000: the lockout during the reveal isn't thinking time
        answer.press(65, 6500) // 100
        assertEquals(listOf(250L, 150L, 300L, 100L), answer.result().responseTimesMs)
        assertEquals(4, answer.result().positions.size)
    }

    @Test
    fun `without a reported input opening the first response time is 0, never a stale time`() {
        val answer = answer()
        answer.press(60, 7000)
        answer.press(62, 7300)
        answer.press(64, 7400)
        answer.press(65, 7900)
        assertEquals(listOf(0L, 300L, 100L, 500L), answer.result().responseTimesMs)
    }

    @Test
    fun `replay is allowed before the first press, up to the limit`() {
        val answer = answer(maxReplays = 2)
        assertTrue(answer.canReplay())
        assertEquals(2, answer.replaysLeft)
        answer.noteReplay()
        assertTrue(answer.canReplay())
        assertEquals(1, answer.replaysLeft)
        answer.noteReplay()
        assertFalse(answer.canReplay())
        assertEquals(0, answer.replaysLeft)
        assertEquals(2, answer.replaysUsed)
        assertThrows(IllegalStateException::class.java) { answer.noteReplay() }
    }

    @Test
    fun `replay is refused after the first press, even a wrong one`() {
        val answer = answer(maxReplays = 2)
        answer.press(61, 0) // wrong
        assertFalse(answer.canReplay())
        assertThrows(IllegalStateException::class.java) { answer.noteReplay() }
        assertEquals(0, answer.replaysUsed)
    }

    @Test
    fun `zero replays means none are allowed`() {
        assertFalse(answer(maxReplays = 0).canReplay())
    }

    @Test
    fun `outcome counts and wrong-press list are exact across a mixed melody`() {
        val answer = answer(maxWrong = 2)
        answer.press(62, 0) // pos 0 wrong
        answer.press(60, 0) // pos 0 found
        answer.press(62, 0) // pos 1 first try
        answer.press(65, 0) // pos 2 wrong
        answer.press(67, 0) // pos 2 wrong -> revealed (64)
        answer.press(65, 0) // pos 3 first try

        val outcome = answer.result()
        assertEquals(
            listOf(PositionOutcome.FOUND, PositionOutcome.FIRST_TRY, PositionOutcome.REVEALED, PositionOutcome.FIRST_TRY),
            outcome.positions,
        )
        assertEquals(2, outcome.firstTryCount)
        assertEquals(4, outcome.total)
        assertEquals(
            listOf(WrongPress(0, 60, 62), WrongPress(2, 64, 65), WrongPress(2, 64, 67)),
            outcome.wrongPresses,
        )
        assertEquals(
            listOf("1st note: you played D4, it was C4", "3rd note: you played F4 and G4, it was E4"),
            outcome.feedbackLines(),
        )
    }

    @Test
    fun `feedback lines skip positions that carry no recorded press instead of crashing`() {
        val outcome = AnswerOutcome(
            positions = listOf(PositionOutcome.CORRECT, PositionOutcome.WRONG, PositionOutcome.REVEALED, PositionOutcome.FOUND),
            wrongPresses = listOf(WrongPress(3, 65, 64)),
            responseTimesMs = emptyList(),
        )
        assertEquals(listOf("4th note: you played E4, it was F4"), outcome.feedbackLines())
    }

    @Test
    fun `ordinals`() {
        assertEquals(listOf("1st", "2nd", "3rd", "4th", "8th", "11th", "12th", "13th", "21st", "22nd", "23rd", "101st", "111th"),
            listOf(1, 2, 3, 4, 8, 11, 12, 13, 21, 22, 23, 101, 111).map { AnswerOutcome.ordinal(it) })
    }

    @Test
    fun `rejects an empty target and nonsensical limits`() {
        assertThrows(IllegalArgumentException::class.java) { ScoredAnswer(emptyList(), 2, 2) }
        assertThrows(IllegalArgumentException::class.java) { ScoredAnswer(target, maxReplays = -1, maxWrongPressesPerNote = 2) }
        assertThrows(IllegalArgumentException::class.java) { ScoredAnswer(target, maxReplays = 2, maxWrongPressesPerNote = 0) }
    }
}
