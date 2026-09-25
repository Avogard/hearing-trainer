# Decisions

Append-only log. Newest at the bottom. One entry per decision, with the
reason, so nobody (human or Claude) re-litigates it later.

## 2026-09-22 — Native Kotlin + Jetpack Compose, not Flutter

Audio latency and MIDI support are first-class on native Android and
second-class through Flutter plugins. The owner is a C++ developer, so
Kotlin is a small step and the NDK (Oboe) is available if latency ever
needs it. iOS later: share `core/` via Kotlin Multiplatform.

## 2026-09-22 — `core/` is pure Kotlin with no Android imports

Music theory, generator and scoring are the part worth testing and the
part that would move to iOS. Keeping Android out of it makes both easy.

## 2026-09-22 — Sampled piano via SoundPool first, Oboe only if needed

Bundling a small set of piano samples (one WAV per note, or per few
semitones with pitch shifting) and playing them with SoundPool is a few
dozen lines and good enough for a dictation trainer. A software
synthesizer (FluidSynth via NDK) is a real option later but adds a
native build to a first Android project. Measure first.

## 2026-09-22 — No backend, no accounts in v1

Reminders are local notifications; progress is on-device. This removes
a privacy policy for user data, a server bill and a login screen from
the first version. A backend only becomes necessary for sync or
monetization beyond simple Play Billing.

## 2026-09-22 — Good defaults over customisation

Guided mode is the default and shows no options. Custom mode exists for
people who want it. "Easy to use" was the top requirement.

## 2026-09-22 — Let Android Studio's wizard generate the initial Gradle project, don't hand-roll it

Claude's cloud workspace can't verify a hand-written Gradle/AGP setup: outbound access to
Google's Maven repo and Maven Central is blocked by org policy (no network to fetch AGP,
androidx or Compose deps), and the Android Gradle Plugin just jumped to a new major version
(9.4.0, requiring Gradle 9.6+) with breaking changes from 8.x. Rather than guess a version
matrix that can't be tested, the project's Gradle skeleton (settings.gradle.kts,
app/build.gradle.kts, the wrapper, AndroidManifest.xml, MainActivity, Compose theme) is
created once via Android Studio's own "New Project" template, which guarantees a version
combination that actually works for whatever Android Studio version is installed. Claude adds
everything else — core/, audio/, ui/, data/ — on top of that generated skeleton.

Settings used: Empty Activity template, Compose, Kotlin, package name
`com.hearingtrainer.app`, min SDK 26, save location = this repo's root (not a nested
subfolder).

Watch out for: the wizard's template grid also has a "No Activity" option right next to
"Empty Activity" — easy to click by mistake. It generates a project with no Kotlin plugin, no
Compose, and no launcher Activity (builds fine, just has nothing to run). If `Run` fails with
"Default Activity not found" and the manifest has no `<activity>`, that's what happened —
redo the wizard rather than trying to patch a "No Activity" project into a Compose one by
hand.

## 2026-09-22 — core/ logic gets verified offline before it ever needs Android Studio

core/ is pure Kotlin (see the decision above), so it can be compiled and unit-tested without
the Android SDK at all — using the Kotlin compiler and JUnit jars bundled inside any local
Gradle installation, entirely offline. `tools/verify-core.sh` does this. It's a fast inner
loop for core/ changes; `./gradlew testDebugUnitTest` (needs the real Android project +
network) is still the real, final check before calling a core/ change done, since only that
build also compiles the Android-facing code and matches what CI/the app actually ships.

## 2026-09-22 — Working AGP 9.4.1 + Compose config, confirmed from the actual generated project

For reference, since AGP 9 changed some DSL (e.g. `compileSdk { version = release(37) }`
instead of the old `compileSdk = 37` assignment): this project's real, wizard-generated
`app/build.gradle.kts` applies `alias(libs.plugins.android.application)` and
`alias(libs.plugins.kotlin.compose)`, sets `buildFeatures { compose = true }` (unchanged from
older AGP), and pulls Compose deps through `platform(libs.androidx.compose.bom)`. Kotlin/AGP
plugin versions live in `gradle/libs.versions.toml`. Any future hand-edit to build files
should match these actually-working patterns rather than older or docs-sourced ones, since
AGP 9's syntax isn't fully reflected in public docs yet.

