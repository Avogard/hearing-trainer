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
- Real melodies from public-domain classical music, instead of (or on
  top of) generated ones: themes from Bach, Mozart, Beethoven, folk
  tunes, etc. — the compositions are out of copyright, so they can ship
  in a commercial app as long as we type in the notes ourselves (a
  specific recording or a modern edition can still be copyrighted; the
  notes are not). Serve them as short phrases (2–8 notes) chopped from
  the theme, in the app's own key/range, and grow to whole phrases at
  higher levels. Gives the "I recognise that!" moment generated melodies
  never will, and is the natural answer to "not a huge variety of
  melodies" (see Sonofield below). Storage: a bundled JSON of
  {title, composer, notes as MIDI, durations in beats}; the same
  `Exercise` interface, just a different source of prompts.

## Practice loop & scoring

- Limit how much help is available before Check. With unlimited Replay
  and "My answer" (added 2026-09-24) it is almost impossible to get a
  melody wrong — you can compare until they match. Options to think
  about: one listen only (Replay counts as a fail / costs the streak
  point); a hard mode where "My answer" is disabled; a small number of
  free replays per session; or score by attempts (first-try correct =
  full marks, correct after a replay = partial). Whatever it is, keep
  the first-time experience forgiving and make the strict rule opt-in
  or level-gated. Owner is still deciding.
- Score only a clean run-through, like picking a tune by ear on a real
  instrument. Today one note at a time is entered, undone, and compared
  until the dots line up. Instead: the user is free to noodle on the
  keyboard (nothing is recorded), and an *attempt* is playing the whole
  melody start to finish, in order, in one go — a wrong note ends the
  attempt (feedback, then try again from the start). Only a complete
  correct pass counts as solved; the number of attempts is the score
  and feeds the difficulty ladder. Open questions: how does the app
  know an attempt has started (first key after a silence? an explicit
  "I'm ready"?), whether rhythm/tempo of the attempt matters at all,
  and how this combines with the replay limits above. Goal for both
  ideas: as close as possible to real picking by ear.

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

## Competitors to learn from

### Sonofield Ear Trainer (closest to what this app wants to be — explore it)

What it is (checked 2026-09-24): iOS/Android/macOS, 4.8–4.9 stars, 10K+
Android installs, free with no ads, one-time "Pro" purchase (~$15–25,
not a subscription — users praise this). Method: a constant drone on the
tonic; you learn scale degrees (1–7, then chromatic) by how each one
*feels* in the key, explicitly not by intervals ("thinking intervals is
too slow for real music"). Modes: Degrees (one note over the drone),
Melodies (short phrases), Voice (sing the asked degree, app checks
tuning), Pocket (hands-free, for commuting), Free Play. A guided "Path"
introduces degrees one at a time. Color-coded circle-of-fifths UI. The
"realistic instrument sound pack" is a Pro feature.

Where it is weak (reviews and critiques):
- Melodies are its thinnest part: "simplistic", "not a huge variety",
  and the answer is *which degree* — identification, not performance.
- No chords, no rhythm, no notation; described as beginner-focused.
- The drone method confuses beginners at first; one teacher reported it
  dented a beginner's confidence.
- UI complaints: unclear what to do after a wrong answer; dark mode
  visibility; users asking for stats.

How to be better, or different:
- Answer on an instrument, not with degree buttons. Replaying on the
  keyboard is already closer to real picking-by-ear than anything
  Sonofield does — make that the promise: "you can play what you hear",
  not "you can name the degree". The clean run-through scoring idea
  above is the same thing taken seriously.
- Steal the one thing they got right: tonal context. Establish the key
  before each melody — a soft I–IV–V–I cadence or a quiet tonic drone
  (per-level option). Cheap to build (it's just more notes through the
  mixer), big for functional hearing.
- Melody variety is our moat. The generator is already parametric (key,
  scale, range, interval, length, seed); add rhythm, wider ranges,
  minor/pentatonic, song-like contours, later real melodies. This is
  exactly the part reviewers fault Sonofield for.
- Optional scale-degree labels on the keys (1–7, colored by function),
  key-agnostic, so the "degrees" idea and the instrument idea combine.
- Keep sound quality best-in-class *in the free tier* — Sonofield sells
  it as an upgrade, Chet is praised mainly for it.
- Pricing: match "no ads, one-time Pro". Subscription apps (Functional
  Ear Trainer, ToneGym) get criticized for it in the same reviews.
- Unique-problem candidates: (a) play-by-ear on *your* instrument
  (guitar fretboard input, see above) — Sonofield is instrument-agnostic
  identification; (b) transcribe real snippets; (c) the bridge from
  hearing to playing, which none of the identification apps cover.

Before deciding anything, install it and note: the first five minutes,
how the Path paces new degrees, what answering in Melodies mode actually
looks like, and how wrong answers are handled.

Other apps named in the same reviews, for a later look: Functional Ear
Trainer (same idea, older, subscription), Chet (iOS, real-song snippets,
great sound, free), Perfect Ear (broad, Android), Complete Ear Trainer,
EarMaster, ToneGym (web), Earpeggio (iOS, melodic contour exercise).
