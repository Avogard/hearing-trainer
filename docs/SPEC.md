# Hearing Trainer — Spec (v1)

Status: draft. Edit freely; this is the source of truth for what the app does.

## The core loop (the only thing v1 must do well)

1. User opens the app and taps **Play melody**. A short melody sounds.
2. User plays it back on the on-screen keyboard. In **Scored** mode
   (the default) every key press is final: a right note turns its dot
   green and the next position opens; a wrong note turns the dot red
   and the same position stays open; after two wrong presses the app
   reveals the note (lights the key, plays it) and moves on. The melody
   can be replayed twice, only before the first press. In **Free play**
   each press just fills a dot, and **Replay**, **My answer** and undo
   are unlimited until **Check**. See "Answer modes" below.
3. When the last position resolves (Scored) or the user taps **Check**
   (Free play), the app shows which notes were right, found after wrong
   presses, or revealed / wrong. If anything wasn't right first time it
   plays the correct melody once more and, in Scored mode, names each
   miss ("3rd note: you played E4, it was F4"). **Next** starts the
   next melody.
4. After a fixed number of melodies (e.g. 10) the session ends with a
   score and the streak is updated. (Not built yet.)

Session target: 3–5 minutes. Usable one-handed, in portrait, no sound
settings needed.

## Screens

- **Home**: streak count, today's status, one big "Practice" button,
  a small settings icon.
- **Practice**: the Scored / Free play toggle, status line, the answer
  strip (one dot per note), the keyboard, then two rows of buttons.
  Scored: Replay (n left) and the big Next button once the melody is
  done. Free play: Replay / My answer, and the big Check (or Next)
  button; undo sits at the end of the strip.
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

## Answer modes

The Practice screen has a one-tap toggle, **Scored** / **Free play**,
above the strip. It switches between melodies (it is disabled while
one is being answered) and applies from the next melody on.

**Scored** (default) tests hearing the note before pressing it:

- Every key press is the answer for the current position. No undo, no
  My answer, no Check: the melody finishes itself on the last position.
- The open position's dot is outlined. Right: it turns green (amber if
  there were wrong presses at that position first) and the next
  position opens. Wrong: it turns red, the press is recorded, and the
  same position stays open. After
  `MAX_WRONG_PRESSES_PER_NOTE` (2) wrong presses the app reveals the
  note: a moment after the wrong key sounds, the correct key lights for
  about 400 ms while its note plays; the dot stays red (revealed) and
  the next position opens.
- Replay is allowed `MAX_REPLAYS_BEFORE_FIRST_PRESS` (2) times, only
  before the first key press; the button shows how many are left and
  is disabled from the first press on.
- Per position the outcome is first try, found after wrong presses, or
  revealed. The score is first-try notes over all notes; a melody is
  clean when every note was first try. After the last position, if
  anything wasn't first try, the melody plays once more (after a beat
  of silence, so the last pressed note isn't cut off) and each miss is
  listed under the strip: "3rd note: you played E4, it was F4" (or
  "you played E4 and D4" after two wrong presses). Feedback is about
  the music: no praise, no penalty language.
- Response time per position runs from the moment the keys became
  available for it (the end of playback, the previous position
  resolving, the end of a reveal) to the press that resolved it.
- Keys still sound (the keyboard stays an instrument);
  `Features.SILENT_SCORED_KEYBOARD` makes them silent while answering,
  for dictation. No UI for it yet.

**Free play** is the flow from before scored mode existed: keys sound
and fill the strip, Replay / My answer / undo without limit, an
explicit Check, then Next. Free-play results never count toward any
score or statistic.

Flags in `core/Features.kt`: `SCORED_ANSWER_MODE`,
`SILENT_SCORED_KEYBOARD`. Limits in `core/Config.kt`.

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
immediately. In Scored mode the press is the answer for the current
position and cannot be taken back; when a note is revealed the correct
key lights up for a moment. In Free play the note is appended to the
answer (undo removes the last one).

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
