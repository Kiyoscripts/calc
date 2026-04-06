# KiyoCalc

Modern Android calculator app built with:
- Kotlin
- Gradle Kotlin DSL
- Jetpack Compose
- MVVM
- Room

## Features
- single-screen calculator UI
- digits, decimal, plus, minus, multiply, divide
- percent and sign toggle
- AC and backspace
- expression evaluation with precedence
- Room-backed history in a bottom sheet
- Material 3 light/dark theme

## Package
`com.mark.kiyocalc`

## Build
This project is designed to open in Android Studio and build into a debug APK.

Typical command:

```bash
./gradlew assembleDebug
```

Expected APK path:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Important note about this environment
I generated the project files here, but this host does not currently have Java / Android SDK available, so I could not produce the APK on-host.

To build locally, install:
- JDK 17
- Android SDK + platform/build tools matching compileSdk 35
- Android Studio Hedgehog+ / current stable

## Structure
- `app/src/main/java/com/mark/kiyocalc` → app code
- `data` → Room entities, DAO, repository
- `domain` → evaluator and state/action models
- `presentation` → ViewModel
- `ui` → Compose screens/components/theme

## Next improvements
- release signing
- better sign-toggle edge cases
- long-press history actions
- haptics and animation
- scientific mode
