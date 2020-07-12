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