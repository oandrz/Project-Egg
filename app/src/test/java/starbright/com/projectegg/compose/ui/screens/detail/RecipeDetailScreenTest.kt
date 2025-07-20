package starbright.com.projectegg.compose.ui.screens.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Instruction
import starbright.com.projectegg.data.model.Recipe

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class RecipeDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockRecipe = Recipe(
        id = 123,
        title = "Pasta Carbonara",
        image = "pasta.jpg",
        cookingMinutes = 30,
        servingCount = 4,
        calories = 500,
        sourceStringUrl = "http://example.com",
        sourceName = "Italian Kitchen",
        ingredients = listOf(
            Ingredient(
                id = 1,
                name = "Pasta",
                original = "400g pasta",
                amount = 400.0,
                unit = "g"
            ),
            Ingredient(
                id = 2,
                name = "Eggs",
                original = "4 eggs",
                amount = 4.0,
                unit = ""
            )
        ),
        instructions = listOf(
            Instruction(
                name = "Preparation",
                steps = listOf(
                    "Boil water for pasta",
                    "Beat eggs in a bowl"
                )
            ),
            Instruction(
                name = "Cooking",
                steps = listOf(
                    "Cook pasta according to package",
                    "Mix eggs with pasta"
                )
            )
        ),
        dishTypes = listOf("main course", "dinner"),
        cuisines = listOf("Italian", "European")
    )

    @Test
    fun recipeDetailScreen_showsLoadingIndicator_whenLoading() {
        // Given
        val uiState = RecipeDetailUiState(isLoading = true)
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                RecipeDetailScreen(
                    uiState = uiState,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onIngredientChecked = { _, _ -> },
                    onAddToCartClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Loading recipe details")
            .assertIsDisplayed()
    }

    @Test
    fun recipeDetailScreen_showsRecipeDetails_whenLoaded() {
        // Given
        val uiState = RecipeDetailUiState(
            isLoading = false,
            recipe = mockRecipe,
            isFavorite = false
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                RecipeDetailScreen(
                    uiState = uiState,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onIngredientChecked = { _, _ -> },
                    onAddToCartClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Pasta Carbonara")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("30 Min")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("4 Peoples")
            .assertIsDisplayed()
    }

    @Test
    fun recipeDetailScreen_showsFavoriteState_correctly() {
        // Given - not favorite
        val uiState = RecipeDetailUiState(
            isLoading = false,
            recipe = mockRecipe,
            isFavorite = false
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                RecipeDetailScreen(
                    uiState = uiState,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onIngredientChecked = { _, _ -> },
                    onAddToCartClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Add to favorites")
            .assertIsDisplayed()

        // Given - is favorite
        composeTestRule.setContent {
            ChefnutTheme {
                RecipeDetailScreen(
                    uiState = uiState.copy(isFavorite = true),
                    onBackClick = {},
                    onFavoriteClick = {},
                    onIngredientChecked = { _, _ -> },
                    onAddToCartClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Remove from favorites")
            .assertIsDisplayed()
    }

    @Test
    fun recipeDetailScreen_showsIngredients_withCheckboxes() {
        // Given
        val uiState = RecipeDetailUiState(
            isLoading = false,
            recipe = mockRecipe,
            isFavorite = false
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                RecipeDetailScreen(
                    uiState = uiState,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onIngredientChecked = { _, _ -> },
                    onAddToCartClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Ingredients")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("400g pasta")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("4 eggs")
            .assertIsDisplayed()
    }

    @Test
    fun recipeDetailScreen_showsInstructions() {
        // Given
        val uiState = RecipeDetailUiState(
            isLoading = false,
            recipe = mockRecipe,
            isFavorite = false
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                RecipeDetailScreen(
                    uiState = uiState,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onIngredientChecked = { _, _ -> },
                    onAddToCartClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Instructions")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Preparation")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("1. Boil water for pasta")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("2. Beat eggs in a bowl")
            .assertIsDisplayed()
    }

    @Test
    fun recipeDetailScreen_triggersIngredientCheck_whenCheckboxClicked() {
        // Given
        var checkedIngredientId = -1
        var isChecked = false
        val uiState = RecipeDetailUiState(
            isLoading = false,
            recipe = mockRecipe,
            isFavorite = false
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                RecipeDetailScreen(
                    uiState = uiState,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onIngredientChecked = { id, checked ->
                        checkedIngredientId = id
                        isChecked = checked
                    },
                    onAddToCartClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithTag("ingredient_checkbox_1")
            .performClick()
        
        assert(checkedIngredientId == 1) { "Should pass correct ingredient id" }
        assert(isChecked) { "Should be checked" }
    }

    @Test
    fun recipeDetailScreen_showsSourceButton_whenSourceAvailable() {
        // Given
        val uiState = RecipeDetailUiState(
            isLoading = false,
            recipe = mockRecipe,
            isFavorite = false
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                RecipeDetailScreen(
                    uiState = uiState,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onIngredientChecked = { _, _ -> },
                    onAddToCartClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("View Source")
            .assertIsDisplayed()
    }

    @Test
    fun recipeDetailScreen_showsError_whenErrorOccurs() {
        // Given
        val uiState = RecipeDetailUiState(
            isLoading = false,
            error = "Failed to load recipe"
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                RecipeDetailScreen(
                    uiState = uiState,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onIngredientChecked = { _, _ -> },
                    onAddToCartClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Failed to load recipe")
            .assertIsDisplayed()
    }

    @Test
    fun recipeDetailScreen_triggersBackClick_whenBackPressed() {
        // Given
        var backClicked = false
        val uiState = RecipeDetailUiState(
            isLoading = false,
            recipe = mockRecipe,
            isFavorite = false
        )
        
        // When
        composeTestRule.setContent {
            ChefnutTheme {
                RecipeDetailScreen(
                    uiState = uiState,
                    onBackClick = { backClicked = true },
                    onFavoriteClick = {},
                    onIngredientChecked = { _, _ -> },
                    onAddToCartClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()
        
        assert(backClicked) { "Should trigger back click" }
    }
} 