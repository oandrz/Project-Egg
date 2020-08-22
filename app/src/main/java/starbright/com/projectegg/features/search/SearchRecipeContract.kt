/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

package starbright.com.projectegg.features.search

import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenterContract
import starbright.com.projectegg.features.base.BaseViewContract

class SearchRecipeContract {

    interface View : BaseViewContract {
        fun setupView()

        fun showLoading()
        fun hideLoading()

        fun renderRecipeSuggestion(recipes: List<Recipe>)
        fun renderSearchHistory(searchQueries: List<String>)

        fun navigateRecipeList(query: String)
        fun navigateRecipeDetail(recipeId: String)

        fun removeSelectedSearchQuery(position: Int)
    }

    interface Presenter : BasePresenterContract {
        fun handleUserTypeInEditText(query: String?)
        fun handleUserSearch(query: String?)
        fun handleSuggestionItemClicked(recipeId: String)
        fun handleRecentSearchItemClicked(query: String)
        fun handleRemoveSearchHistory(query: String, position: Int)
        fun loadSearchHistory()
    }
}