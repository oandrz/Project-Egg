package starbright.com.projectegg.compose.ui.screens.search

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
import org.mockito.Mockito.never
import org.mockito.junit.MockitoJUnitRunner
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.RecipeRepository
import starbright.com.projectegg.data.model.RecipeResponse
import starbright.com.projectegg.data.model.local.SearchHistory
import starbright.com.projectegg.util.scheduler.TestSchedulerProvider

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = androidx.arch.core.InstantTaskExecutorRule()

    @Mock
    private lateinit var recipeRepository: RecipeRepository

    @Mock
    private lateinit var appRepository: AppRepository

    private lateinit var schedulerProvider: TestSchedulerProvider
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        schedulerProvider = TestSchedulerProvider()
        viewModel = SearchViewModel(recipeRepository, appRepository, schedulerProvider)
    }

    @Test
    fun `initial state should show search history`() = runTest {
        // Given
        val mockHistory = listOf(
            SearchHistory(id = 1, query = "pasta"),
            SearchHistory(id = 2, query = "salad")
        )
        `when`(appRepository.loadSearchHistory())
            .thenReturn(Single.just(mockHistory))

        // When
        viewModel.loadSearchHistory()
        schedulerProvider.triggerActions()

        // Then
        val state = viewModel.uiState.value
        assert(!state.isSearching) { "Should not be searching initially" }
        assert(!state.isLoading) { "Should not be loading after history loaded" }
        assert(state.searchResults.isEmpty()) { "Should have no search results" }
        assert(state.searchHistory == mockHistory) { "Should have loaded search history" }
        assert(state.error == null) { "Should have no error" }
    }

    @Test
    fun `searchRecipes should update state with results`() = runTest {
        // Given
        val query = "chicken"
        val mockRecipes = listOf(
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
        
        `when`(recipeRepository.getSearchRecipes(query))
            .thenReturn(Single.just(mockRecipes))
        `when`(appRepository.insertSearchHistory(query))
            .thenReturn(Completable.complete())

        // When
        viewModel.searchRecipes(query)
        schedulerProvider.triggerActions()

        // Then
        val state = viewModel.uiState.value
        assert(state.isSearching) { "Should be in searching mode" }
        assert(!state.isLoading) { "Should not be loading after results" }
        assert(state.searchResults == mockRecipes) { "Should have search results" }
        assert(state.currentQuery == query) { "Should store current query" }
        assert(state.error == null) { "Should have no error" }
        
        // Verify search history was saved
        verify(appRepository).insertSearchHistory(query)
    }

    @Test
    fun `searchRecipes with empty query should clear results`() = runTest {
        // Given
        val emptyQuery = ""

        // When
        viewModel.searchRecipes(emptyQuery)

        // Then
        val state = viewModel.uiState.value
        assert(!state.isSearching) { "Should not be searching with empty query" }
        assert(state.searchResults.isEmpty()) { "Should clear search results" }
        assert(state.currentQuery.isEmpty()) { "Should clear current query" }
        
        // Verify no API call was made
        verify(recipeRepository, never()).getSearchRecipes(emptyQuery)
        verify(appRepository, never()).insertSearchHistory(emptyQuery)
    }

    @Test
    fun `searchRecipes should handle error`() = runTest {
        // Given
        val query = "test"
        val error = Exception("Network error")
        `when`(recipeRepository.getSearchRecipes(query))
            .thenReturn(Single.error(error))

        // When
        viewModel.searchRecipes(query)
        schedulerProvider.triggerActions()

        // Then
        val state = viewModel.uiState.value
        assert(state.isSearching) { "Should still be in searching mode" }
        assert(!state.isLoading) { "Should not be loading after error" }
        assert(state.searchResults.isEmpty()) { "Should have no results on error" }
        assert(state.error == "Network error") { "Should have error message" }
    }

    @Test
    fun `clearSearch should reset to initial state`() = runTest {
        // Given - search was performed
        val query = "test"
        `when`(recipeRepository.getSearchRecipes(query))
            .thenReturn(Single.just(emptyList()))
        `when`(appRepository.insertSearchHistory(query))
            .thenReturn(Completable.complete())
        
        viewModel.searchRecipes(query)
        schedulerProvider.triggerActions()

        // When
        viewModel.clearSearch()

        // Then
        val state = viewModel.uiState.value
        assert(!state.isSearching) { "Should not be searching" }
        assert(state.searchResults.isEmpty()) { "Should clear results" }
        assert(state.currentQuery.isEmpty()) { "Should clear query" }
    }

    @Test
    fun `deleteSearchHistory should remove item and reload`() = runTest {
        // Given
        val historyId = 1L
        val remainingHistory = listOf(
            SearchHistory(id = 2, query = "remaining")
        )
        
        `when`(appRepository.deleteSearchHistoryById(historyId))
            .thenReturn(Completable.complete())
        `when`(appRepository.loadSearchHistory())
            .thenReturn(Single.just(remainingHistory))

        // When
        viewModel.deleteSearchHistory(historyId)
        schedulerProvider.triggerActions()

        // Then
        verify(appRepository).deleteSearchHistoryById(historyId)
        val state = viewModel.uiState.value
        assert(state.searchHistory == remainingHistory) { "Should reload history after deletion" }
    }

    @Test
    fun `onRecipeClicked should trigger navigation event`() = runTest {
        // Given
        val recipeId = "123"

        // When
        viewModel.onRecipeClicked(recipeId)

        // Then
        val event = viewModel.navigationEvent.value
        assert(event is SearchNavigationEvent.NavigateToRecipeDetail) { 
            "Should emit NavigateToRecipeDetail event" 
        }
        assert((event as SearchNavigationEvent.NavigateToRecipeDetail).recipeId == recipeId) {
            "Should pass correct recipe id"
        }
    }

    @Test
    fun `onSearchHistoryClicked should search with that query`() = runTest {
        // Given
        val historyQuery = "pasta"
        val mockRecipes = listOf(
            RecipeResponse(
                id = "1",
                title = "Pasta Recipe",
                image = "pasta.jpg",
                readyInMinutes = 30,
                servings = 4,
                sourceUrl = "http://example.com"
            )
        )
        
        `when`(recipeRepository.getSearchRecipes(historyQuery))
            .thenReturn(Single.just(mockRecipes))
        `when`(appRepository.insertSearchHistory(historyQuery))
            .thenReturn(Completable.complete())

        // When
        viewModel.onSearchHistoryClicked(historyQuery)
        schedulerProvider.triggerActions()

        // Then
        val state = viewModel.uiState.value
        assert(state.isSearching) { "Should be searching" }
        assert(state.currentQuery == historyQuery) { "Should set query from history" }
        assert(state.searchResults == mockRecipes) { "Should have search results" }
    }
} 