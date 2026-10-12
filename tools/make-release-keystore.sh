#!/usr/bin/env bash
# Create the release signing key, once. Produces two gitignored files at the
# repo root: release-keystore.jks (the key) and keystore.properties (its
# passwords, read by app/build.gradle.kts). Back both up somewhere outside
# this folder: anyone who installed the app from an APK you handed out can
# only get updates signed with this exact key.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
KS="$ROOT/release-keystore.jks"
PROPS="$ROOT/keystore.properties"

if [ -e "$KS" ] || [ -e "$PROPS" ]; then
  echo "refusing to overwrite: $KS or $PROPS already exists" >&2
  exit 1
fi

# keytool ships with any JDK; Android Studio's bundled one is the fallback.
JAVA_HOME="${JAVA_HOME:-$HOME/android-studio/jbr}"
KEYTOOL="$JAVA_HOME/bin/keytool"
[ -x "$KEYTOOL" ] || KEYTOOL="$(command -v keytool)" || {
  echo "keytool not found; set JAVA_HOME to a JDK" >&2; exit 1; }

PASSWORD="$(head -c 24 /dev/urandom | base64 | tr -d '/+=' | head -c 24)"

umask 077
"$KEYTOOL" -genkeypair \
  -keystore "$KS" -storetype PKCS12 -alias hearingtrainer \
  -keyalg RSA -keysize 4096 -validity 10000 \
  -storepass "$PASSWORD" -keypass "$PASSWORD" \
  -dname "CN=Hearing Trainer, O=Hearing Trainer, C=PL"

cat > "$PROPS" <<PROPS
storeFile=release-keystore.jks
storePassword=$PASSWORD
keyAlias=hearingtrainer
keyPassword=$PASSWORD
PROPS

echo "Created:"
echo "  $KS"
echo "  $PROPS"
echo "Back both up now (password manager or encrypted drive). Then: ./gradlew assembleRelease"
