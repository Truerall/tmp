# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Model Routing — Right Model for the Job

Assign agents the cheapest model that can handle the subtask well:

- **opus** — Architecture decisions, complex debugging, multi-file refactors requiring deep reasoning, code review, planning. Use for the team lead / orchestrator role.
- **sonnet** — Implementation work: writing new features, editing existing code, writing tests, fixing bugs with clear reproduction steps. This is your default workhorse for code changes.
- **haiku** — Fast, cheap tasks: running tests, linting, formatting, simple file searches, generating boilerplate, mechanical find-and-replace style edits, gathering information.
-


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

