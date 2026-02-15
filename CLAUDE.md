# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

- **Build:** `./gradlew build`
- **Run unit tests:** `./gradlew test`
- **Run single unit test:** `./gradlew test --tests "nl.vanhaak.claudlist.ExampleUnitTest.addition_isCorrect"`
- **Run instrumented tests:** `./gradlew connectedAndroidTest` (requires emulator/device)
- **Clean build:** `./gradlew clean build`
- **Assemble debug APK:** `./gradlew assembleDebug`

## Architecture

Single-module Android app (`app`) using Jetpack Compose with Material 3. Package: `nl.vanhaak.claudlist`.

- **Entry point:** `MainActivity.kt` — ComponentActivity with edge-to-edge Compose UI
- **Theme:** `ui/theme/` — Material 3 theming with dynamic color support (Android 12+)
- **Dependencies:** Managed via Gradle version catalog (`gradle/libs.versions.toml`)

**Target:** API 24–34, Kotlin 1.9.0, Compose compiler 1.5.1, Gradle 8.7.
