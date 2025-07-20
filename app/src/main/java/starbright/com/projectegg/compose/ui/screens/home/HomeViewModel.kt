package starbright.com.projectegg.compose.ui.screens.home

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import starbright.com.projectegg.data.RecipeRepository
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

/**
 * UI State for Home Screen
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val recipes: List<Recipe> = emptyList(),
    val error: String? = null,
    val currentPage: Int = 0,
    val hasMore: Boolean = true
)

/**
 * Navigation events from Home Screen
 */
sealed class HomeNavigationEvent {
    object NavigateToSearch : HomeNavigationEvent()
    data class NavigateToRecipeDetail(val recipeId: String) : HomeNavigationEvent()
}

/**
 * ViewModel for Home Screen
 */
class HomeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val schedulerProvider: SchedulerProviderContract
) : ViewModel() {

    companion object {
        private const val RECIPES_PER_PAGE = 10
    }

    private val compositeDisposable = CompositeDisposable()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<HomeNavigationEvent?>(null)
    val navigationEvent: StateFlow<HomeNavigationEvent?> = _navigationEvent.asStateFlow()

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        compositeDisposable.add(
            recipeRepository.getRandomRecipe(0)
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
                                isLoading = false,
                                recipes = recipes,
                                error = null,
                                currentPage = 0
                            )
                        }
                    },
                    { error ->
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                error = error.message ?: "Unknown error occurred"
                            )
                        }
                    }
                )
        )
    }

    fun loadMoreRecipes() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMore) return
        
        _uiState.update { it.copy(isLoadingMore = true) }
        val nextPage = _uiState.value.currentPage + 1
        
        compositeDisposable.add(
            recipeRepository.getRandomRecipe(nextPage * RECIPES_PER_PAGE)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { response ->
                        val newRecipes = response.results.map { recipeResponse ->
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
                                isLoadingMore = false,
                                recipes = state.recipes + newRecipes,
                                currentPage = nextPage,
                                hasMore = newRecipes.isNotEmpty()
                            )
                        }
                    },
                    { error ->
                        _uiState.update { state ->
                            state.copy(
                                isLoadingMore = false,
                                error = error.message ?: "Failed to load more recipes"
                            )
                        }
                    }
                )
        )
    }

    fun refresh() {
        loadRecipes()
    }

    fun onSearchClicked() {
        _navigationEvent.value = HomeNavigationEvent.NavigateToSearch
    }

    fun onRecipeClicked(recipeId: String) {
        _navigationEvent.value = HomeNavigationEvent.NavigateToRecipeDetail(recipeId)
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
} 