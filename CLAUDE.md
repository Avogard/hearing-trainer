# Hearing Trainer

Android ear-training app: generates a short melody, plays it, the user
replays it on an on-screen keyboard and gets feedback. Daily-practice
loop with streaks and reminders. Kotlin + Jetpack Compose, single
Gradle module, min SDK 26, target latest stable SDK.

## Owner context

The owner is an experienced C++ developer, new to Android and Kotlin.
Explain Android-specific concepts briefly the first time they appear
(Activity, lifecycle, Compose recomposition, Gradle, ViewModel).
Prefer plain, well-known solutions over clever ones. When there are
two reasonable options, name both and recommend one.

## Commands

- Build:      ./gradlew assembleDebug
- Unit tests: ./gradlew testDebugUnitTest
- Install:    ./gradlew installDebug   (phone connected, USB debugging on)
- Lint:       ./gradlew lint
- Logs:       adb logcat -s HearingTrainer
- Core-only tests without the Android SDK: tools/verify-core.sh
- Re-render piano samples: python3 tools/render_piano_samples.py

## Architecture rules

- `core/` package: pure Kotlin, NO Android imports. Music theory (scales,
  intervals), melody generator, answer scoring, difficulty ladder.
  Fully unit-tested. This code must stay portable (future iOS via KMP).
- `audio/`: playback of notes (sampled piano). The only place that
  touches audio APIs. A small software mixer on AudioTrack
  (`MixerNotePlayer`) plays one bundled WAV per note; move to Oboe only
  if latency is a measured problem. Samples are rendered by
  `tools/render_piano_samples.py` (needs numpy) — re-run it after
  changing its constants; never hand-edit the WAVs.
- `ui/`: Compose screens and ViewModels. MVVM, unidirectional data flow,
  one ViewModel per screen.
- `data/`: persistence (SharedPreferences for the two settings ints,
  DataStore if settings grow; Room for progress) and reminders
  (WorkManager + local notifications).
- A note is an Int MIDI number everywhere in `core/` (60 = middle C).
  Durations are in beats (Double), tempo in BPM.

## Changeability (this app is developed rapidly; behavior will change often)

- Anything that touches a platform API sits behind a small Kotlin
  interface owned by `core/` or `ui/`: `AudioEngine` (play a note),
  `Clock`, `ProgressStore`, `ReminderScheduler`. Swapping SoundPool for
  Oboe, or Room for something else, must not touch `core/` or screens.
- Exercise types are data, not code paths: one `Exercise` interface
  (generate prompt, judge answer, describe feedback). Adding intervals
  or chords means adding a class, not editing existing ones.
- The difficulty ladder, default settings and tuning constants live in
  ONE file (`core/Config.kt` or a bundled JSON), never scattered as
  magic numbers.
- Every Room schema change ships with a migration; never
  `fallbackToDestructiveMigration` — the owner's own progress data is
  the test data.
- New or experimental behavior goes behind a feature flag in
  `core/Features.kt` (a plain Kotlin object with booleans) so it can be
  turned off without a revert.
- Do NOT over-abstract to achieve this: no dependency-injection
  framework, no multi-module split, no generic "plugin system" until a
  second concrete implementation actually exists. Interfaces at the
  four boundaries above are enough.
- Things that are genuinely hard to reverse (applicationId / package
  name, signing key, min SDK going down, Room table names): flag them
  explicitly and ask before changing.

## Conventions

- Kotlin official code style. No wildcard imports.
- Every change to `core/` comes with a unit test.
- Run the unit tests before declaring a task done. If they fail, show
  the failing output; never delete or skip a test to make it pass.
- Do not add a dependency without saying why in the commit message.
- Do not touch signing config, ProGuard/R8 or release build settings
  unless explicitly asked.
- Commit after each working milestone with a descriptive message.
- Keep the UI usable one-handed on a phone: big touch targets, no
  hidden gestures, the default path shows no settings.

## Git

- Repo: local git + GitHub, private. Solo project — work directly on
  `main`, no branch/PR ceremony unless that changes later.
- Commit after each working milestone: it builds, unit tests pass. Never
  commit a broken build or a failing test to get "checkpoint" coverage —
  fix it or stash it first.
- Commit message: one short imperative-mood summary line (~50 chars, no
  trailing period), e.g. "Add melody generator and answer scoring". Add
  a body only when the *why* isn't obvious from the diff or the commit
  touches more than one concern — most commits don't need one.
- Push right after committing. Local-only commits aren't backed up and
  don't show up anywhere Claude or the owner can point at them later —
  don't let them pile up unpushed.
- Never commit: `build/`, `.gradle/`, `.idea/` (mostly), `local.properties`
  (machine-specific SDK path), any keystore or signing credentials. The
  Android Studio-generated `.gitignore` already covers the first four —
  don't fight it or re-add something it excludes.
- Claude: propose the commit message, and stage/commit/push yourself
  once you have a shell on this machine; until then, give the owner the
  exact commands to run themselves rather than describing them vaguely.

## Read first

- docs/PLAN.md      where this is going: vision, learning model, features
                    by phase, roadmap. Sections 7–9 drive what to build next.
- docs/SPEC.md      what the app does (screens, rules, difficulty ladder)
- docs/DECISIONS.md why it is built this way
- docs/research/    the sourced research behind PLAN.md (pedagogy, learning
                    science, landscape); consult before changing the engine
