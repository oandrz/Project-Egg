# Chefnut - Recipe Discovery App 🍳

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_chefnut.png" alt="Chefnut Logo" width="120" height="120"/>
</p>

Chefnut is an Android recipe discovery application that helps users find recipes based on ingredients they have. Using image recognition and a comprehensive recipe database, users can discover, save, and cook delicious meals.

## Features 🌟

- **Ingredient Recognition**: Take photos of ingredients and automatically identify them using Clarifai AI
- **Recipe Search**: Search for recipes by ingredients, dietary restrictions, and cuisine types
- **Recipe Details**: View detailed recipe information including:
  - Ingredients list
  - Step-by-step instructions
  - Nutritional information
  - Cooking time and servings
- **Favorites**: Save your favorite recipes for quick access
- **Shopping List**: Generate shopping lists from recipe ingredients
- **Search History**: Keep track of your recent searches
- **Advanced Filtering**: Filter recipes by:
  - Diet type (vegetarian, vegan, gluten-free, etc.)
  - Cuisine type
  - Preparation time
  - Meal type

## Architecture 🏗️

The app follows the **MVP (Model-View-Presenter)** architecture pattern with:

- **Dagger 2** for dependency injection
- **RxJava 2** for reactive programming
- **Room** for local database management
- **Retrofit 2** for REST API communication

### Project Structure

```
app/
├── data/                   # Data layer (repositories, models, database)
│   ├── local/             # Local data sources (Room database, DAOs)
│   ├── remote/            # Remote data sources (API services)
│   └── model/             # Data models and entities
├── dagger/                # Dependency injection setup
│   ├── component/         # Dagger components
│   ├── module/            # Dagger modules
│   └── scope/             # Custom scopes
├── features/              # Feature modules (following MVP pattern)
│   ├── home/              # Home screen with bottom navigation
│   ├── detail/            # Recipe detail view
│   ├── ingredients/       # Ingredient management
│   ├── recipelist/        # Recipe list with filtering
│   ├── search/            # Recipe search functionality
│   └── userAccount/       # User account management
├── util/                  # Utility classes
└── view/                  # Custom views and UI components
```

## Tech Stack 📚

### Core
- **Language**: Kotlin & Java
- **Min SDK**: 19 (Android 4.4 KitKat)
- **Target SDK**: 29 (Android 10)

### Libraries
- **UI Components**:
  - Material Design Components
  - ConstraintLayout
  - FastAdapter for RecyclerView
  - Material Dialogs
  - CircleImageView

- **Networking**:
  - Retrofit 2.9.0
  - OkHttp 4.7.2
  - Gson

- **Image Loading**:
  - Glide 4.11.0

- **Dependency Injection**:
  - Dagger 2.28

- **Reactive Programming**:
  - RxJava 2.2.19
  - RxAndroid 2.1.1

- **Local Storage**:
  - Room 2.2.5
  - SharedPreferences

- **Firebase**:
  - Firebase Core
  - Firebase Auth
  - Firebase Analytics
  - Cloud Firestore

- **AI/ML**:
  - Clarifai API for image recognition

- **Ads**:
  - AdMob

## Setup & Installation 🚀

### Prerequisites
- Android Studio 4.0 or higher
- JDK 8
- Android SDK

### API Keys
The app requires the following API keys:

1. **Spoonacular API Key**: For recipe data
2. **Clarifai API Key**: For ingredient image recognition
3. **Google Services**: For Firebase integration

### Build Configuration

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/Project-Egg.git
   cd Project-Egg
   ```

2. Add your API keys in the appropriate build variant:
   - Development: `app/build.gradle` (develop flavor)
   - Production: `app/build.gradle` (production flavor)

3. Add `google-services.json` file to:
   - `app/src/debug/` for debug builds
   - `app/src/release/` for release builds

4. Build and run the project in Android Studio

### Build Variants

The app has two product flavors:
- **develop**: For development and testing (uses test AdMob IDs)
- **production**: For production release

And two build types:
- **debug**: Development builds with debugging enabled
- **release**: Production builds with ProGuard minification

## Testing 🧪

The project includes:
- Unit tests using JUnit, Mockito, and Robolectric
- Instrumented tests using Espresso
- Database migration tests for Room

Run tests:
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

## API Integration 🔌

The app integrates with:
- **Spoonacular API**: Main recipe database providing recipe search, details, and nutritional information
- **Clarifai API**: Image recognition for identifying ingredients from photos

## Contributing 🤝

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License 📄

This project is proprietary software. All rights reserved.

Copyright (c) by Andreas (oentoro.andreas@gmail.com)

## Modernization Proposal 🚀

As the technologies used in this project are from 2020, we've created a comprehensive modernization proposal:

- **[MODERNIZATION_SUMMARY.md](MODERNIZATION_SUMMARY.md)** - Executive summary and quick overview
- **[MODERNIZATION_PROPOSAL.md](MODERNIZATION_PROPOSAL.md)** - Detailed technical roadmap with 7 phases
- **[MIGRATION_QUICKSTART.md](MIGRATION_QUICKSTART.md)** - Step-by-step migration guide for developers
- **[TECHNOLOGY_COMPARISON.md](TECHNOLOGY_COMPARISON.md)** - Comparison of old vs new technologies

These documents provide a complete guide to modernizing the app with 2024 best practices including Jetpack Compose, Kotlin Coroutines, Material 3, and more.

## Author ✨

**Andreas Oentoro**
- Email: oentoro.andreas@gmail.com

---

<p align="center">Made with ❤️ for food lovers</p> 