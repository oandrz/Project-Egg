package starbright.com.projectegg.compose.ui.screens.detail

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.RecipeRepository
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

/**
 * UI State for Recipe Detail Screen
 */
data class RecipeDetailUiState(
    val isLoading: Boolean = true,
    val recipe: Recipe? = null,
    val isFavorite: Boolean = false,
    val selectedIngredients: Set<Int> = emptySet(),
    val error: String? = null
)

/**
 * Navigation events from Recipe Detail Screen
 */
sealed class RecipeDetailNavigationEvent {
    data class NavigateToCart(val ingredients: List<Ingredient>) : RecipeDetailNavigationEvent()
    data class OpenSourceUrl(val url: String) : RecipeDetailNavigationEvent()
}

/**
 * ViewModel for Recipe Detail Screen
 */
class RecipeDetailViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val appRepository: AppRepository,
    private val schedulerProvider: SchedulerProviderContract
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<RecipeDetailNavigationEvent?>(null)
    val navigationEvent: StateFlow<RecipeDetailNavigationEvent?> = _navigationEvent.asStateFlow()

    fun loadRecipeDetail(recipeId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        compositeDisposable.add(
            recipeRepository.getRecipeDetailInformation(recipeId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { recipe ->
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                recipe = recipe,
                                error = null
                            )
                        }
                        // Check if recipe is favorite
                        checkIfFavorite(recipeId)
                    },
                    { error ->
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                error = error.message ?: "Failed to load recipe details"
                            )
                        }
                    }
                )
        )
    }

    private fun checkIfFavorite(recipeId: String) {
        compositeDisposable.add(
            appRepository.checkIfRecipeIsFavourite(recipeId.toIntOrNull() ?: 0)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { isFavorite ->
                        _uiState.update { state ->
                            state.copy(isFavorite = isFavorite)
                        }
                    },
                    { /* Ignore errors for favorite check */ }
                )
        )
    }

    fun toggleFavorite() {
        val recipe = _uiState.value.recipe ?: return
        val isFavorite = _uiState.value.isFavorite
        
        if (isFavorite) {
            // Remove from favorites
            compositeDisposable.add(
                appRepository.deleteFavouriteRecipeById(recipe.id)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(
                        {
                            _uiState.update { state ->
                                state.copy(isFavorite = false)
                            }
                        },
                        { error ->
                            _uiState.update { state ->
                                state.copy(
                                    error = error.message ?: "Failed to remove from favorites"
                                )
                            }
                        }
                    )
            )
        } else {
            // Add to favorites
            val favoriteRecipe = FavouriteRecipe(
                recipeId = recipe.id,
                recipeTitle = recipe.title,
                recipeImageUrl = recipe.image ?: "",
                cookingTimeInMinutes = recipe.cookingMinutes ?: 0,
                servingCount = recipe.servingCount ?: 0,
                source = recipe.sourceName ?: ""
            )
            
            compositeDisposable.add(
                appRepository.insertFavouriteRecipe(favoriteRecipe)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(
                        {
                            _uiState.update { state ->
                                state.copy(isFavorite = true)
                            }
                        },
                        { error ->
                            _uiState.update { state ->
                                state.copy(
                                    error = error.message ?: "Failed to add to favorites"
                                )
                            }
                        }
                    )
            )
        }
    }

    fun onIngredientChecked(ingredientId: Int, isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(
                selectedIngredients = if (isChecked) {
                    state.selectedIngredients + ingredientId
                } else {
                    state.selectedIngredients - ingredientId
                }
            )
        }
    }

    fun onAddToCartClick() {
        val recipe = _uiState.value.recipe ?: return
        val selectedIds = _uiState.value.selectedIngredients
        val selectedIngredients = recipe.ingredients?.filter { ingredient ->
            selectedIds.contains(ingredient.id?.toIntOrNull() ?: 0) 
        } ?: emptyList()
        
        if (selectedIngredients.isNotEmpty()) {
            _navigationEvent.value = RecipeDetailNavigationEvent.NavigateToCart(selectedIngredients)
        }
    }

    fun onSourceClick() {
        val sourceUrl = _uiState.value.recipe?.sourceStringUrl
        if (!sourceUrl.isNullOrBlank()) {
            _navigationEvent.value = RecipeDetailNavigationEvent.OpenSourceUrl(sourceUrl)
        }
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
} 