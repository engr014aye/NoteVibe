# NoteVibe 📱

A production-ready, 100% offline Android productivity application built with **Jetpack Compose**, **Room DB (SQLite)**, and strict **Clean Architecture** (Layer-first approach: `/data`, `/domain`, `/presentation`), styled with premium Apple iOS-inspired glassmorphism, fluid spring physics, and continuous squircle corners.

---

## 🎯 Architecture & Tech Stack

- **Package Name**: `com.nouman.notevibe`
- **SDK Compatibility**: Minimum SDK 26 (Android 8.0) | Target SDK 36 (Android 16)
- **Language**: Kotlin 2.0+ (with modern Kotlin Compose compiler plugin)
- **UI Framework**: Jetpack Compose & Material 3 with custom iOS Human Interface Design System
- **Local Persistence**: Room SQLite Database (100% local, no cloud / no network adapters)
- **Architecture**: Strict Layer-First Clean Architecture
  - `/domain`: Business logic, pure Kotlin entities (`Note`, `Priority`, `FocusBoardStats`), repository contracts, and isolated UseCases.
  - `/data`: Room Entities, DAOs, SQLite database migrations, bidirectional Mappers, and Repository implementations.
  - `/presentation`: MVVM pattern with `ViewModel` and `StateFlow`, UI states, fluid spring interaction modifiers, iOS-inspired glassmorphism components, and Jetpack Navigation Compose.

---

## 🛡️ Strict Offline-First & Privacy Guarantee

- **Zero Network Permissions**: The app does **NOT** declare `android.permission.INTERNET` in its `AndroidManifest.xml`.
- **Zero Third-Party Trackers**: Completely free of Firebase, Crashlytics, Google Analytics, or remote tracking SDKs.
- **Data Sovereignty**: 100% of notes, focus states, and priority metadata remain securely encrypted on the local device SQLite storage.

---

## 🌟 Core Features

### 1. 🎯 Focus Board (Strict 3-Slot Cognitive Limit)
- Main dashboard strictly limited to pinning **exactly 3 active notes/tasks** to eliminate cognitive fatigue and maintain laser focus.
- Dedicated Slot #1, #2, and #3 cards with live progress tracking (`X of 3 Slots Occupied`).
- Built-in guardrails: Attempts to pin a 4th note trigger a polite iOS alert banner informing the user to unpin an active note first.

### 2. 🏷️ Color-Coded Priority Tags & Filtering
- Seamless visual tagging using Apple-inspired color semantics:
  - **High Priority**: Apple Red / Rose (`#FF3B30`)
  - **Medium Priority**: Apple Orange / Amber (`#FF9500`)
  - **Low Priority**: Apple Mint / Green (`#34C759`)
- Real-time segmented filter pill bar to seamlessly filter active notes by priority level.

### 3. 📦 7-Day Auto-Archive System
- Automated local Room DB logic that detects completed notes older than 7 days (`completedAt + 7 days <= now`) and transitions them to the **Archive** screen.
- Keeps the active workspace clutter-free while ensuring full historical access.
- Users can view, restore (unarchive), or permanently delete archived items at any time.

---

## 🎨 iOS-Inspired Design System

- **Glassmorphic Aesthetic**: Translucent frosted surfaces with specular edge highlights and multi-layer depth.
- **Continuous Squircle Radii**: Apple-style continuous smooth rounded corners (20dp - 28dp) across note cards, dialogs, and segmented filters.
- **Spring Physics Animations**: Custom `springClickable` and layout transitions utilizing `spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)`.
- **Typographic Hierarchy**: High-contrast typography scaled to match Apple Human Interface guidelines.

---

## 🚀 Store Listing Assets

Store listing assets have been generated strictly at:
`D:\Antigravity\store listing\NoteVibe`

- `app_icon_512.png`: 512x512 flat, minimalist Play Store app icon (strictly without 3D shadows or awkward corner gradients, smoothly filling the squircle).
- `feature_graphic_1024x500.png`: 1024x500 high-resolution Play Store promotional feature graphic.

---

## 🛠️ Building & Running

Open the project directly in **Android Studio Ladybug (or newer)**:
1. Select **File > Open** and navigate to `D:\Antigravity\NoteVibe`.
2. Sync the project with Gradle files.
3. Run the app on any Android device or emulator running API 26 to API 36.
