# Future Ideas

Backlog of things that are explicitly *not* v1. Append freely, in any
order — this is a parking lot, not a roadmap. When one of these gets
scheduled, move the relevant bit into docs/SPEC.md (and log the choice
in docs/DECISIONS.md if it changes an architecture rule).

## Input & instruments

- Guitar mode: fretboard input instead of the piano keyboard (pick a
  string, tap a fret). Same core/ melody + scoring, new ui/ input
  widget only.
- Support for other on-screen instruments (bass, simple drum pad for
  rhythm exercises).
- External MIDI keyboard input over USB/Bluetooth MIDI, for users who
  own a real keyboard.
- Sing-back mode: user sings the answer, app does pitch detection
  instead of reading key presses. Bigger effort — needs a pitch-
  tracking library and a noisy-mic fallback.

## Modes & content

- Harmony mode: app plays a chord (or short progression), user
  identifies/replays the notes or names the chord quality. Needs
  core/ chord model to grow beyond single melodic lines.
- Interval-only mode: two notes, identify the interval by name/size
  rather than replaying it (already sketched as a "later" ladder step
  in SPEC.md).
- Rhythm dictation: melody has varied note durations, user taps the
  rhythm back.
- Call-and-response / two-part melodies (app plays a short phrase,
  answer is a variation or continuation).
- More scales/modes: dorian, mixolydian, harmonic/melodic minor,
  blues scale, microtonal scales for advanced users.
- "No reference note" / perfect-pitch-style mode for advanced levels.

## Motivation & retention

- Achievements/badges beyond the daily streak (e.g. "first perfect
  session", "10 sessions in a minor key").
- Friend streaks / shared challenges, Duolingo-league style
  leaderboards. Would need a backend + accounts (currently out of
  scope, see DECISIONS.md).
- Smarter reminders: nudge time adapts to when the user actually
  practices, instead of one fixed daily time.
- Weekly/monthly recap screen (progress trends, hardest intervals).

## Monetization (post-v1)

- Play Billing for a "Pro" tier: extra scales/modes, custom instrument
  packs, detailed stats.
- Cosmetic packs (keyboard/guitar skins, sound sets) as a lighter
  monetization path than gating core practice.
- Keep the core daily practice loop free — monetize breadth (modes,
  content, cosmetics), not the thing that builds the habit.

## Platform & architecture

- iOS app sharing core/ via Kotlin Multiplatform (already a stated
  direction in DECISIONS.md — this is the concrete follow-through).
- Cloud backup/sync of progress, once there's any backend at all.
- Accessibility pass: colorblind-safe key/feedback colors, TalkBack
  support, larger touch-target mode.
- Tablet/landscape layout (v1 is one-handed phone portrait only).