## 2026-09-22 — core/ melody generator folded into the real project

`app/src/main/java/com/hearingtrainer/app/core/` (Scale, MidiNote, Melody, MelodySpec,
MelodyGenerator) and the matching tests under `app/src/test/.../core/` are in. Next: run
`./gradlew testDebugUnitTest` on-device to confirm the real AGP build agrees with the offline
verification (`tools/verify-core.sh`), then start on `ui/` (the Home and Practice screens) and
`audio/` (SoundPool playback).
## 2026-09-23 — Synthesized piano samples instead of real recordings

Real piano sample packs would need downloading from the internet, which Claude's cloud
workspace can't do (org policy blocks the relevant hosts) and Claude doesn't have a shell on
the user's machine to fetch them there either. Instead, `res/raw/piano_c3.wav`,
`piano_c4.wav`, and `piano_c5.wav` are synthesized offline (stdlib-only Python: a few additive
harmonics, fast attack, exponential decay, a touch of onset noise) — not a real piano
recording, but pitch-accurate, license-free, and good enough for an ear-training app where
recognizing pitch is the point, not timbral realism. `SoundPoolNotePlayer` pitch-shifts these
three octave-apart samples to cover every note SoundPool's 0.5x-2x rate range reaches. Swapping
in real recorded samples later is a drop-in change — same three filenames, same pitch-shift
scheme — whenever real samples are available.

## 2026-09-23 — No navigation library yet for Home <-> Practice

Two screens in one Activity doesn't need Navigation Compose — `MainActivity`'s `AppRoot` just
holds a `remember`ed boolean and shows one screen or the other. Revisit this once Session
Summary and Settings exist and the back-stack behavior between them actually matters.

## 2026-09-23 — Added androidx.lifecycle:lifecycle-viewmodel-compose

Needed for `ViewModel` and the `viewModel()` composable factory function, per CLAUDE.md's "one
ViewModel per screen" rule — the wizard's default dependencies didn't include it (only
`lifecycle-runtime-ktx`). Pinned to the same version as `lifecycle-runtime-ktx` (2.6.1) since
androidx.lifecycle artifacts release in lockstep; not managed by the Compose BOM, which doesn't
cover this group.

## 2026-09-23 — First playable core loop: ui/practice, ui/keyboard, ui/home, audio/

`PracticeViewModel` + `PracticeScreen` + `PianoKeyboard` implement docs/SPEC.md's core loop for
Difficulty Level 1 only (hardcoded via `core/DifficultyLevel.level1`): tap Play, hear a 3-note
C-major melody, play it back on a one-octave on-screen keyboard, see per-note right/wrong
feedback, tap Next for another one. `HomeScreen` is a placeholder with just a Practice button —
streak and "done today" need `data/` (Room/DataStore), not built yet. `AnswerScorer` (core/, 5
tests) does the right/wrong comparison.

## 2026-09-23 — Fixed a stray duplicate MainActivity.kt and a bad Compose import blocking the build

Two unrelated build breaks turned up while getting this first version to actually compile:
a stray `MainActivity-1.kt` (an accidental duplicate of `MainActivity.kt` from an in-progress
later batch) caused a duplicate-class error and was emptied out; and `PracticeScreen.kt` had
an explicit `import androidx.compose.foundation.layout.weight`, which isn't needed —
`Modifier.weight()` inside a `Column`/`Row` resolves automatically as a member of
`ColumnScope`/`RowScope` — and was instead binding to an unrelated internal library symbol of
the same name, breaking compilation with "it is internal in file". Removed the import; no
behavior change.

## 2026-09-23 — Level 1 was nearly the same melody every time: `maxInterval` fixed from 2 to 4 semitones

