/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 1 - 8 - 2020.
 */

package starbright.com.projectegg.features.home.bookmark

import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenterContract
import starbright.com.projectegg.features.base.BaseViewContract

class FavouriteListContract {
    interface View : BaseViewContract {
        fun setupView()

        fun navigateDetailPage(id: Int)

        fun renderList(favouriteRecipes: List<Recipe>)
        fun renderEmptyView()
    }

    interface Presenter : BasePresenterContract {
        fun handleItemClick(recipeId: Int)
        fun getFavouriteList()
    }
}