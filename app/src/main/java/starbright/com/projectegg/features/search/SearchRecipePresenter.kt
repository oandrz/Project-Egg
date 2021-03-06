/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

package starbright.com.projectegg.features.search

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.RecipeConfig
import starbright.com.projectegg.data.model.local.SearchHistory
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val MIN_QUERY_LENGTH = 2
class SearchRecipePresenter @Inject constructor(
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    schedulerProvider: SchedulerProviderContract,
    private val repository: AppRepository
) : BasePresenter<SearchRecipeContract.View>(
    schedulerProvider, compositeDisposable, networkHelper
), SearchRecipeContract.Presenter {

    private var lastQuery: String = ""
    private val subject: PublishSubject<String> = PublishSubject.create()

    override fun onCreateScreen() {
        view.setupView()
        fetchSuggestedRecipe()
    }

    override fun handleUserTypeInEditText(query: String?) {
        lastQuery = query.orEmpty()
        if (query == null || query.isEmpty()) {
            loadSearchHistory()
        } else {
            subject.onNext(query)
        }
    }

    override fun handleUserSearch(query: String?) {
        query?.let {
            searchQueryExistence(query)
        }
    }

    override fun handleSuggestionItemClicked(recipeId: String) {
        view.navigateRecipeDetail(recipeId)
    }

    override fun handleRecentSearchItemClicked(query: String) {
        view.navigateRecipeList(query)
    }

    override fun handleRemoveSearchHistory(query: String, position: Int) {
        compositeDisposable.add(
            repository.removeSearchHistory(query)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe {
                    view.removeSelectedSearchQuery(position)
                }
        )
    }

    private fun fetchSuggestedRecipe() {
        if (!isConnectedToInternet()) view.showError(R.string.server_connection_error)

        compositeDisposable.add(
            subject
                .filter { it.isNotEmpty() && it.length >= MIN_QUERY_LENGTH }
                .debounce(200, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap {
                    repository.getRecipes(
                        RecipeConfig(it, "", responseLimit = 5, ingredients = null), 0
                    ).subscribeOn(schedulerProvider.io())
                }
                .observeOn(schedulerProvider.ui())
                .subscribe({ recipes ->
                    if (recipes.isNotEmpty() && lastQuery.isNotEmpty() && lastQuery.length >= MIN_QUERY_LENGTH) {
                        view.renderRecipeSuggestion(recipes)
                    }
                }, {
                    view.showError(R.string.error_title_system)
                })
        )
    }

    override fun loadSearchHistory() {
        compositeDisposable.add(
            repository.getSearchHistory()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe { histories ->
                    view.renderSearchHistory(histories.map { it.query })
                }
        )
    }

    private fun addSearchQuery(query: String) {
        compositeDisposable.add(
            repository.addSearchHistory(
                SearchHistory(
                    query = query,
                    createdAt = System.currentTimeMillis()
                )
            )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe {
                    view.navigateRecipeList(query)
                }
        )
    }

    private fun updateSearchQueryTimestamp(query: String) {
        compositeDisposable.add(
            repository.updateExistingHistoryTimestamp(query, System.currentTimeMillis())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe {
                    view.navigateRecipeList(query)
                }
        )
    }

    private fun searchQueryExistence(query: String) {
        compositeDisposable.add(
            repository.checkQueryExistence(query)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe { histories ->
                    if (histories == null || histories.isEmpty()) {
                        addSearchQuery(query)
                    } else {
                        updateSearchQueryTimestamp(query)
                    }
                }
        )
    }
}