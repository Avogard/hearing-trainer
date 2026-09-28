# Hearing Trainer

An Android ear-training app. It plays a short melody (after a cadence
that sets the key). You play it back on an on-screen keyboard and get
feedback note by note. It is meant for a few minutes of practice a day.

Kotlin + Jetpack Compose, single Gradle module, min SDK 26.

## Status

Early MVP. Working now:

- Melody generator with a difficulty ladder (pure Kotlin, unit-tested)
- Sampled-piano playback through a small AudioTrack mixer
- Practice screen in **Scored** mode (every press counts, misses are
  revealed after two tries) and **Free play** mode (edit freely, then
  Check)
- Settings: tempo and melody length
- Local attempt log

Not built yet: session summary, streaks, reminders. See
[docs/PLAN.md](docs/PLAN.md) for the roadmap.

## Build and run

You need the Android SDK (Android Studio ships one, plus a JDK).

```bash
./gradlew assembleDebug        # build
./gradlew installDebug         # install on a connected phone (USB debugging on)
./gradlew testDebugUnitTest    # unit tests
./gradlew lint
adb logcat -s HearingTrainer   # app logs
```

To test the pure-Kotlin core without the Android SDK, run
`tools/verify-core.sh`.

The piano samples in `app/src/main/res/raw/` are generated. To change them,
edit the constants in `tools/render_piano_samples.py` and run
`python3 tools/render_piano_samples.py` (needs numpy). Don't edit the
WAVs by hand.

## Layout

```
app/src/main/java/com/hearingtrainer/app/
  core/    pure Kotlin, no Android imports: music theory, melody generator,
           scoring, difficulty ladder, Config.kt, Features.kt
  audio/   note playback (the only code that touches audio APIs)
  data/    persistence, clock, attempt log
  ui/      Compose screens and one ViewModel per screen
tools/     sample renderer, core-only test script
docs/      plan, spec, design decisions, research
```

## Docs

- [docs/PLAN.md](docs/PLAN.md): vision, learning model, roadmap
- [docs/SPEC.md](docs/SPEC.md): what the app does, screen by screen
- [docs/DECISIONS.md](docs/DECISIONS.md): why it's built this way
- [docs/research/](docs/research/): the research behind the plan
