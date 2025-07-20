package starbright.com.projectegg.compose.ui.screens.favorites

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

/**
 * UI State for Favorites Screen
 */
data class FavoritesUiState(
    val isLoading: Boolean = true,
    val favorites: List<FavouriteRecipe> = emptyList(),
    val error: String? = null
)

/**
 * Navigation events from Favorites Screen
 */
sealed class FavoritesNavigationEvent {
    data class NavigateToRecipeDetail(val recipeId: String) : FavoritesNavigationEvent()
}

/**
 * ViewModel for Favorites Screen
 */
class FavoritesViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val schedulerProvider: SchedulerProviderContract
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<FavoritesNavigationEvent?>(null)
    val navigationEvent: StateFlow<FavoritesNavigationEvent?> = _navigationEvent.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        compositeDisposable.add(
            appRepository.loadFavouriteRecipe()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { favorites ->
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                favorites = favorites,
                                error = null
                            )
                        }
                    },
                    { error ->
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                error = error.message ?: "Failed to load favorites"
                            )
                        }
                    }
                )
        )
    }

    fun removeFavorite(recipeId: String) {
        compositeDisposable.add(
            appRepository.deleteFavouriteRecipeById(recipeId.toIntOrNull() ?: 0)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    {
                        // Reload favorites after successful deletion
                        loadFavorites()
                    },
                    { error ->
                        _uiState.update { state ->
                            state.copy(
                                error = error.message ?: "Failed to remove favorite"
                            )
                        }
                    }
                )
        )
    }

    fun refresh() {
        loadFavorites()
    }

    fun onRecipeClicked(recipeId: String) {
        _navigationEvent.value = FavoritesNavigationEvent.NavigateToRecipeDetail(recipeId)
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
} 