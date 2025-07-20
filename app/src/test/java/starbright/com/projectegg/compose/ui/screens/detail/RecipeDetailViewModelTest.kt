package starbright.com.projectegg.compose.ui.screens.detail

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.RecipeRepository
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Instruction
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.util.scheduler.TestSchedulerProvider

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RecipeDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = androidx.arch.core.InstantTaskExecutorRule()

    @Mock
    private lateinit var recipeRepository: RecipeRepository

    @Mock
    private lateinit var appRepository: AppRepository

    private lateinit var schedulerProvider: TestSchedulerProvider
    private lateinit var viewModel: RecipeDetailViewModel

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

    @Before
    fun setup() {
        schedulerProvider = TestSchedulerProvider()
        viewModel = RecipeDetailViewModel(recipeRepository, appRepository, schedulerProvider)
    }

    @Test
    fun `loadRecipeDetail should update state with recipe on success`() = runTest {
        // Given
        val recipeId = "123"
        `when`(recipeRepository.getRecipeDetailInformation(recipeId))
            .thenReturn(Observable.just(mockRecipe))
        `when`(appRepository.checkIfRecipeIsFavourite(recipeId))
            .thenReturn(Single.just(true))

        // When
        viewModel.loadRecipeDetail(recipeId)
        schedulerProvider.triggerActions()

        // Then
        val state = viewModel.uiState.value
        assert(!state.isLoading) { "Should not be loading after success" }
        assert(state.recipe == mockRecipe) { "Should have loaded recipe" }
        assert(state.isFavorite) { "Should be marked as favorite" }
        assert(state.error == null) { "Should have no error" }
    }

    @Test
    fun `loadRecipeDetail should handle error`() = runTest {
        // Given
        val recipeId = "123"
        val error = Exception("Network error")
        `when`(recipeRepository.getRecipeDetailInformation(recipeId))
            .thenReturn(Observable.error(error))

        // When
        viewModel.loadRecipeDetail(recipeId)
        schedulerProvider.triggerActions()

        // Then
        val state = viewModel.uiState.value
        assert(!state.isLoading) { "Should not be loading after error" }
        assert(state.recipe == null) { "Should have no recipe on error" }
        assert(state.error == "Network error") { "Should have error message" }
    }

    @Test
    fun `toggleFavorite should add recipe to favorites when not favorite`() = runTest {
        // Given
        val recipeId = "123"
        `when`(recipeRepository.getRecipeDetailInformation(recipeId))
            .thenReturn(Observable.just(mockRecipe))
        `when`(appRepository.checkIfRecipeIsFavourite(recipeId))
            .thenReturn(Single.just(false))
        
        viewModel.loadRecipeDetail(recipeId)
        schedulerProvider.triggerActions()
        
        // When
        `when`(appRepository.insertFavouriteRecipe(mockRecipe))
            .thenReturn(Completable.complete())
        
        viewModel.toggleFavorite()
        schedulerProvider.triggerActions()

        // Then
        verify(appRepository).insertFavouriteRecipe(mockRecipe)
        val state = viewModel.uiState.value
        assert(state.isFavorite) { "Should be marked as favorite" }
    }

    @Test
    fun `toggleFavorite should remove recipe from favorites when already favorite`() = runTest {
        // Given
        val recipeId = "123"
        `when`(recipeRepository.getRecipeDetailInformation(recipeId))
            .thenReturn(Observable.just(mockRecipe))
        `when`(appRepository.checkIfRecipeIsFavourite(recipeId))
            .thenReturn(Single.just(true))
        
        viewModel.loadRecipeDetail(recipeId)
        schedulerProvider.triggerActions()
        
        // When
        `when`(appRepository.deleteFavouriteRecipeById(recipeId))
            .thenReturn(Completable.complete())
        
        viewModel.toggleFavorite()
        schedulerProvider.triggerActions()

        // Then
        verify(appRepository).deleteFavouriteRecipeById(recipeId)
        val state = viewModel.uiState.value
        assert(!state.isFavorite) { "Should not be marked as favorite" }
    }

    @Test
    fun `onIngredientChecked should update ingredient selection`() = runTest {
        // Given
        val recipeId = "123"
        `when`(recipeRepository.getRecipeDetailInformation(recipeId))
            .thenReturn(Observable.just(mockRecipe))
        `when`(appRepository.checkIfRecipeIsFavourite(recipeId))
            .thenReturn(Single.just(false))
        
        viewModel.loadRecipeDetail(recipeId)
        schedulerProvider.triggerActions()

        // When
        viewModel.onIngredientChecked(1, true)

        // Then
        val state = viewModel.uiState.value
        assert(state.selectedIngredients.contains(1)) { "Should add ingredient to selected" }

        // When - uncheck
        viewModel.onIngredientChecked(1, false)

        // Then
        val updatedState = viewModel.uiState.value
        assert(!updatedState.selectedIngredients.contains(1)) { "Should remove ingredient from selected" }
    }

    @Test
    fun `onAddToCartClick should trigger navigation event with selected ingredients`() = runTest {
        // Given
        val recipeId = "123"
        `when`(recipeRepository.getRecipeDetailInformation(recipeId))
            .thenReturn(Observable.just(mockRecipe))
        `when`(appRepository.checkIfRecipeIsFavourite(recipeId))
            .thenReturn(Single.just(false))
        
        viewModel.loadRecipeDetail(recipeId)
        schedulerProvider.triggerActions()
        
        viewModel.onIngredientChecked(1, true)
        viewModel.onIngredientChecked(2, true)

        // When
        viewModel.onAddToCartClick()

        // Then
        val event = viewModel.navigationEvent.value
        assert(event is RecipeDetailNavigationEvent.NavigateToCart) { 
            "Should emit NavigateToCart event" 
        }
        val cartEvent = event as RecipeDetailNavigationEvent.NavigateToCart
        assert(cartEvent.ingredients.size == 2) { "Should include selected ingredients" }
        assert(cartEvent.ingredients.any { it.id == 1 }) { "Should include first ingredient" }
        assert(cartEvent.ingredients.any { it.id == 2 }) { "Should include second ingredient" }
    }

    @Test
    fun `onSourceClick should trigger navigation event when source URL exists`() = runTest {
        // Given
        val recipeId = "123"
        `when`(recipeRepository.getRecipeDetailInformation(recipeId))
            .thenReturn(Observable.just(mockRecipe))
        `when`(appRepository.checkIfRecipeIsFavourite(recipeId))
            .thenReturn(Single.just(false))
        
        viewModel.loadRecipeDetail(recipeId)
        schedulerProvider.triggerActions()

        // When
        viewModel.onSourceClick()

        // Then
        val event = viewModel.navigationEvent.value
        assert(event is RecipeDetailNavigationEvent.OpenSourceUrl) { 
            "Should emit OpenSourceUrl event" 
        }
        assert((event as RecipeDetailNavigationEvent.OpenSourceUrl).url == "http://example.com") {
            "Should pass correct URL"
        }
    }

    @Test
    fun `onSourceClick should not trigger event when no source URL`() = runTest {
        // Given
        val recipeWithoutUrl = mockRecipe.copy(sourceStringUrl = null)
        val recipeId = "123"
        `when`(recipeRepository.getRecipeDetailInformation(recipeId))
            .thenReturn(Observable.just(recipeWithoutUrl))
        `when`(appRepository.checkIfRecipeIsFavourite(recipeId))
            .thenReturn(Single.just(false))
        
        viewModel.loadRecipeDetail(recipeId)
        schedulerProvider.triggerActions()

        // When
        viewModel.onSourceClick()

        // Then
        val event = viewModel.navigationEvent.value
        assert(event == null) { "Should not emit any event when no URL" }
    }
} 