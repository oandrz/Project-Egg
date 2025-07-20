package starbright.com.projectegg.compose.ui.screens.favorites

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme
import starbright.com.projectegg.data.model.local.FavouriteRecipe

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun favoritesScreen_showsLoadingIndicator_whenLoading() {
        // Given
        val uiState = FavoritesUiState(isLoading = true)
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                FavoritesScreen(
                    uiState = uiState,
                    onRecipeClick = {},
                    onRemoveFavorite = {},
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Loading favorites")
            .assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_showsEmptyState_whenNoFavorites() {
        // Given
        val uiState = FavoritesUiState(isLoading = false, favorites = emptyList())
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                FavoritesScreen(
                    uiState = uiState,
                    onRecipeClick = {},
                    onRemoveFavorite = {},
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("No Recipe Bookmarked Yet")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Don't forget to bookmark your favorite recipe")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Empty favorites illustration")
            .assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_showsDiscoverButton_inEmptyState() {
        // Given
        val uiState = FavoritesUiState(isLoading = false, favorites = emptyList())
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                FavoritesScreen(
                    uiState = uiState,
                    onRecipeClick = {},
                    onRemoveFavorite = {},
                    onRefresh = {},
                    onDiscoverRecipes = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Discover Recipes")
            .assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_showsFavoriteCards_whenFavoritesLoaded() {
        // Given
        val favorites = listOf(
            FavouriteRecipe(
                id = "1",
                title = "Pasta Carbonara",
                image = "pasta.jpg",
                readyInMinutes = 30,
                servings = 4,
                sourceName = "Italian Kitchen"
            ),
            FavouriteRecipe(
                id = "2",
                title = "Caesar Salad",
                image = "salad.jpg",
                readyInMinutes = 15,
                servings = 2,
                sourceName = "Healthy Eats"
            )
        )
        val uiState = FavoritesUiState(isLoading = false, favorites = favorites)
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                FavoritesScreen(
                    uiState = uiState,
                    onRecipeClick = {},
                    onRemoveFavorite = {},
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Pasta Carbonara")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Caesar Salad")
            .assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_showsError_whenErrorOccurs() {
        // Given
        val uiState = FavoritesUiState(
            isLoading = false,
            error = "Failed to load favorites"
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                FavoritesScreen(
                    uiState = uiState,
                    onRecipeClick = {},
                    onRemoveFavorite = {},
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Failed to load favorites")
            .assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_triggersRecipeClick_whenCardClicked() {
        // Given
        var clickedRecipeId = ""
        val favorites = listOf(
            FavouriteRecipe(
                id = "123",
                title = "Test Recipe",
                image = "test.jpg",
                readyInMinutes = 30,
                servings = 4,
                sourceName = "Test Source"
            )
        )
        val uiState = FavoritesUiState(isLoading = false, favorites = favorites)
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                FavoritesScreen(
                    uiState = uiState,
                    onRecipeClick = { id -> clickedRecipeId = id },
                    onRemoveFavorite = {},
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Test Recipe")
            .performClick()
        
        assert(clickedRecipeId == "123") { "Recipe click should pass correct id" }
    }

    @Test
    fun favoritesScreen_triggersDiscoverClick_whenDiscoverButtonClicked() {
        // Given
        var discoverClicked = false
        val uiState = FavoritesUiState(isLoading = false, favorites = emptyList())
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                FavoritesScreen(
                    uiState = uiState,
                    onRecipeClick = {},
                    onRemoveFavorite = {},
                    onRefresh = {},
                    onDiscoverRecipes = { discoverClicked = true }
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Discover Recipes")
            .performClick()
        
        assert(discoverClicked) { "Discover button should trigger callback" }
    }
} 