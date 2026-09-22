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

Not yet done, in rough priority order: wire a real streak/session via `data/`; the fixed
10-melody session + summary screen from SPEC; levels 2-5 and ladder promotion; daily reminders
(WorkManager); Settings screen. This project's `CLAUDE.md` also asks for a commit after each
working milestone — Claude can't run `git` here (no shell on this machine from this session),
so that commit is on you once you've verified the build.
