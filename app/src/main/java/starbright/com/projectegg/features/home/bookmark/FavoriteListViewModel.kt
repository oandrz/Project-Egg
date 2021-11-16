package starbright.com.projectegg.features.home.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.home.list.RecipeHomeViewModel
import javax.inject.Inject

class FavoriteListViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _favoriteListState: MutableLiveData<FavoriteListState> = MutableLiveData()
    val favoriteListState: LiveData<FavoriteListState> = _favoriteListState

    fun handleItemClick(recipeId: Int) {
        _favoriteListState.value = FavoriteListState.NavigateDetail(recipeId)
    }

    fun getFavouriteList() {
        viewModelScope.launch {
            kotlin.runCatching { repository.getFavouriteRecipe() }
                .onSuccess {
                    if (it.isEmpty()) {
                        _favoriteListState.value = FavoriteListState.RenderEmptyView
                    } else {
                        _favoriteListState.value = FavoriteListState.RenderList(
                            it.map { recipe ->
                                Recipe(
                                    recipe.recipeId,
                                    recipe.cookingTimeInMinutes,
                                    recipe.servingCount,
                                    recipe.recipeTitle,
                                    recipe.recipeImageUrl,
                                    sourceName = recipe.source
                                )
                            }
                        )
                    }
                }
                .onFailure {
                    Log.e(RecipeHomeViewModel::class.java.simpleName, it.message, it)
                    _favoriteListState.value = FavoriteListState.RenderEmptyView
                }
        }
    }

    sealed class FavoriteListState {
        object RenderEmptyView : FavoriteListState()
        object RenderError : FavoriteListState()
        class NavigateDetail(val recipeId: Int) : FavoriteListState()
        class RenderList(val recipes: List<Recipe>) : FavoriteListState()
    }
}