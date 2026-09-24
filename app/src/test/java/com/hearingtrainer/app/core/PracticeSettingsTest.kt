package com.hearingtrainer.app.core

import org.junit.Assert.assertEquals
import org.junit.Test

class PracticeSettingsTest {

    @Test
    fun `defaults are 90 BPM and 4 notes`() {
        assertEquals(90, PracticeSettings.DEFAULT.tempoBpm)
        assertEquals(4, PracticeSettings.DEFAULT.melodyLength)
    }

    @Test
    fun `clamped forces values into the allowed ranges`() {
        assertEquals(
            PracticeSettings(tempoBpm = Config.MIN_TEMPO_BPM, melodyLength = Config.MAX_MELODY_LENGTH),
            PracticeSettings(tempoBpm = 1, melodyLength = 99).clamped(),
        )
        assertEquals(
            PracticeSettings(tempoBpm = Config.MAX_TEMPO_BPM, melodyLength = Config.MIN_MELODY_LENGTH),
            PracticeSettings(tempoBpm = 999, melodyLength = 0).clamped(),
        )
    }

    @Test
    fun `clamped leaves in-range values alone`() {
        val settings = PracticeSettings(tempoBpm = 120, melodyLength = 6)
        assertEquals(settings, settings.clamped())
    }

    @Test
    fun `in-memory store round-trips and clamps`() {
        val store = InMemorySettingsStore()
        assertEquals(PracticeSettings.DEFAULT, store.load())
        store.save(PracticeSettings(tempoBpm = 500, melodyLength = 5))
        assertEquals(PracticeSettings(tempoBpm = Config.MAX_TEMPO_BPM, melodyLength = 5), store.load())
    }
}
