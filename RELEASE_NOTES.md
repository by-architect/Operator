# Operator v1.0 - Release Notes

**Release Date:** October 19, 2025
**Version:** 1.0.0
**Platform:** Android 7.0+ (API 24+)

## Overview

We are excited to announce the first stable release of **Operator** - a Matrix-inspired Android process manager that brings powerful system monitoring and process management capabilities to rooted Android devices. Built with modern Android technologies including Jetpack Compose and Kotlin, Operator provides real-time insights into your device's running processes with a clean, minimal interface.

## What is Operator?

Operator is a native Android application that leverages root access to provide deep system-level monitoring and control over running processes. Whether you're a developer, system administrator, or power user, Operator gives you the tools to monitor CPU and memory usage, terminate unwanted processes, and customize your monitoring experience.

## Key Features

### Real-Time Process Monitoring
- **Live Process Tracking**: Monitor all running system processes in real-time
- **CPU & Memory Metrics**: Track CPU percentage and memory usage for each process
- **Detailed Process Information**: View PID, package name, parent PID, and 45+ additional process attributes
- **Configurable Refresh Rate**: Customize monitoring intervals (default: 3 seconds) for optimal performance

### Advanced Process Management
- **Process Termination**: Kill background processes with precision using root privileges
- **Smart Search**: Quickly find processes by name or package identifier
- **Flexible Filtering**: Focus on specific processes with real-time search filtering
- **Process Selection**: Tap any process to view detailed information

### Customizable Interface
- **45+ Process Attributes**: Choose from a comprehensive list of process labels including:
  - Basic: PID, PPID, CPU%, MEM%, USER, TTY
  - Advanced: VSZ, RSS, WCHAN, ADDR, S, PRI, NI, RTPRIO, SCH
  - Detailed: TIME, CMD, ARGS, and many more
- **Flexible Sorting**: Sort by CPU usage, memory usage, PID, or process name
- **Ascending/Descending**: Toggle sort order with a single tap
- **Scrollable Data Table**: Navigate through process lists with smooth scrolling
- **Material3 Design**: Modern UI with dynamic theming and dark mode support

### Persistent Settings
- **User Preferences**: All settings are saved automatically to local database
- **Configurable Refresh Intervals**: Set your preferred monitoring frequency
- **Settings Persistence**: Your preferences survive app restarts

### Root Integration
- **Secure Root Access**: Uses libsu library for safe root shell communication
- **Automatic Retry**: Built-in connection retry mechanism for reliability
- **Error Handling**: Comprehensive error states with user-friendly messages
- **Shell State Management**: Monitor root access status in real-time

## Technical Specifications

### Architecture
- **Pattern**: MVVM (Model-View-ViewModel) with Clean Architecture
- **UI Framework**: 100% Jetpack Compose with Material3
- **Language**: Kotlin 2.0.21
- **State Management**: Kotlin Flow and StateFlow for reactive updates
- **Dependency Injection**: Hilt-ready architecture

### Technologies Used
- **Android SDK**: Min API 24 (Android 7.0), Target API 35 (Android 15)
- **Jetpack Compose**: 2024.09.00 (latest stable)
- **Material3**: Modern Material Design components
- **Room Database**: 2.6.1 for persistent storage
- **libsu**: 6.0.0 by topjohnwu for root access
- **Kotlin Coroutines**: Asynchronous programming and Flow APIs
- **Gradle**: 8.9.1 build system

### Performance
- **Lightweight**: Minimal resource footprint
- **Efficient**: Configurable refresh rates to balance performance and battery
- **Responsive**: Real-time UI updates with Compose state management
- **Stable**: Comprehensive error handling and recovery mechanisms

## System Requirements

### Required
- Android device running Android 7.0 (Nougat) or higher
- Root access (device must be rooted)
- Approximately 5 MB of storage space

### Recommended
- Android 10.0 or higher for optimal performance
- Magisk or SuperSU for root management
- At least 2 GB of RAM

## Installation

1. Download the `Operator-v1.0.apk` file
2. Enable "Install from Unknown Sources" in your device settings
3. Install the APK
4. Grant root permissions when prompted
5. Launch Operator and start monitoring

## Getting Started

1. **First Launch**: Grant root access when prompted by your root manager
2. **View Processes**: The main screen displays all running processes automatically
3. **Search**: Use the search bar to filter processes by name
4. **Sort**: Tap column headers to sort by that attribute
5. **Select**: Tap any process to view detailed information
6. **Terminate**: Use the kill button to terminate unwanted processes
7. **Configure**: Access settings to adjust refresh rate and preferences

## Known Limitations

- **Root Required**: Operator requires root access and will not function on non-rooted devices
- **Android Only**: This release is for Android devices only
- **Process Data**: Some process information may vary depending on Android version and kernel
- **Battery Usage**: Shorter refresh intervals may impact battery life

## What's Next?

We're committed to continuously improving Operator. Future updates may include:
- Process CPU and memory usage history graphs
- Custom process groups and favorites
- Advanced filtering and saved searches
- Process startup monitoring and notifications
- Export process data for analysis
- Multi-language support

## Open Source

Operator is licensed under the GNU General Public License v3.0. The source code is available for review, modification, and contribution.

## Credits

### Libraries & Dependencies
- **libsu** by John Wu (topjohnwu) - Root shell library
- **Jetpack Compose** by Google - Modern Android UI toolkit
- **Material3** by Google - Material Design components
- **Room** by Google - Local database
- **Kotlin Coroutines** by JetBrains - Async programming

### Development
- Built with Android Studio Ladybug
- Developed using modern Android development best practices
- Inspired by Matrix command-line interfaces

## Support & Feedback

- **Issues**: Report bugs or request features via GitHub Issues
- **Documentation**: See `CLAUDE.md` for development documentation
- **License**: See `Lisence.md` for GPL v3.0 license details

## Changelog

### Version 1.0.0 (October 19, 2025)
- Initial stable release
- Real-time process monitoring with CPU and memory metrics
- Process termination with root privileges
- Advanced search and filtering capabilities
- Customizable process columns (45+ attributes available)
- Flexible sorting by multiple criteria
- Configurable refresh rates
- Material3 UI with dark theme support
- Persistent settings with Room database
- Comprehensive error handling and retry mechanisms
- Support for Android 7.0 through Android 15

---

**Thank you for using Operator v1.0!**

We hope Operator enhances your Android system monitoring and management experience. Your feedback helps us improve - please share your thoughts and suggestions.

*Stay in control. Monitor. Terminate. Operate.*
