# Running and Verifying the Jetpack Compose Migration

## Prerequisites
1. Open the project in Android Studio
2. Sync the project with Gradle files
3. Ensure you have an Android emulator or device ready

## Running the App

### Option 1: Android Studio
1. Click the "Run" button (green play icon) in Android Studio
2. Select your target device/emulator
3. Wait for the app to build and install

### Option 2: Command Line (if Gradle issues are resolved)
```bash
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n starbright.com.projectegg/.features.SplashScreenActivity
```

## What to Verify

### 1. App Launch
- [ ] App launches without crashing
- [ ] Splash screen appears with Chefnut logo
- [ ] Automatically navigates to Home screen after delay

### 2. Home Screen
- [ ] Hero banner image displays
- [ ] "Discover Best Recipe" title shows
- [ ] Search bar is visible and clickable
- [ ] Recipe list loads (may need API key configured)
- [ ] Bottom navigation shows Home and Favourite tabs
- [ ] Pull-to-refresh works
- [ ] Infinite scroll loads more recipes

### 3. Search Screen
- [ ] Clicking search bar navigates to search screen
- [ ] Back button returns to home
- [ ] Search history displays (if any)
- [ ] Typing in search bar triggers recipe search
- [ ] Search results display as recipe cards
- [ ] Clicking a recipe navigates to detail screen

### 4. Favorites Screen
- [ ] Bottom nav "Favourite" tab navigates correctly
- [ ] Empty state shows if no favorites
- [ ] "Discover Recipes" button returns to Home
- [ ] Favorited recipes display correctly
- [ ] Can remove favorites with delete icon

### 5. Recipe Detail Screen
- [ ] Recipe image loads
- [ ] Title, time, and servings display
- [ ] Ingredients list with checkboxes
- [ ] Can select/deselect ingredients
- [ ] FAB appears when ingredients selected
- [ ] Instructions display with steps
- [ ] Favorite icon toggles correctly
- [ ] Source button shows (if available)
- [ ] Back button returns to previous screen

## Known Issues to Check

### 1. Dependency Injection
Since DI is not yet integrated, you'll see placeholder comments in MainScreen.kt where ViewModels should be injected. The app won't fully function until ViewModels are connected.

### 2. API Configuration
Ensure your API keys are properly configured in:
- `local.properties` or
- `gradle.properties`

### 3. Expected Crashes
The app will likely crash when navigating to screens because ViewModels aren't injected yet. This is expected and will be resolved with DI integration.

## Temporary Testing Solution

To test individual screens without DI, you can temporarily modify the navigation composables to use mock data:

```kotlin
// In MainScreen.kt, temporarily add:
composable(Screen.Home.route) {
    HomeScreen(
        uiState = HomeUiState(
            isLoading = false,
            recipes = listOf(/* mock recipes */)
        ),
        onSearchClick = { /* log */ },
        onRecipeClick = { /* log */ },
        onLoadMore = { /* log */ },
        onRefresh = { /* log */ }
    )
}
```

## Next Steps

1. **Integrate Dagger**: Connect all ViewModels to their screens
2. **Add ViewModel Providers**: Use ViewModelProvider or Hilt integration
3. **Test Complete Flow**: Once DI is integrated, test the full app flow
4. **Fix Any Runtime Issues**: Address any crashes or bugs found
5. **Performance Testing**: Check for UI jank or slow loading

## Success Criteria

Once DI is integrated, the app should:
- Navigate between all screens smoothly
- Load and display recipe data
- Save and retrieve favorites
- Search recipes in real-time
- Handle errors gracefully
- Maintain state across configuration changes 