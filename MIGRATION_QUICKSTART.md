# Chefnut App Migration Quick Start Guide

## Day 1: Emergency Updates 🚨

### 1. Update Gradle & Kotlin (30 minutes)
```bash
# In Android Studio: File > Project Structure > Project
# Update:
# - Android Gradle Plugin: 8.2.0
# - Gradle Version: 8.2
```

Update `gradle-wrapper.properties`:
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.2-bin.zip
```

### 2. Fix Compilation Issues (1-2 hours)

Common fixes:
```kotlin
// Old
import kotlinx.android.synthetic.main.activity_home.*

// New - Remove synthetic imports, will migrate to Compose later
// For now, use ViewBinding
```

Enable ViewBinding temporarily:
```kotlin
android {
    buildFeatures {
        viewBinding = true
    }
}
```

### 3. Update Critical Dependencies (1 hour)

```kotlin
// build.gradle.kts (project level)
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}
```

## Week 1: Foundation Updates

### Migrate to KSP (2-3 hours)

1. Add KSP plugin:
```kotlin
plugins {
    id("com.google.devtools.ksp") version "1.9.22-1.0.16"
}
```

2. Replace KAPT with KSP:
```kotlin
// Old
kapt "androidx.room:room-compiler:$roomVersion"
kapt "com.google.dagger:dagger-compiler:$daggerVersion"

// New
ksp("androidx.room:room-compiler:2.6.1")
ksp("com.google.dagger:dagger-compiler:2.48")
```

### Create Version Catalog (2 hours)

1. Create `gradle/libs.versions.toml`
2. Run this script to migrate dependencies:

```kotlin
// Simple migration script
val oldDeps = """
    implementation 'androidx.core:core-ktx:1.6.0'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
""".trimIndent()

// Convert to version catalog format
```

## Week 2-3: Architecture Migration

### RxJava to Coroutines Cheat Sheet

| RxJava | Coroutines |
|--------|------------|
| `Observable<T>` | `Flow<T>` |
| `Single<T>` | `suspend fun(): T` |
| `Completable` | `suspend fun()` |
| `subscribeOn(Schedulers.io())` | `withContext(Dispatchers.IO)` |
| `observeOn(AndroidSchedulers.mainThread())` | Automatic with `viewModelScope` |
| `CompositeDisposable` | `CoroutineScope` |
| `dispose()` | `cancel()` |

### Quick MVP to MVVM Migration

1. Convert Presenter to ViewModel:
```kotlin
// Step 1: Copy presenter logic to ViewModel
class RecipeListViewModel : ViewModel() {
    // Copy presenter methods here
}

// Step 2: Replace View interface with StateFlow
private val _uiState = MutableStateFlow(RecipeListUiState())
val uiState: StateFlow<RecipeListUiState> = _uiState

// Step 3: Update Activity/Fragment
lifecycleScope.launch {
    viewModel.uiState.collect { state ->
        // Update UI
    }
}
```

## Week 4: Start Compose Migration

### Hybrid Approach - Add Compose to Existing Screens

1. Add Compose to existing Activity:
```kotlin
class RecipeListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChefnutTheme {
                RecipeListScreen()
            }
        }
    }
}
```

2. Create Compose version alongside XML:
```kotlin
@Composable
fun RecipeListScreen() {
    // Start simple - just display a list
    LazyColumn {
        items(recipes) { recipe ->
            RecipeItem(recipe)
        }
    }
}
```

## Migration Checklist

### Immediate (Week 1)
- [ ] Update to Gradle 8.2
- [ ] Update Kotlin to 1.9.22
- [ ] Fix compilation errors
- [ ] Update targetSdk to 34
- [ ] Enable ViewBinding
- [ ] Update critical security dependencies

### Short-term (Week 2-4)
- [ ] Migrate from KAPT to KSP
- [ ] Create version catalog
- [ ] Start RxJava to Coroutines migration
- [ ] Convert first Presenter to ViewModel
- [ ] Add Compose dependencies
- [ ] Create first Compose screen

### Medium-term (Month 2-3)
- [ ] Complete architecture migration
- [ ] Migrate 50% of screens to Compose
- [ ] Implement Hilt
- [ ] Add modern testing
- [ ] Setup CI/CD improvements

## Common Issues & Solutions

### Issue: "Unresolved reference: kotlinx"
```kotlin
// Add to dependencies
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

### Issue: "Cannot find symbol BR"
```kotlin
// Migrate from DataBinding to ViewBinding or Compose
```

### Issue: Room migration errors
```kotlin
// Add migration or allow destructive migration for development
Room.databaseBuilder(...)
    .fallbackToDestructiveMigration() // Only for development!
    .build()
```

## Useful Scripts

### Find all RxJava usages:
```bash
grep -r "Observable\|Single\|Completable\|Flowable" --include="*.kt" --include="*.java" .
```

### Find all synthetic imports:
```bash
grep -r "kotlinx.android.synthetic" --include="*.kt" .
```

### Count XML layouts to migrate:
```bash
find . -name "*.xml" -path "*/layout/*" | wc -l
```

## Resources & Tools

### Essential Documentation
- [Migrate to Kotlin DSL](https://developer.android.com/studio/build/migrate-to-kts)
- [Compose Migration Guide](https://developer.android.com/jetpack/compose/migrate)
- [Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)

### Helpful Tools
- **[Jetpack Compose Converter](https://www.composables.com/xml2compose)** - Convert XML to Compose
- **[Dependency Analysis Plugin](https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin)** - Find unused dependencies
- **[Compose BOM](https://developer.android.com/jetpack/compose/bom)** - Manage Compose versions

### Code Generation
```kotlin
// Android Studio Live Templates for Compose
// Settings > Editor > Live Templates > Kotlin

// Template: comp
@Composable
fun $NAME$($PARAMS$) {
    $END$
}

// Template: prev
@Preview(showBackground = true)
@Composable
fun $NAME$Preview() {
    ChefnutTheme {
        $NAME$()
    }
}
```

## Support & Troubleshooting

### Community Resources
- [Android Developers Slack](https://androidstudygroup.slack.com/)
- [Kotlin Slack](https://kotlinlang.slack.com/)
- [Stack Overflow - android-jetpack-compose](https://stackoverflow.com/questions/tagged/android-jetpack-compose)

### When Stuck
1. Check the [migration guide](https://developer.android.com/jetpack/compose/migrate)
2. Search for the error message + "compose" or "kotlin"
3. Use Android Studio's quick fixes (Alt+Enter)
4. Ask in community channels with a minimal reproducible example

Remember: **Migration is iterative!** Don't try to do everything at once. Focus on getting the app running with minimal changes first, then improve incrementally. 