package starbright.com.projectegg.compose.ui.screens.favorites

import io.reactivex.Completable
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
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.util.scheduler.TestSchedulerProvider

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoritesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = androidx.arch.core.InstantTaskExecutorRule()

    @Mock
    private lateinit var appRepository: AppRepository

    private lateinit var schedulerProvider: TestSchedulerProvider
    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setup() {
        schedulerProvider = TestSchedulerProvider()
        viewModel = FavoritesViewModel(appRepository, schedulerProvider)
    }

    @Test
    fun `loadFavorites should update state with empty list when no favorites`() = runTest {
        // Given
        `when`(appRepository.loadFavouriteRecipe())
            .thenReturn(Single.just(emptyList()))

        // When
        viewModel.loadFavorites()
        schedulerProvider.triggerActions()

        // Then
        val state = viewModel.uiState.value
        assert(!state.isLoading) { "Should not be loading after success" }
        assert(state.favorites.isEmpty()) { "Should have empty favorites list" }
        assert(state.error == null) { "Should have no error" }
    }

    @Test
    fun `loadFavorites should update state with favorites on success`() = runTest {
        // Given
        val mockFavorites = listOf(
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
        
        `when`(appRepository.loadFavouriteRecipe())
            .thenReturn(Single.just(mockFavorites))

        // When
        viewModel.loadFavorites()
        schedulerProvider.triggerActions()

        // Then
        val state = viewModel.uiState.value
        assert(!state.isLoading) { "Should not be loading after success" }
        assert(state.error == null) { "Should have no error" }
        assert(state.favorites == mockFavorites) { "Should have loaded favorites" }
    }

    @Test
    fun `loadFavorites should update state with error on failure`() = runTest {
        // Given
        val error = Exception("Database error")
        `when`(appRepository.loadFavouriteRecipe())
            .thenReturn(Single.error(error))

        // When
        viewModel.loadFavorites()
        schedulerProvider.triggerActions()

        // Then
        val state = viewModel.uiState.value
        assert(!state.isLoading) { "Should not be loading after error" }
        assert(state.error == "Database error") { "Should have error message" }
        assert(state.favorites.isEmpty()) { "Should have no favorites on error" }
    }

    @Test
    fun `removeFavorite should delete recipe and reload favorites`() = runTest {
        // Given
        val recipeId = "123"
        val remainingFavorites = listOf(
            FavouriteRecipe(
                id = "456",
                title = "Other Recipe",
                image = "other.jpg",
                readyInMinutes = 20,
                servings = 2,
                sourceName = "Test"
            )
        )
        
        `when`(appRepository.deleteFavouriteRecipeById(recipeId))
            .thenReturn(Completable.complete())
        `when`(appRepository.loadFavouriteRecipe())
            .thenReturn(Single.just(remainingFavorites))

        // When
        viewModel.removeFavorite(recipeId)
        schedulerProvider.triggerActions()

        // Then
        verify(appRepository).deleteFavouriteRecipeById(recipeId)
        val state = viewModel.uiState.value
        assert(state.favorites == remainingFavorites) { "Should reload favorites after deletion" }
    }

    @Test
    fun `onRecipeClicked should trigger navigation event`() = runTest {
        // Given
        val recipeId = "123"

        // When
        viewModel.onRecipeClicked(recipeId)

        // Then
        val event = viewModel.navigationEvent.value
        assert(event is FavoritesNavigationEvent.NavigateToRecipeDetail) { 
            "Should emit NavigateToRecipeDetail event" 
        }
        assert((event as FavoritesNavigationEvent.NavigateToRecipeDetail).recipeId == recipeId) {
            "Should pass correct recipe id"
        }
    }
} 