Level 1's range (C4-G4) puts the root, C4, at the exact bottom edge. With `maxInterval = 2`
semitones and "always starts on the root", C4's only in-range, in-scale note within 2
semitones is D4 (E4 is 4 semitones away) — so the first two notes of every melody were forced
to C4-D4 every single time, and only the third note varied (between C4 and E4). That's 2
possible melodies total, which is what "melodies are almost always the same" was — not a
seeding bug (each melody does use a fresh random seed), just an over-constrained parameter
combination.

Raised `maxInterval` to 4 semitones (letting the generator use thirds, not just steps) rather
than changing the range or the "always starts on root" rule, since both of those are explicit,
deliberate parts of docs/SPEC.md and changing the range would also require reworking the
Practice screen's hardcoded one-octave keyboard window. With `maxInterval = 4` the same range
and starting rule produce 7 distinct melodies instead of 2 (verified by simulating all 200
seeds 0-199, both before and after). `DifficultyLevelTest` (`core/`, 2 new tests) pins this
down as a regression test: across 200 seeds, Level 1 must produce at least 5 distinct melodies.
docs/SPEC.md's difficulty ladder section updated to match.

## 2026-09-24 — Per-note rendered samples + an AudioTrack mixer, instead of SoundPool + pitch shifting

The first sound was "weird" for four concrete reasons, all visible in the files: the three
synthesized samples were organ-like (nearly flat for 250 ms, still only -13 dB after a full
second), then cut off abruptly at 1.6 s while still audible (a click); SoundPool stretched each
one up to 6 semitones, which changes the timbre and adds resampling artifacts; melody timing
came from `delay()` on the UI thread, so the rhythm jittered by 10-20 ms; and notes were never
released, so they piled up on each other.

Now: `tools/render_piano_samples.py` renders one WAV per MIDI note, C3-C6 (37 files, ~3.6 MB,
48 kHz mono 16-bit) from a physically-informed piano model — inharmonic partials, a hammer-
position comb, a soft-hammer low-pass, two-stage decay per partial, 2-3 detuned unison strings,
a bandlimited soundboard knock, a little room. Nothing is pitch-shifted at runtime. The tone is
deliberately soft, clear and short (middle C is 40 dB down within 0.6 s), tuned for pitch
recognition rather than realism; presets in the script (`soft`, `bright`, `dry`) make a
different taste one command away. Real recordings, if they ever become available, are a drop-in
replacement: same file names, same format, no code change.

`audio/MixerNotePlayer` replaces SoundPool: one audio thread mixes voices into 5 ms blocks and
feeds `AudioTrack` (float PCM, low-latency mode). It's the standard "audio callback" shape, about
200 lines, and gives what SoundPool can't: sample-accurate scheduling of a whole melody
(`NotePlayer.playSequence`), a 60 ms release fade on note-off instead of a hard stop, voice
stealing at 8 voices, and a soft clipper so overlapping notes compress instead of crackling.
`core/MelodyTiming` (pure Kotlin, tested) turns notes + BPM into that schedule; the ViewModel
only waits for the total duration to flip the UI back. Oboe/NDK remains the escape hatch if
touch-to-sound latency is ever measured as a problem; it would replace only this class.

Real sample libraries were considered again and are still unreachable from Claude's cloud
workspace (GitHub raw, Maven, npm all blocked by policy); the only soundfont on the machine
(TimGM6mb) is GPL-2, which would be a licensing problem for a monetized app, so it was not used.

## 2026-09-24 — Answers are checked on demand, not auto-scored at the last note

The user asked to hear what they played before committing. So the answer no longer scores itself
the moment the last dot fills in: while answering there's Replay (the melody), My answer (the
answer so far, at the same tempo), undo, and an explicit Check button that only enables once the
answer is complete. After a wrong answer the correct melody plays once more (docs/SPEC.md step 3)
and Replay / My answer stay available for comparing the two. Both behaviors are behind
`core/Features.kt` flags (`HEAR_MY_ANSWER`, `REPLAY_MELODY_AFTER_WRONG_ANSWER`). Keys always
sound, even outside answering, so the keyboard doubles as a keyboard.

## 2026-09-24 — Settings: tempo and melody length only, in SharedPreferences (DataStore later, if ever)

