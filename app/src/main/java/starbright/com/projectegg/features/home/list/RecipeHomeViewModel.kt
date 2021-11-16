/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 8 - 8 - 2020.
 */

package starbright.com.projectegg.features.home.list

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.home.list.RecipeHomeViewModel.RecipeHomeFragmentState.*
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

class RecipeHomeViewModel @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val repository: AppRepository
): ViewModel() {

    private val viewState: MutableLiveData<RecipeHomeFragmentState> = MutableLiveData()
    val viewStateLive: LiveData<RecipeHomeFragmentState> = viewState

    fun refresh() {
        loadRecipe(0)
    }

    private fun loadRecipe(page: Int, footerLoad: Boolean = false) {
        viewModelScope.launch {
            if (!networkHelper.isConnectedWithNetwork()) {
                viewState.value = ErrorState(R.string.server_connection_error)
            }
            if (footerLoad) {
                viewState.postValue(RenderFooterLoad)
            }

            kotlin.runCatching { repository.getRecommendedRecipe(page) }
                .onSuccess {
                    viewState.value = RenderList(it)
                }
                .onFailure {
                    Log.e(RecipeHomeViewModel::class.java.simpleName, it.message, it)
                    viewState.value = ErrorState(R.string.network_connection_error)
                }
        }
    }

    fun handleLoadMore(currentPage: Int) {
        loadRecipe(currentPage, true)
    }

    fun handleItemClick(selectedRecipeId: String) {
        viewState.value = NavigateDetailPage(selectedRecipeId)
    }

    sealed class RecipeHomeFragmentState {
        class ErrorState(@StringRes val errorRes: Int) : RecipeHomeFragmentState()
        class RenderList(val recipe: List<Recipe>) : RecipeHomeFragmentState()
        object RenderFooterLoad : RecipeHomeFragmentState()
        class NavigateDetailPage(val selectedRecipeId: String) : RecipeHomeFragmentState()
    }
}