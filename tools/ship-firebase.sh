#!/usr/bin/env bash
# Build a signed release APK and upload it to Firebase App Distribution.
# One-time setup is in docs/SHIPPING.md.
#
#   tools/ship-firebase.sh [tester-group] [release notes]
#
# Defaults: group "testers", notes = the last commit's subject line.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

APP_ID="${FIREBASE_APP_ID:-$(cat .firebase-app-id 2>/dev/null || true)}"
if [ -z "$APP_ID" ]; then
  echo "No Firebase App ID. Put it in .firebase-app-id at the repo root" >&2
  echo "(one line, looks like 1:123456789012:android:0a1b2c...) or set FIREBASE_APP_ID." >&2
  exit 1
fi
if [ ! -f keystore.properties ]; then
  echo "keystore.properties missing: run tools/make-release-keystore.sh first" >&2
  exit 1
fi
if ! command -v firebase >/dev/null; then
  echo "firebase CLI not on PATH: see docs/SHIPPING.md" >&2
  exit 1
fi

TESTER_GROUPS="${1:-testers}"
NOTES="${2:-$(git log -1 --pretty='%h %s')}"
export JAVA_HOME="${JAVA_HOME:-$HOME/android-studio/jbr}"

./gradlew assembleRelease --console=plain -q

APK="app/build/outputs/apk/release/app-release.apk"
if [ ! -f "$APK" ]; then
  echo "no signed APK at $APK (an unsigned one means keystore.properties was not picked up)" >&2
  exit 1
fi

firebase appdistribution:distribute "$APK" \
  --app "$APP_ID" \
  --groups "$TESTER_GROUPS" \
  --release-notes "$NOTES"
