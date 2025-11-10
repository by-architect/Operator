# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Operator is an Android process monitoring application that displays system processes with real-time updates. It requires root access to function and uses the libsu library for root shell operations. The app is built with Jetpack Compose and follows a clean architecture pattern.

## Build and Development Commands

### Building the Application

```bash
# Build debug APK
cd Operator && ./gradlew assembleDebug

# Build release APK
cd Operator && ./gradlew assembleRelease

# Install debug build to connected device
cd Operator && ./gradlew installDebug
```

### Testing

```bash
# Run unit tests
cd Operator && ./gradlew test

# Run instrumented tests (requires connected device/emulator)
cd Operator && ./gradlew connectedAndroidTest
```

### Code Quality

```bash
# Clean build artifacts
cd Operator && ./gradlew clean

# Run lint checks
cd Operator && ./gradlew lint
```

## Architecture

### Layer Structure

The codebase follows a three-layer architecture:

1. **Presentation Layer** (`presentation/`)
   - Built with Jetpack Compose
   - Uses MVVM pattern with ViewModel and StateFlow
   - `ProcessScreen` is the main UI component
   - `ProcessViewModel` manages UI state and coordinates data flow

2. **Data Layer** (`data/`)
   - `SystemFetcher`: Provides process data via shell commands
   - `ShellManager`: Manages root shell lifecycle using libsu
   - `ProcessSettingsRepository`: Stores user preferences for refresh rate and displayed columns
   - `Terminal`: Alternative command execution utility

3. **Common Layer** (`common/`)
   - Resource wrapper for handling Loading/Success/Error states
   - Utility classes for logging and error handling
   - Constants and animation specifications

### Key Architectural Patterns

**Shell Integration**: The app uses libsu (topjohnwu's library) to execute root shell commands. `ShellManager` initializes the shell with `FLAG_MOUNT_MASTER` flag and 100ms timeout. All process operations go through `SystemFetcher` which wraps shell commands in Flow<Resource<T>>.

**Process List Flow**:
1. `ProcessViewModel` combines `shellState`, `refreshInterval`, `processLabels`, `sortOrder`, and `searchQuery` flows
2. On changes, it triggers periodic refresh via `startPeriodicRefresh()`
3. `SystemFetcher.getProcessList()` executes `ps -A -o [labels]` command
4. Results are parsed, filtered by search query, sorted, and emitted as Resource states
5. ViewModel updates `_processState` which the UI observes

**Process Labels System**: `ProcessLabel` enum defines all available process columns (PID, CPU%, MEM%, etc.). Users can customize which labels to display. The enum includes `isNumber` flag to determine numeric vs string sorting.

**State Management**: All UI state flows through StateFlow in the ViewModel. The app uses combine() to react to multiple state changes and trigger appropriate refreshes.

## Technology Stack

- **Language**: Kotlin 2.0.21
- **Min SDK**: 24 (Android 7.0)
- **Target/Compile SDK**: 35
- **UI Framework**: Jetpack Compose with Material3
- **Dependency Injection**: Hilt 2.48 (configured but not fully implemented in ViewModels)
- **Root Access**: libsu 6.0.0 (core, service, nio modules)
- **Architecture Components**: Lifecycle, ViewModel, StateFlow

## Important Implementation Notes

### Root Access Requirements

The app requires root access to function. `SystemFetcher.loadShell()` checks:
1. Shell is alive
2. Shell has root access

If either check fails, the app shows an error screen with retry option.

### Process Refresh Mechanism

Process list refreshes periodically based on user-configured interval (default 3000ms). The refresh job is cancelled and restarted when:
- Refresh interval changes
- Displayed process labels change
- Sort order changes
- Search query changes

If shell encounters an error, the refresh job is paused automatically.

### Shell Command Parsing

Process data comes from `ps -A -o [labels]` command. The parser:
1. Drops the header row
2. Splits each line by whitespace into the exact number of labels
3. Creates a Map<ProcessLabel, String> for each process
4. Filters by search query if present

### ViewModel Construction

`ProcessViewModel` uses `@Inject constructor()` with Jakarta annotations but is currently instantiated manually in ProcessScreen using `viewModel { ProcessViewModel(repository) }`. Hilt setup exists in build files but ViewModels are not yet using @HiltViewModel.

## Development Workflow

When working with this codebase:

1. All Android commands should be run from the `Operator/` directory
2. Root shell functionality can only be tested on rooted Android devices
3. Process monitoring relies on Android's `ps` command syntax which may vary by device/ROM
4. The app uses Material3 dynamic theming
5. State flows should be collected using `collectAsState()` in Composables