Two integers with defaults, clamped ranges and a Reset button. The default melody length went
from 3 to 4 (owner's call); `DifficultyLevel.level1` now takes the length. Defaults and limits
are in `core/Config.kt`, the model + `SettingsStore` interface in `core/PracticeSettings.kt`, and
the Android implementation in `data/SharedPreferencesSettingsStore.kt`.

SharedPreferences over DataStore (which docs/SPEC.md originally named): it is built in, synchronous
and trivially correct for two ints, whereas DataStore adds a dependency plus Flow/coroutine
plumbing that Claude can't compile-check from the cloud workspace. `ui/` only sees `SettingsStore`,
so switching to DataStore later touches `data/` alone. Settings are read from the store each
time a new melody starts, so a change on the Settings screen applies to the very next melody.

## 2026-09-24 — Three screens, still no navigation library

Home is the hub; Practice and Settings only ever go back to Home. `MainActivity.AppRoot` keeps a
`rememberSaveable`d enum (survives rotation) and a `BackHandler` that returns to Home, so the
system back button doesn't exit the app from Practice. Navigation Compose becomes worth it when
Session Summary arrives and a real back stack matters.

## 2026-09-24 — The Practice ViewModel owns the audio engine for the Activity's lifetime

`viewModel()` in a composable without a navigation graph scopes the ViewModel to the Activity, so
the previous code (engine created with `remember` in the screen, ViewModel keeping the first one)
leaked a SoundPool every time you went Home and back. Now the factory builds `MixerNotePlayer` from
the application context inside the ViewModel, `onCleared` releases it, and the screen's
`DisposableEffect` calls `setActive(true/false)` so the audio output only runs while Practice is on
screen. Returning to Practice also returns to the exact state you left (mid-answer included).

## 2026-09-25 — Scored answers commit as you play; free play kept as a second mode

With Replay / My answer / undo / Check, an answer could be edited until it sounded right, so every
melody ended at 100% and the only skill measured was comparing two sounds. Playing by ear means
hearing which note it is *before* pressing it, so the default answer mode is now scored: every key
press is the answer for its position (right: green, on to the next; wrong: red, try again; after
`Config.MAX_WRONG_PRESSES_PER_NOTE` wrong presses the app reveals the note and moves on), Replay is
limited to `Config.MAX_REPLAYS_BEFORE_FIRST_PRESS` times and only before the first press, and the
melody finishes itself. The score is first-try notes over all notes. This is what the ear-training
apps that actually measure progress do (score as you play, hearings budgeted), and it is what
docs/PLAN.md's "Product design" asks for. Feedback names the confusion ("3rd note: you played E4,
it was F4") and nothing else: no praise, no penalty language.

The old flow stays as **Free play**, one tap away on the Practice screen, because it is how people
hunt for a tune and explore the keyboard; it just never counts toward anything. The toggle is
disabled while a melody is being answered (the plainer option: no abandon path, nothing to log
half-way). Scored mode and the toggle sit behind `Features.SCORED_ANSWER_MODE`; with it off the
screen is free play only. This supersedes the 2026-09-24 entry "Answers are checked on demand"
for the default mode; that flow survives unchanged as Free play.

Mechanics: `core/ScoredAnswer.kt` is the per-melody state machine (pure Kotlin, takes times as
parameters, 15 tests); the ViewModel owns one per melody and derives the strip from it. Response
time per position runs from the moment the keys became available (end of playback, the previous
position resolving, the end of a reveal) — narrower than "from the previous position resolving",
so the reveal's 700 ms input lockout isn't counted as thinking time. The pressed note sounds at the
press and the revealed note `Config.REVEAL_DELAY_MILLIS` later, so the two don't clash. After a
non-clean melody the replay waits `Config.REPLAY_AFTER_FINISH_GAP_BEATS`, because starting a
sequence fades whatever is still sounding and would cut off the user's last note. The old result
type `ScoredAnswer` (from `AnswerScorer.score`) was renamed `AnswerScore` to free the name.
`core/Clock.kt` (interface) and `data/RealClock.kt` are the first use of the `Clock` boundary from
CLAUDE.md.
