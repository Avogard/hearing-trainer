# Hearing Trainer — Spec (v1)

Status: draft. Edit freely; this is the source of truth for what the app does.

## The core loop (the only thing v1 must do well)

1. User opens the app and taps **Play melody**. A short melody sounds.
2. User replays it on the on-screen keyboard. Each key press sounds and
   fills in one dot of the answer strip. Nothing is scored yet: the
   user can **Replay** the melody, hear **My answer** played back at the
   same tempo, or undo the last note, as often as they like.
3. User taps **Check**. App shows which notes were right/wrong; after a
   wrong answer it plays the correct melody once more. **Next** starts
   the next melody.
4. After a fixed number of melodies (e.g. 10) the session ends with a
   score and the streak is updated. (Not built yet.)

Session target: 3–5 minutes. Usable one-handed, in portrait, no sound
settings needed.

## Screens

- **Home**: streak count, today's status, one big "Practice" button,
  a small settings icon.
- **Practice**: status line, the answer strip (dots that fill in as you
  play, plus undo), the keyboard, then two rows of buttons: Replay /
  My answer, and the big Check (or Next) button.
- **Session summary**: score, notes that were hardest, "Done" button.
  (Not built yet.)
- **Settings** (built): tempo in BPM and melody length, as big +/-
  steppers, plus "Reset to defaults". Later: reminder time on/off,
  sound on/off, practice mode (Guided ladder vs Custom), and under
  Custom: key, scale, note range, largest interval.

## Melody generator (core/, pure Kotlin)

Inputs: key (root MIDI note), scale (major, natural minor, pentatonic,
chromatic later), lowest/highest note, length in notes, largest
allowed interval in semitones, rhythm (v1: all notes equal length),
random seed.

Playback: one note per beat at the user's tempo (default 90 BPM, range
40-180); each note is released after 85% of its beat so notes stay
clearly separated. Defaults and limits live in `core/Config.kt`.

Rules: first note is the root or fifth (early levels: always the
root). No repeated note twice in a row on early levels. Stays inside
the range. Deterministic for a given seed (so bugs are reproducible
and tests are stable).

## Sound

A soft, clear piano tone with a short decay (about a second at middle
C), so consecutive notes never blur together. One bundled sample per
note, C3–C6, rendered by `tools/render_piano_samples.py`; playback goes
through the app's own mixer (`audio/MixerNotePlayer`), which times
melodies sample-accurately and fades notes out instead of cutting them.

## Keyboard

One octave visible by default, scrollable to two. White keys at least
48 dp wide. Keys not in the current scale are dimmed on early levels
(still playable on later levels). Pressing a key plays the note
immediately; the note is appended to the answer.

## Difficulty ladder (Guided mode)

Level 1: C major, range C4–G4, max interval 4 (steps and thirds — see
docs/DECISIONS.md's "Level 1 was nearly the same melody every time"; a
stricter max interval of 2 combined with the root sitting at the
bottom of the range left only 2 possible melodies). Melody length
comes from Settings: default 4 notes, range 2–8.
Level 2: range C4–C5, max interval 4.
Level 3: 5 notes, max interval 7.
Level 4: other major keys.
Level 5: natural minor.
Later: pentatonic, chromatic passing notes, rhythm, intervals-only
mode, chords.
Promotion: 80% correct over the last 20 melodies.

## Reminders

One daily local notification at a user-chosen time (default 19:00),
skipped if today's session is already done. Android 13+ needs the
POST_NOTIFICATIONS runtime permission — ask on the first Home screen
visit after the first completed session, not on launch.

## Persistence

Settings in SharedPreferences (two integers; DataStore if it ever grows
— see docs/DECISIONS.md). Progress (per-melody results, streak, level)
in Room. No account, no backend in v1.

## Out of scope for v1

Accounts, sync, monetization, iOS, external MIDI keyboards, chords,
rhythm dictation, social features.
