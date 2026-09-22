# Hearing Trainer — Spec (v1)

Status: draft. Edit freely; this is the source of truth for what the app does.

## The core loop (the only thing v1 must do well)

1. User opens the app and taps **Play**. A short melody sounds.
2. User replays it on the on-screen keyboard. Each key press sounds.
3. App shows which notes were right/wrong, plays the correct melody
   once more, and offers **Next** (or **Retry**).
4. After a fixed number of melodies (e.g. 10) the session ends with a
   score and the streak is updated.

Session target: 3–5 minutes. Usable one-handed, in portrait, no sound
settings needed.

## Screens

- **Home**: streak count, today's status, one big "Practice" button,
  a small settings icon.
- **Practice**: the melody player (Play / Replay), the keyboard, the
  answer strip (dots that fill in as you play), feedback state.
- **Session summary**: score, notes that were hardest, "Done" button.
- **Settings**: reminder time on/off, sound on/off, practice mode
  (Guided ladder vs Custom), and under Custom: key, scale, note range,
  melody length, largest interval, tempo.

## Melody generator (core/, pure Kotlin)

Inputs: key (root MIDI note), scale (major, natural minor, pentatonic,
chromatic later), lowest/highest note, length in notes, largest
allowed interval in semitones, rhythm (v1: all notes equal length),
random seed.

Rules: first note is the root or fifth (early levels: always the
root). No repeated note twice in a row on early levels. Stays inside
the range. Deterministic for a given seed (so bugs are reproducible
and tests are stable).

## Keyboard

One octave visible by default, scrollable to two. White keys at least
48 dp wide. Keys not in the current scale are dimmed on early levels
(still playable on later levels). Pressing a key plays the note
immediately; the note is appended to the answer.

## Difficulty ladder (Guided mode)

Level 1: C major, 3 notes, range C4–G4, steps only (max interval 2).
Level 2: 4 notes, range C4–C5, max interval 4.
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

Settings in DataStore. Progress (per-melody results, streak, level) in
Room. No account, no backend in v1.

## Out of scope for v1

Accounts, sync, monetization, iOS, external MIDI keyboards, chords,
rhythm dictation, social features.
