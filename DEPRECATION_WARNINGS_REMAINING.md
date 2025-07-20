# Remaining Deprecation Warnings

## Summary
After fixing critical deprecation warnings (Handler, activeNetworkInfo, adapterPosition), these warnings remain and can be addressed in future updates:

## Low Priority Warnings

### 1. Unused Parameters
- `UserAccountPresenter.kt:14` - Parameter 'isLogin' is never used
- `HeaderWithChipsItem.kt:42` - Parameter 'index' is never used

**Fix**: Remove unused parameters or rename to underscore (_)

### 2. String Case Conversion
- `AppRemoteDataStore.kt:44,90` - 'toLowerCase()' is deprecated

**Fix**: Replace with `lowercase()` method

### 3. Activity Lifecycle
- `BaseActivity.kt:67` - 'onBackPressed()' is deprecated
- `WebviewActivity.kt:41` - 'onBackPressed()' is deprecated

**Fix**: Use OnBackPressedCallback with OnBackPressedDispatcher

### 4. Window Flags
- `RecipeDetailActivity.kt:67` - 'FLAG_TRANSLUCENT_STATUS' is deprecated
- `HomeActivity.kt:36` - 'FLAG_TRANSLUCENT_STATUS' is deprecated

**Fix**: Use WindowInsetsController for API 30+

### 5. Navigation
- `HomeActivity.kt:39` - 'setOnNavigationItemSelectedListener' is deprecated

**Fix**: Use setOnItemSelectedListener

### 6. Activity Result
- `IngredientsActivity.kt:300` - 'startActivityForResult' is deprecated

**Fix**: Use Activity Result API

### 7. Parcelable
- `RecipeListActivity.kt:127` - 'getParcelableArrayList' is deprecated
- `RecipeSortBottomSheetFragment.kt:49` - 'getParcelableArrayList' is deprecated

**Fix**: Use getParcelableArrayListExtra with type parameter for API 33+

### 8. Unnecessary Safe Calls
- `IngredientsActivity.kt:144` - Unnecessary safe call on EditText
- `IngredientsCartAdapter.kt:33` - Unnecessary safe call on ImageView
- `SearchRecipeActivity.kt:177` - Unnecessary safe call on RecyclerView

**Fix**: Remove unnecessary safe call operators (?)

### 9. Parameter Name Mismatch
- `StorageModule.kt:41` - Parameter name mismatch with supertype
- `RecipeDetailActivity.kt:223` - Parameter name mismatch with supertype

**Fix**: Rename parameters to match supertype

## Priority Level
These warnings don't affect functionality and can be addressed when:
- Updating minimum SDK version
- Refactoring specific features
- General code cleanup

## Already Fixed
✅ Handler() constructor → Handler(Looper.getMainLooper())
✅ activeNetworkInfo → NetworkCapabilities API
✅ adapterPosition → bindingAdapterPosition
✅ onNestedScroll → Added @Suppress annotation

---
*Generated: December 2024* 