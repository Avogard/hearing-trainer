package com.hearingtrainer.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The writer has a fixed field order and deterministic escaping, so exact strings are the
 * plainest oracle (org.json is only a stub in local unit tests, and a parser would just repeat the
 * same beliefs about JSON). The expected strings below were checked to be valid JSON by hand.
 */
class AttemptRecordTest {

    private val scoredRecord = AttemptRecord(
        timestampMillis = 1_758_800_000_123L,
        mode = AnswerMode.SCORED,
        keyRoot = 60,
        melody = listOf(60, 62, 64, 65),
        tempoBpm = 90,
        cadencePlayed = true,
        replaysUsed = 1,
        positions = listOf(PositionOutcome.FIRST_TRY, PositionOutcome.FOUND, PositionOutcome.REVEALED, PositionOutcome.FIRST_TRY),
        wrongPresses = listOf(WrongPress(1, 62, 64), WrongPress(2, 64, 65), WrongPress(2, 64, 67)),
        responseTimesMs = listOf(400, 700, 200, 50),
        firstTryCount = 2,
        total = 4,
        silentKeyboard = false,
    )

    @Test
    fun `a scored record is one line of JSON with every field, in order`() {
        assertEquals(
            """{"timestampMillis":1758800000123,"mode":"scored","keyRoot":60,"melody":[60,62,64,65],""" +
                """"tempoBpm":90,"cadencePlayed":true,"replaysUsed":1,""" +
                """"positions":["first_try","found","revealed","first_try"],""" +
                """"wrongPresses":[{"position":1,"target":62,"pressed":64},{"position":2,"target":64,"pressed":65},""" +
                """{"position":2,"target":64,"pressed":67}],""" +
                """"responseTimesMs":[400,700,200,50],"firstTryCount":2,"total":4,"silentKeyboard":false}""",
            scoredRecord.toJson(),
        )
        assertFalse("must be a single line", scoredRecord.toJson().contains('\n'))
    }

    @Test
    fun `a free-play record marks the mode, uses correct or wrong per position and counts no first tries`() {
        val record = AttemptRecord.free(
            timestampMillis = 5L,
            keyRoot = 60,
            melody = listOf(60, 62, 64),
            answer = listOf(60, 63, 64),
            tempoBpm = 100,
            cadencePlayed = false,
            replaysUsed = 3,
        )
        assertEquals(AnswerMode.FREE, record.mode)
        assertEquals(listOf(PositionOutcome.CORRECT, PositionOutcome.WRONG, PositionOutcome.CORRECT), record.positions)
        assertEquals(listOf(WrongPress(1, 62, 63)), record.wrongPresses)
        assertEquals(0, record.firstTryCount)
        assertEquals(3, record.total)
        assertTrue(record.responseTimesMs.isEmpty())
        assertEquals(
            """{"timestampMillis":5,"mode":"free","keyRoot":60,"melody":[60,62,64],"tempoBpm":100,""" +
                """"cadencePlayed":false,"replaysUsed":3,"positions":["correct","wrong","correct"],""" +
                """"wrongPresses":[{"position":1,"target":62,"pressed":63}],"responseTimesMs":[],""" +
                """"firstTryCount":0,"total":3,"silentKeyboard":false}""",
            record.toJson(),
        )
    }

    @Test
    fun `a scored record built from an outcome copies its counts`() {
        val outcome = AnswerOutcome(
            positions = listOf(PositionOutcome.FIRST_TRY, PositionOutcome.REVEALED),
            wrongPresses = listOf(WrongPress(1, 62, 60), WrongPress(1, 62, 64)),
            responseTimesMs = listOf(100, 900),
        )
        val record = AttemptRecord.scored(
            timestampMillis = 1L, keyRoot = 60, melody = listOf(60, 62), tempoBpm = 90,
            cadencePlayed = true, replaysUsed = 0, outcome = outcome, silentKeyboard = true,
        )
        assertEquals(1, record.firstTryCount)
        assertEquals(2, record.total)
        assertEquals(outcome.positions, record.positions)
        assertEquals(outcome.wrongPresses, record.wrongPresses)
        assertEquals(outcome.responseTimesMs, record.responseTimesMs)
        assertTrue(record.toJson().endsWith(""""silentKeyboard":true}"""))
    }

    @Test
    fun `plain strings are just quoted`() {
        assertEquals("\"C4\"", Json.string("C4"))
        assertEquals("\"\"", Json.string(""))
        assertEquals("\"é 😀 /\"", Json.string("é 😀 /")) // non-ASCII and slashes need no escaping
    }

    @Test
    fun `quotes, backslashes and control characters are escaped so the line stays one valid JSON line`() {
        assertEquals("\"say \\\"hi\\\"\"", Json.string("say \"hi\""))
        assertEquals("\"a\\\\b\"", Json.string("a\\b"))
        assertEquals("\"a\\nb\\tc\\rd\"", Json.string("a\nb\tc\rd"))
        assertEquals("\"\\b\\f\"", Json.string("\b\u000C"))
        assertEquals("\"\\u0001\\u001f\"", Json.string("\u0001\u001F"))
        assertEquals("\"\\u2028\\u2029\"", Json.string("\u2028\u2029")) // Unicode line separators would split a .jsonl line
        val nasty = "a \"quoted\" \\ back\\slash\nnew line\ttab \u0001 end"
        val json = Json.obj("v" to Json.string(nasty))
        assertFalse(json.contains('\n'))
        assertEquals("""{"v":"a \"quoted\" \\ back\\slash\nnew line\ttab \u0001 end"}""", json)
    }

    @Test
    fun `arrays and objects encode as expected`() {
        assertEquals("[]", Json.numberArray(emptyList()))
        assertEquals("[1,2,3]", Json.numberArray(listOf(1, 2, 3)))
        assertEquals("[\"a\",\"b\"]", Json.stringArray(listOf("a", "b")))
        assertEquals("{}", Json.obj())
        assertEquals("{\"a\":1,\"b\":\"x\"}", Json.obj("a" to "1", "b" to Json.string("x")))
        assertEquals("{\"key \\\"k\\\"\":true}", Json.obj("key \"k\"" to "true"))
    }
}
