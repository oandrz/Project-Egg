/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.search

import starbright.com.projectegg.features.base.BasePresenterContract
import starbright.com.projectegg.features.base.BaseViewContract

class SearchRecipeContract {

    interface View : BaseViewContract {
        fun setupSearchByIngredientFab()
    }

    interface Presenter : BasePresenterContract {
    }
}