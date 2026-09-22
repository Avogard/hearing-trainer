#!/usr/bin/env bash
# Compiles and runs the core/ unit tests directly with the Kotlin compiler + JUnit4,
# without Gradle, the Android Gradle Plugin, or the Android SDK.
#
# Why this exists: core/ is pure Kotlin with no Android imports (see CLAUDE.md), but the
# app is a single Android Gradle module, so a normal `./gradlew testDebugUnitTest` needs the
# Android SDK and network access to Google's/Maven's repositories. This script needs neither
# — it borrows the Kotlin compiler and JUnit jars bundled inside any local Gradle
# installation, so it works even fully offline. Use it for a fast inner loop on core/ logic;
# still run `./gradlew testDebugUnitTest` before calling a change done, since that's the real
# build (it also compiles the Android-facing code this script never touches).
#
# Usage: tools/verify-core.sh
# Requires: `gradle` on PATH (any recent version installed on this machine), and a JDK.

set -euo pipefail
cd "$(dirname "$0")/.."

CORE_MAIN="app/src/main/java/com/hearingtrainer/app/core"
CORE_TEST="app/src/test/java/com/hearingtrainer/app/core"

if [ ! -d "$CORE_MAIN" ]; then
  echo "error: $CORE_MAIN not found. Run this from the project root (or after the core/" >&2
  echo "package has been copied in from core-package-ready/, if you haven't done that yet)." >&2
  exit 1
fi

GRADLE_BIN="$(command -v gradle || true)"
if [ -z "$GRADLE_BIN" ]; then
  echo "error: no 'gradle' on PATH. Install Gradle, or run this from a shell that has it" >&2
  echo "(e.g. Android Studio's Terminal, or after running ./gradlew once)." >&2
  exit 1
fi
GRADLE_HOME="$(dirname "$(dirname "$(readlink -f "$GRADLE_BIN")")")"
LIB="$GRADLE_HOME/lib"

# Collect every jar the embeddable Kotlin compiler + its IntelliJ-platform bits need to run,
# by pattern rather than pinned version, so this keeps working as the local Gradle updates.
shopt -s nullglob
KOTLINC_JARS=(
  "$LIB"/kotlin-compiler-embeddable-*.jar
  "$LIB"/kotlin-script-runtime-*.jar
  "$LIB"/kotlin-daemon-embeddable-*.jar
  "$LIB"/kotlin-stdlib-*.jar
  "$LIB"/kotlinx-coroutines-core-jvm-*.jar
  "$LIB"/kotlin-reflect-*.jar
  "$LIB"/kotlinx-metadata-jvm-*.jar
  "$LIB"/trove4j-*.jar
  "$LIB"/kotlin-scripting-jvm-*.jar
  "$LIB"/kotlin-scripting-common-*.jar
  "$LIB"/annotations-*.jar
)
JUNIT_JARS=("$LIB"/junit-4*.jar "$LIB"/hamcrest-core-*.jar)
STDLIB_JAR=("$LIB"/kotlin-stdlib-*.jar)

if [ ${#KOTLINC_JARS[@]} -eq 0 ] || [ ${#JUNIT_JARS[@]} -eq 0 ]; then
  echo "error: couldn't find the Kotlin compiler / JUnit jars under $LIB" >&2
  echo "(this script expects a full Gradle distribution, not just the wrapper)." >&2
  exit 1
fi
join_by_colon() { local IFS=:; echo "$*"; }
KOTLINC_CP="$(join_by_colon "${KOTLINC_JARS[@]}")"
TEST_CP="$(join_by_colon "${JUNIT_JARS[@]}" "${STDLIB_JAR[@]}")"

WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT
mkdir -p "$WORK/out"

echo "Compiling core/ (main + test)..."
java -cp "$KOTLINC_CP" org.jetbrains.kotlin.cli.jvm.K2JVMCompiler \
  -d "$WORK/out" -cp "$TEST_CP" "$CORE_MAIN" "$CORE_TEST" 2>&1 | grep -v "^warning:" || true

# Discover the compiled test classes and run them with JUnitCore.
TEST_CLASSES=$(cd "$WORK/out" && find . -name '*Test.class' | sed 's#^\./##; s#/#.#g; s#\.class$##')
if [ -z "$TEST_CLASSES" ]; then
  echo "error: compilation produced no *Test classes." >&2
  exit 1
fi

echo "Running: $(echo "$TEST_CLASSES" | tr '\n' ' ')"
java -cp "$WORK/out:$TEST_CP" org.junit.runner.JUnitCore $TEST_CLASSES
