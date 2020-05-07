package starbright.com.projectegg.features.home.list

import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenterContract
import starbright.com.projectegg.features.base.BaseViewContract

class RecipeHomeContract {

    interface View: BaseViewContract {
        fun setupSearchView()
        fun setupList()
        fun populateList(recipe: MutableList<Recipe>)
    }

    interface Presenter: BasePresenterContract {

    }
}