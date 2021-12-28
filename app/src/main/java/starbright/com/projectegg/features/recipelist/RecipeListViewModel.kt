/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 9 - 8 - 2020.
 */

/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.RecipeConfig
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.SortOption
import starbright.com.projectegg.enum.RecipeSortCategory
import starbright.com.projectegg.util.NetworkHelper
import javax.inject.Inject

private const val FIRESTORE_NAME_KEY = "name"
private const val FIRESTORE_DOCUMENT_KEY = "sortOption"
private const val FIRESTORE_IMAGE_URL_KEY = "imageUrl"
class RecipeListViewModel @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val repository: AppRepository
) : ViewModel() {

    private var cuisines: MutableSet<String> = mutableSetOf()

    private lateinit var sortOption: List<SortOption>
    private lateinit var firstVersionConfig: RecipeConfig
    private lateinit var config: RecipeConfig

    private var _state: MutableLiveData<RecipeListState> = MutableLiveData()
    var state: LiveData<RecipeListState> = _state

    fun init(recipeConfig: RecipeConfig) {
        config = recipeConfig
        firstVersionConfig = config
        loadSortOption()
        _state.value = RecipeListState.ShowFooterLoading
        loadRecipe(0)
    }

    fun handleListItemClicked(selectedRecipeId: String) {
        _state.value = RecipeListState.NavigateRecipeDetail(selectedRecipeId)
    }

    fun handleLoadMore(lastPosition: Int) {
        _state.value = RecipeListState.ShowFooterLoading
        loadRecipe(lastPosition)
    }

    fun handleSortActionClicked() {
        _state.value = RecipeListState.ShowSortSelector(sortOption, config.sortCategory.type)
    }

    fun handleSortItemSelected(sortType: RecipeSortCategory) {
        config.sortCategory = sortType
        _state.value = RecipeListState.ResetList
    }

    fun handleFilterActionClicked() {
        _state.value = RecipeListState.ShowFilterSelector(cuisines.toList(), config.cuisine)
    }

    fun handleFilterItemSelected(cuisine: String) {
        config.cuisine = cuisine
        _state.value = RecipeListState.ResetList
    }

    private fun loadSortOption() {
        Firebase.firestore.collection(FIRESTORE_DOCUMENT_KEY)
            .get()
            .addOnSuccessListener { snapshot ->
                sortOption = snapshot
                    .map {
                        SortOption(
                            it.get(FIRESTORE_NAME_KEY).toString(),
                            it.get(FIRESTORE_IMAGE_URL_KEY).toString()
                        )
                    }
            }
            .addOnFailureListener { }
    }

    private fun loadRecipe(pos: Int) {
        if (!networkHelper.isConnectedWithNetwork()) {
            _state.value = RecipeListState.ShowErrorMessage(R.string.server_connection_error)
        }
        viewModelScope.launch {
            kotlin.runCatching { repository.getRecipes(config, pos) }
                .onSuccess {
                    it.forEach { it.cuisines?.mapTo(cuisines) { cuisine -> cuisine } }
                    _state.value = RecipeListState.HideFooterLoading
                    _state.value = if (it.isEmpty()) {
                        RecipeListState.SuccessEmptyRecipeState
                    } else {
                        RecipeListState.SuccessLoadRecipe(it)
                    }
                }
                .onFailure {
                    _state.value = RecipeListState.HideFooterLoading
                    _state.value = RecipeListState.RenderErrorState
                }
        }
    }

    sealed class RecipeListState {
        class NavigateRecipeDetail(val recipeId: String) : RecipeListState()
        class ShowErrorMessage(val resId: Int) : RecipeListState()
        class SuccessLoadRecipe(val recipeList: List<Recipe>) : RecipeListState()
        object SuccessEmptyRecipeState : RecipeListState()
        class ShowSortSelector(val sortOption: List<SortOption>, val selectedSort: String?) : RecipeListState()
        class ShowFilterSelector(val filterOption: List<String>, val selectedFilter: String?) : RecipeListState()
        object ResetList : RecipeListState()
        object RenderErrorState : RecipeListState()
        object ShowFooterLoading : RecipeListState()
        object HideFooterLoading : RecipeListState()
    }
}
