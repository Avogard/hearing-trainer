# Shipping

How a build gets from this repo onto someone else's phone. Three stages,
in order of how far it travels.

| Stage | Who | How | Needs |
| --- | --- | --- | --- |
| 1 | You | `./gradlew installDebug` over USB | A phone with USB debugging on |
| 2 | A few testers | Firebase App Distribution: `tools/ship-firebase.sh` | Release key, Firebase project, Firebase CLI |
| 3 | Strangers | Google Play, as planned for M5 (see PLAN.md) | Play developer account, final name and applicationId, store listing, privacy policy |

Stage 2 does not lock anything in: the Firebase app can be re-registered
under a new package name later. Stage 3 does: the first Play upload fixes
the applicationId forever, so pick the real name first.

## Terms, once

- **APK**: the installable package, like an installer .exe. Phones install
  it directly ("sideloading"); the user has to allow installs from that
  source once.
- **AAB** (App Bundle): what Google Play wants instead of an APK. Play
  splits it per device. `./gradlew bundleRelease` makes one; not needed
  for Firebase.
- **Signing key**: every APK is signed; Android only installs an update
  if it is signed with the same key as the installed version. Lose the key
  and existing installs can never update. Play App Signing (stage 3) keeps
  the real key on Google's side so this cannot happen; for stage 2 the key
  is yours to keep.

## One-time setup

1. **Create the release key.** Run once, then back up the two files it
   prints (password manager or an encrypted drive):

       tools/make-release-keystore.sh

   It writes `release-keystore.jks` and `keystore.properties` at the repo
   root. Both are gitignored. `app/build.gradle.kts` signs the release
   build with them when they exist and leaves it unsigned when they do not.

2. **Create a Firebase project** at https://console.firebase.google.com
   (any Google account; the free Spark plan is enough for App Distribution).
   In it, add an Android app with package name `com.hearingtrainer.app`.
   Skip the `google-services.json` download: the app does not use the
   Firebase SDK, only the upload service.

3. **Copy the App ID.** Project settings, General, Your apps. It looks like
   `1:123456789012:android:0a1b2c3d4e5f6a7b`. Put it on one line in
   `.firebase-app-id` at the repo root (not secret; commit it).

4. **Enable App Distribution** in the console (Release & Monitor, App
   Distribution, Get started) and create a tester group named `testers`
   with the emails of the people who get builds.

5. **Install the Firebase CLI** and sign in. Two options; the first needs
   no Node.js:

       curl -sL https://firebase.tools | bash     # standalone binary
       firebase login                              # opens a browser once

   or, if Node.js is installed: `npm install -g firebase-tools`.

## Every release

1. Bump `versionCode` (and `versionName` if you like) in
   `app/build.gradle.kts`. Testers' phones treat a lower or equal
   versionCode as "nothing new".
2. Commit, then:

       tools/ship-firebase.sh

   This runs `./gradlew assembleRelease` and uploads the signed APK with
   the last commit's subject as release notes. Optional arguments:
   tester group, then release notes.

3. Testers get an email with a link; the first time they accept the
   invitation and allow installs from their browser. Later builds show up
   as update notices.

## When this changes

- New applicationId (planned before Play): register a new Android app in
  the same Firebase project, update `.firebase-app-id`. Testers install
  fresh once; the old package stays on their phone until they remove it.
- Moving to Play: `./gradlew bundleRelease`, upload the AAB in the Play
  Console, opt in to Play App Signing and upload `release-keystore.jks`
  as the upload key when asked. The Firebase channel can keep running
  alongside for pre-release builds.
