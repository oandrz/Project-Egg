package starbright.com.projectegg.compose.ui.screens.home

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
import starbright.com.projectegg.data.model.Recipe

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_showsLoadingIndicator_whenLoading() {
        // Given
        val uiState = HomeUiState(isLoading = true)
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                HomeScreen(
                    uiState = uiState,
                    onSearchClick = {},
                    onRecipeClick = {},
                    onLoadMore = {},
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Loading recipes")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsHeroBanner_withText() {
        // Given
        val uiState = HomeUiState(isLoading = false)
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                HomeScreen(
                    uiState = uiState,
                    onSearchClick = {},
                    onRecipeClick = {},
                    onLoadMore = {},
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("What do you like to cook?")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsSearchBar() {
        // Given
        val uiState = HomeUiState(isLoading = false)
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                HomeScreen(
                    uiState = uiState,
                    onSearchClick = {},
                    onRecipeClick = {},
                    onLoadMore = {},
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Search by dish")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsRecipeCards_whenRecipesLoaded() {
        // Given
        val recipes = listOf(
            Recipe(
                id = "1",
                title = "Pasta Carbonara",
                image = "pasta.jpg",
                readyInMinutes = 30,
                servings = 4,
                sourceName = "Italian Kitchen"
            ),
            Recipe(
                id = "2",
                title = "Caesar Salad",
                image = "salad.jpg",
                readyInMinutes = 15,
                servings = 2,
                sourceName = "Healthy Eats"
            )
        )
        val uiState = HomeUiState(isLoading = false, recipes = recipes)
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                HomeScreen(
                    uiState = uiState,
                    onSearchClick = {},
                    onRecipeClick = {},
                    onLoadMore = {},
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
        composeTestRule
            .onNodeWithText("30 Min")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("4 Peoples")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsError_whenErrorOccurs() {
        // Given
        val uiState = HomeUiState(
            isLoading = false,
            error = "Network connection failed"
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                HomeScreen(
                    uiState = uiState,
                    onSearchClick = {},
                    onRecipeClick = {},
                    onLoadMore = {},
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Network connection failed")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_triggersSearchClick_whenSearchBarClicked() {
        // Given
        var searchClicked = false
        val uiState = HomeUiState(isLoading = false)
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                HomeScreen(
                    uiState = uiState,
                    onSearchClick = { searchClicked = true },
                    onRecipeClick = {},
                    onLoadMore = {},
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Search by dish")
            .performClick()
        
        assert(searchClicked) { "Search click should be triggered" }
    }

    @Test
    fun homeScreen_triggersRecipeClick_whenRecipeCardClicked() {
        // Given
        var clickedRecipeId = ""
        val recipes = listOf(
            Recipe(
                id = "123",
                title = "Test Recipe",
                image = "test.jpg",
                readyInMinutes = 30,
                servings = 4,
                sourceName = "Test Source"
            )
        )
        val uiState = HomeUiState(isLoading = false, recipes = recipes)
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                HomeScreen(
                    uiState = uiState,
                    onSearchClick = {},
                    onRecipeClick = { id -> clickedRecipeId = id },
                    onLoadMore = {},
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
} 