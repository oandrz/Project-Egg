/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 31 - 7 - 2020.
 */

package starbright.com.projectegg.features.detail

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.util.NetworkHelper
import javax.inject.Inject

class RecipeDetailViewModel @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val repository: AppRepository
) : ViewModel() {

    private val _state: MutableLiveData<RecipeDetailState> = MutableLiveData()
    val state: LiveData<RecipeDetailState> = _state

    private var isBookmarked: Boolean = false
    private var recipe: Recipe? = null

    fun getRecipeDetailInformation(recipeId: String) {
        isRecipeBookmarked(recipeId)
        if (!networkHelper.isConnectedWithNetwork()) {
            _state.value = RecipeDetailState.Error(R.string.server_connection_error)
        }
        _state.value = RecipeDetailState.ShowLoading

        viewModelScope.launch {
            kotlin.runCatching { repository.getRecipeDetailInformation(recipeId) }
                .onSuccess {
                    recipe = it
                    _state.value = recipe?.let { recipe ->
                        RecipeDetailState.Success(recipe)
                    } ?: RecipeDetailState.Empty
                }
                .onFailure {
                    _state.value = recipe?.let {
                        RecipeDetailState.Success(it)
                    } ?: RecipeDetailState.Empty
                }
        }
    }

    fun handleShareMenuClicked() {
        _state.value = recipe?.let {
            val sourceUrl = it.sourceStringUrl
            if (sourceUrl != null) {
                RecipeDetailState.NavigateShareIntent(sourceUrl, it.title)
            } else {
                RecipeDetailState.Error(R.string.detail_empty_label)
            }
        } ?: RecipeDetailState.Error(R.string.detail_empty_label)
    }

    fun handleWebViewMenuClicked() {
        _state.value = recipe?.sourceStringUrl?.let {
            RecipeDetailState.NavigateWebView(it)
        } ?: RecipeDetailState.Error(R.string.detail_empty_label)
    }

    fun handleBookmarkRecipeMenuClicked() {
        if (isBookmarked) {
            removeRecipeBookmark()
        } else {
            bookmarkRecipe()
        }
    }

    private fun bookmarkRecipe() {
        recipe?.let {
            viewModelScope.launch {
                repository.saveFavouriteRecipe(it)
                isBookmarked = true
                _state.value = RecipeDetailState.BookmarkUpdate(isBookMarked = isBookmarked)
            }
        }
    }

    private fun removeRecipeBookmark() {
        recipe?.let {
            viewModelScope.launch {
                repository.removeFavouriteRecipe(it.id)
                isBookmarked = false
                _state.value = RecipeDetailState.BookmarkUpdate(isBookMarked = isBookmarked)
            }
        }
    }

    private fun isRecipeBookmarked(recipeId: String) {
        viewModelScope.launch {
            val recipeFound = repository.isRecipeSavedBefore(recipeId.toInt())
            _state.value = RecipeDetailState.BookmarkUpdate(showSnackBar = false, recipeFound != null)
        }
    }
}

sealed class RecipeDetailState {
    object Empty: RecipeDetailState()
    class Success(val recipe: Recipe): RecipeDetailState()
    class Error(@StringRes val res: Int) : RecipeDetailState()
    class BookmarkUpdate(val showSnackBar: Boolean = true, val isBookMarked: Boolean) : RecipeDetailState()
    object ShowLoading : RecipeDetailState()
    class NavigateShareIntent(val url: String, val title: String) : RecipeDetailState()
    class NavigateWebView(val url: String) : RecipeDetailState()
}