/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

package starbright.com.projectegg.features.search

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.RecipeConfig
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.SearchHistory
import starbright.com.projectegg.util.NetworkHelper
import timber.log.Timber
import javax.inject.Inject

private const val MIN_QUERY_LENGTH = 2

class SearchRecipeViewModel @Inject constructor(
    private val repository: AppRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _searchRecipeState: MutableLiveData<SearchState> = MutableLiveData()
    val searchRecipeState: LiveData<SearchState> = _searchRecipeState

    private var lastQuery: String = ""
    private val subject: MutableSharedFlow<String> = MutableSharedFlow()

    fun handleUserTypeInEditText(query: String?) {
        lastQuery = query.orEmpty()
        if (query == null || query.isEmpty()) {
            loadSearchHistory()
        } else {
            viewModelScope.launch {
                subject.emit(query)
            }
        }
    }

    fun handleUserSearch(query: String?) {
        query?.let {
            searchQueryExistence(query)
        }
    }

    fun handleSuggestionItemClicked(recipeId: String) {
        _searchRecipeState.value = SearchState.NavigateRecipeDetail(recipeId)
    }

    fun handleRecentSearchItemClicked(query: String) {
        _searchRecipeState.value = SearchState.NavigateRecipeList(query)
    }

    fun handleRemoveSearchHistory(query: String, position: Int) {
        viewModelScope.launch {
            kotlin.runCatching { repository.removeSearchHistory(query) }
                .onSuccess {
                    _searchRecipeState.value = SearchState.UpdateList(position)
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun observeSuggestedRecipe() {
        if (!networkHelper.isConnectedWithNetwork()) {
            _searchRecipeState.value = SearchState.RenderError(R.string.server_connection_error)
        }

        viewModelScope.launch {
            try {
                subject
                    .filter { it.isNotEmpty() && it.length >= MIN_QUERY_LENGTH }
                    .debounce(200L)
                    .distinctUntilChanged()
                    .mapLatest {
                        repository.getRecipes(
                            RecipeConfig(it, "", responseLimit = 5, ingredients = null), 0
                        )
                    }
                    .collect {
                        if (it.isNotEmpty() && lastQuery.isNotEmpty() && lastQuery.length >= MIN_QUERY_LENGTH) {
                            _searchRecipeState.postValue(SearchState.RenderRecipe(it))
                        }
                    }
            } catch (e: Throwable) {
                Timber.e(e)
                _searchRecipeState.value = SearchState.RenderError(R.string.error_title_system)
            }
        }
    }

    fun loadSearchHistory() {
        viewModelScope.launch {
            try {
                repository.getSearchHistory().collect {
                    _searchRecipeState.value = SearchState.RenderSearchHistory(it.map { it.query })
                }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    private suspend fun addSearchQuery(query: String) {
        kotlin.runCatching {
            repository.addSearchHistory(
                SearchHistory(
                    query = query,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
            .onSuccess {
                _searchRecipeState.value = SearchState.NavigateRecipeList(query)
            }
            .onFailure {
                Timber.e(it)
            }
    }

    private suspend fun updateSearchQueryTimestamp(query: String) {
        kotlin.runCatching {
            repository.updateExistingHistoryTimestamp(query, System.currentTimeMillis())
        }
            .onSuccess {
                _searchRecipeState.value = SearchState.NavigateRecipeList(query)
            }
            .onFailure { Timber.e(it) }
    }

    private fun searchQueryExistence(query: String) {
        viewModelScope.launch {
            try {
                val result = repository.checkQueryExistence(query).first()
                if (result.isEmpty()) {
                    addSearchQuery(query)
                } else {
                    updateSearchQueryTimestamp(query)
                }
            } catch (t : Throwable) {
                Timber.e(t)
            }
        }
    }

    sealed class SearchState {
        class RenderSearchHistory(val history: List<String>) : SearchState()
        class RenderRecipe(val recipes: List<Recipe>) : SearchState()
        class NavigateRecipeList(val query: String) : SearchState()
        class NavigateRecipeDetail(val recipeId: String) : SearchState()
        class RenderError(@StringRes val error: Int) : SearchState()
        class UpdateList(val position: Int) : SearchState()
        object ShowLoading : SearchState()
        object CloseLoading : SearchState()
    }
}