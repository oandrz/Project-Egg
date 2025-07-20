package starbright.com.projectegg.compose.ui.screens.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme
import starbright.com.projectegg.data.model.RecipeResponse
import starbright.com.projectegg.data.model.local.SearchHistory

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchScreen_showsSearchBarAndBackButton() {
        // Given
        val uiState = SearchUiState()
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                SearchScreen(
                    uiState = uiState,
                    onSearchQueryChanged = {},
                    onBackClick = {},
                    onRecipeClick = {},
                    onSearchHistoryClick = {},
                    onDeleteSearchHistory = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Back")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Search Recipe")
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_showsSearchHistory_whenNotSearching() {
        // Given
        val searchHistory = listOf(
            SearchHistory(id = 1, query = "pasta"),
            SearchHistory(id = 2, query = "salad"),
            SearchHistory(id = 3, query = "chicken")
        )
        val uiState = SearchUiState(
            isSearching = false,
            searchHistory = searchHistory
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                SearchScreen(
                    uiState = uiState,
                    onSearchQueryChanged = {},
                    onBackClick = {},
                    onRecipeClick = {},
                    onSearchHistoryClick = {},
                    onDeleteSearchHistory = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Search History")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("pasta")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("salad")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("chicken")
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_showsEmptySearchHistory_whenNoHistory() {
        // Given
        val uiState = SearchUiState(
            isSearching = false,
            searchHistory = emptyList()
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                SearchScreen(
                    uiState = uiState,
                    onSearchQueryChanged = {},
                    onBackClick = {},
                    onRecipeClick = {},
                    onSearchHistoryClick = {},
                    onDeleteSearchHistory = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("No search history")
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_showsSearchResults_whenSearching() {
        // Given
        val searchResults = listOf(
            RecipeResponse(
                id = "1",
                title = "Chicken Parmesan",
                image = "chicken1.jpg",
                readyInMinutes = 45,
                servings = 4,
                sourceUrl = "http://example.com"
            ),
            RecipeResponse(
                id = "2",
                title = "Grilled Chicken",
                image = "chicken2.jpg",
                readyInMinutes = 30,
                servings = 2,
                sourceUrl = "http://example.com"
            )
        )
        val uiState = SearchUiState(
            isSearching = true,
            currentQuery = "chicken",
            searchResults = searchResults
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                SearchScreen(
                    uiState = uiState,
                    onSearchQueryChanged = {},
                    onBackClick = {},
                    onRecipeClick = {},
                    onSearchHistoryClick = {},
                    onDeleteSearchHistory = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Chicken Parmesan")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Grilled Chicken")
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_showsLoadingIndicator_whenLoading() {
        // Given
        val uiState = SearchUiState(
            isSearching = true,
            isLoading = true,
            currentQuery = "test"
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                SearchScreen(
                    uiState = uiState,
                    onSearchQueryChanged = {},
                    onBackClick = {},
                    onRecipeClick = {},
                    onSearchHistoryClick = {},
                    onDeleteSearchHistory = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Searching recipes")
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_showsEmptyResults_whenNoResults() {
        // Given
        val uiState = SearchUiState(
            isSearching = true,
            isLoading = false,
            currentQuery = "xyz",
            searchResults = emptyList()
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                SearchScreen(
                    uiState = uiState,
                    onSearchQueryChanged = {},
                    onBackClick = {},
                    onRecipeClick = {},
                    onSearchHistoryClick = {},
                    onDeleteSearchHistory = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("No recipes found")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Try searching with different keywords")
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_triggersSearchQueryChanged_whenTyping() {
        // Given
        var searchQuery = ""
        val uiState = SearchUiState()
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                SearchScreen(
                    uiState = uiState,
                    onSearchQueryChanged = { query -> searchQuery = query },
                    onBackClick = {},
                    onRecipeClick = {},
                    onSearchHistoryClick = {},
                    onDeleteSearchHistory = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Search Recipe")
            .performTextInput("pasta")
        
        assert(searchQuery == "pasta") { "Search query should be updated" }
    }

    @Test
    fun searchScreen_triggersRecipeClick_whenRecipeClicked() {
        // Given
        var clickedRecipeId = ""
        val searchResults = listOf(
            RecipeResponse(
                id = "123",
                title = "Test Recipe",
                image = "test.jpg",
                readyInMinutes = 30,
                servings = 4,
                sourceUrl = "http://example.com"
            )
        )
        val uiState = SearchUiState(
            isSearching = true,
            searchResults = searchResults
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                SearchScreen(
                    uiState = uiState,
                    onSearchQueryChanged = {},
                    onBackClick = {},
                    onRecipeClick = { id -> clickedRecipeId = id },
                    onSearchHistoryClick = {},
                    onDeleteSearchHistory = {}
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
    fun searchScreen_triggersHistoryClick_whenHistoryItemClicked() {
        // Given
        var clickedQuery = ""
        val searchHistory = listOf(
            SearchHistory(id = 1, query = "pasta")
        )
        val uiState = SearchUiState(
            isSearching = false,
            searchHistory = searchHistory
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                SearchScreen(
                    uiState = uiState,
                    onSearchQueryChanged = {},
                    onBackClick = {},
                    onRecipeClick = {},
                    onSearchHistoryClick = { query -> clickedQuery = query },
                    onDeleteSearchHistory = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("pasta")
            .performClick()
        
        assert(clickedQuery == "pasta") { "History click should pass correct query" }
    }
} 