package starbright.com.projectegg.compose.ui.screens.search

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.RecipeRepository
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.response.RecipeResponse
import starbright.com.projectegg.data.model.response.RecipeListResponse
import starbright.com.projectegg.data.model.local.SearchHistory
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

/**
 * UI State for Search Screen
 */
data class SearchUiState(
    val isSearching: Boolean = false,
    val currentQuery: String = "",
    val searchResults: List<Recipe> = emptyList(),
    val searchHistory: List<SearchHistory> = emptyList(),
    val error: String? = null
)

/**
 * Navigation events from Search Screen
 */
sealed class SearchNavigationEvent {
    data class NavigateToRecipeDetail(val recipeId: String) : SearchNavigationEvent()
}

/**
 * ViewModel for Search Screen
 */
class SearchViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val appRepository: AppRepository,
    private val schedulerProvider: SchedulerProviderContract
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<SearchNavigationEvent?>(null)
    val navigationEvent: StateFlow<SearchNavigationEvent?> = _navigationEvent.asStateFlow()

    init {
        loadSearchHistory()
    }

    fun loadSearchHistory() {
        compositeDisposable.add(
            appRepository.loadSearchHistory()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { history ->
                        _uiState.update { state ->
                            state.copy(
                                searchHistory = history,
                                error = null
                            )
                        }
                    },
                    { error ->
                        _uiState.update { state ->
                            state.copy(
                                error = error.message ?: "Failed to load search history"
                            )
                        }
                    }
                )
        )
    }

    fun searchRecipes(query: String) {
        // Clear search if query is empty
        if (query.isBlank()) {
            clearSearch()
            return
        }

        _uiState.update { it.copy(
            isSearching = true,
            currentQuery = query,
            error = null
        )}

        compositeDisposable.add(
            recipeRepository.getSearchRecipes(query, 10, 0)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { response ->
                        val recipes = response.results.map { recipeResponse ->
                            Recipe(
                                id = recipeResponse.id,
                                title = recipeResponse.title,
                                image = recipeResponse.image,
                                cookingMinutes = recipeResponse.cookingTime,
                                servingCount = recipeResponse.servings,
                                cuisines = recipeResponse.cuisines,
                                dishTypes = recipeResponse.dishTypes,
                                sourceStringUrl = recipeResponse.sourceStringUrl,
                                sourceName = recipeResponse.sourceName,
                                totalRecipe = response.totalResults
                            )
                        }
                        _uiState.update { state ->
                            state.copy(
                                searchResults = recipes,
                                error = null
                            )
                        }
                        // Save to search history
                        saveSearchHistory(query)
                    },
                    { error ->
                        _uiState.update { state ->
                            state.copy(
                                error = error.message ?: "Failed to search recipes"
                            )
                        }
                    }
                )
        )
    }

    private fun saveSearchHistory(query: String) {
        val searchHistory = SearchHistory(
            query = query,
            createdAt = System.currentTimeMillis()
        )
        
        compositeDisposable.add(
            appRepository.insertSearchHistory(searchHistory)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { /* Successfully saved */ },
                    { /* Ignore errors for history saving */ }
                )
        )
    }

    fun clearSearch() {
        _uiState.update { state ->
            state.copy(
                isSearching = false,
                currentQuery = "",
                searchResults = emptyList(),
                error = null
            )
        }
        // Reload search history
        loadSearchHistory()
    }

    fun deleteSearchHistory(historyId: Int) {
        compositeDisposable.add(
            appRepository.deleteSearchHistoryById(historyId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    {
                        // Reload search history after deletion
                        loadSearchHistory()
                    },
                    { error ->
                        _uiState.update { state ->
                            state.copy(
                                error = error.message ?: "Failed to delete search history"
                            )
                        }
                    }
                )
        )
    }

    fun onSearchHistoryClicked(query: String) {
        searchRecipes(query)
    }

    fun onRecipeClicked(recipeId: String) {
        _navigationEvent.value = SearchNavigationEvent.NavigateToRecipeDetail(recipeId)
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
} 