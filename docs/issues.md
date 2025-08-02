# Project Issues Documentation

This document tracks significant issues encountered during development, their root causes, and solutions implemented.

---

## Issue #1: JDK Image Transformation Error
**Date:** January 18, 2025  
**Severity:** Critical  
**Status:** ✅ Resolved

### Problem
Build failed with JDK image transformation error during compilation:
```
Execution failed for task ':app:compileDevelopDebugJavaWithJavac'.
> Could not resolve all files for configuration ':app:androidJdkImage'.
   > Failed to transform core-for-system-modules.jar to match attributes {artifactType=_internal_android_jdk_image, org.gradle.libraryelements=jar, org.gradle.usage=java-runtime}.
      > Execution failed for JdkImageTransform: /Volumes/Oink_Machine/Library/Android/sdk/platforms/android-34/core-for-system-modules.jar.
         > Error while executing process /Volumes/Oink_Machine/Applications/Android Studio.app/Contents/jbr/Contents/Home/bin/jlink with arguments {--module-path /Volumes/Oink_Machine/.gradle/caches/transforms-3/d53723bdf616124ea4640cf337c80d2b/transformed/output/temp/jmod --add-modules java.base --output /Volumes/Oink_Machine/.gradle/caches/transforms-3/d53723bdf616124ea4640cf337c80d2b/transformed/output/jdkImage --disable-plugin system-modules}
```

### Root Cause
Multiple factors contributed to this issue:

1. **Corrupted Gradle Cache**: The custom `gradle.user.home=.gradle-local` setting in `gradle.properties` was causing cache corruption in the JDK image transform artifacts.

2. **Java Version Mismatch**: The system was using Java 23 as default, but Android Gradle Plugin 8.2.0 requires Java 17+ to run. The project's compile target was set to Java 11, creating version conflicts.

3. **Cache State Corruption**: The Gradle daemon had cached incompatible JDK state from previous builds with different Java versions.

### Solution
Implemented a systematic fix:

#### Step 1: Clear Corrupted Caches
```bash
# Remove custom gradle home cache
rm -rf .gradle-local

# Clear global gradle caches
rm -rf ~/.gradle/caches

# Remove specific problematic cache
rm -rf ~/.gradle/caches/8.2
```

#### Step 2: Stop Gradle Daemon
```bash
./gradlew --stop
```

#### Step 3: Use Correct Java Version
Identified available Java versions:
```bash
/usr/libexec/java_home -V
# Available: Java 23, 21, 11, 8
```

Used Java 21 (meets AGP 8.2.0 requirements) with GRADLE_USER_HOME override:
```bash
JAVA_HOME=/Volumes/Oink_Machine/Library/Java/JavaVirtualMachines/ms-21.0.7/Contents/Home GRADLE_USER_HOME=~/.gradle ./gradlew clean
```

#### Step 4: Build Successfully
```bash
JAVA_HOME=/Volumes/Oink_Machine/Library/Java/JavaVirtualMachines/ms-21.0.7/Contents/Home GRADLE_USER_HOME=~/.gradle ./gradlew assembleDevelopDebug
```

#### Step 5: Permanent Fix
Updated `gradle.properties` to prevent future issues:
```properties
# Disabled problematic custom gradle home
# gradle.user.home=.gradle-local  # Disabled due to cache corruption issues

# Set correct Java home for AGP compatibility
org.gradle.java.home=/Volumes/Oink_Machine/Library/Java/JavaVirtualMachines/ms-21.0.7/Contents/Home
```

### Prevention
- Avoid custom `gradle.user.home` settings that can cause cache corruption
- Ensure Java version compatibility: AGP 8.2.0 requires Java 17+, project compile target can remain Java 11
- Use `org.gradle.java.home` in `gradle.properties` for consistent Java version across builds
- Clear caches when switching between different Java versions

### Related Files
- `gradle.properties` - Updated with Java home configuration
- `app/build.gradle` - Contains Java 11 compile target (unchanged)
- `build.gradle` - Contains AGP 8.2.0 configuration

---

## Issue #2: Android SDK Location Warnings
**Date:** January 18, 2025  
**Severity:** Low  
**Status:** ℹ️ Informational

### Problem
Build shows warnings about Android SDK package locations:
```
This version only understands SDK XML versions up to 3 but an SDK XML file of version 4 was encountered.
Observed package id 'build-tools;34.0.0' in inconsistent location '/Volumes/Oink_Machine/Library/Android/sdk/build-tools/34.0.0' (Expected '/Volumes/Oink_Machine/Library/Android/build-tools/34.0.0')
```

### Root Cause
Android Studio and command-line tools were installed at different times, creating version mismatches in SDK XML format and duplicate package locations.

### Solution
These warnings are informational and don't affect build functionality. The build continues successfully despite these warnings.

### Prevention
- Keep Android Studio and SDK tools updated to compatible versions
- Use consistent SDK installation paths

---

## Issue #3: String Resource Formatting Warnings
**Date:** January 18, 2025  
**Severity:** Low  
**Status:** ℹ️ Informational

### Problem
Build shows warnings about string resource formatting:
```
Multiple substitutions specified in non-positional format of string resource string/detail.intent.share. Did you mean to add the formatted="false" attribute?
```

### Root Cause
String resources in `strings.xml` contain multiple `%s` placeholders without proper formatting attributes.

### Solution
These are linting suggestions, not errors. The build completes successfully. To fix the warnings, add `formatted="false"` attribute to affected string resources:

```xml
<string name="detail.intent.share" formatted="false">Share %s recipe with %s</string>
```

### Prevention
- Use `formatted="false"` attribute for strings with multiple substitutions
- Consider using positional formatting (`%1$s`, `%2$s`) for better maintainability

---

## Build Environment Summary

### Current Configuration
- **Android Gradle Plugin:** 8.2.0
- **Gradle Version:** 8.2
- **Kotlin Version:** 1.9.22
- **Compile SDK:** 34
- **Target SDK:** 34
- **Min SDK:** 24
- **Java Runtime:** 21.0.7 (for AGP)
- **Java Compile Target:** 11 (for app)

### Working Build Command
```bash
./gradlew assembleDevelopDebug
```

### Troubleshooting Commands
```bash
# Clear all caches
rm -rf ~/.gradle/caches .gradle-local

# Stop daemon
./gradlew --stop

# Clean build
./gradlew clean

# Build with specific Java version (if needed)
JAVA_HOME=/Volumes/Oink_Machine/Library/Java/JavaVirtualMachines/ms-21.0.7/Contents/Home ./gradlew assembleDevelopDebug
```

---

*Last Updated: January 18, 2025*
*Documentation Version: 1.0* 