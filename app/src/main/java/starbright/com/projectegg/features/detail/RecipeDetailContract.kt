/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 31 - 7 - 2020.
 */

/**
 * Created by Andreas on 5/10/2019.
 */

/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.detail

import androidx.annotation.StringRes
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Instruction
import starbright.com.projectegg.features.base.BasePresenterContract
import starbright.com.projectegg.features.base.BaseViewContract

class RecipeDetailContract {

    interface View : BaseViewContract {
        fun showProgressBar()
        fun hideProgressBar()

        fun hideScrollContainer()
        fun showScrollContainer()
        fun hideEmptyView()

        fun renderErrorView(errorMessage: String)
        fun renderEmptyView()
        fun renderBannerFoodImage(imageURL: String)

        fun renderHeaderContainer(
            serving: Int,
            cookingMinutes: Int,
            recipeName: String,
            dishType: String,
            calories: Int
        )

        fun renderIngredientsList(ingredients: MutableList<Ingredient>)
        fun renderInstructionsList(instructions: MutableList<Instruction>)

        fun setupSwipeRefreshLayout()

        fun createShareIntent(url: String, recipeName: String)

        fun navigateToWebViewActivity(url: String)

        fun showSnackbar(@StringRes text: Int)

        fun updateMenu(isBookmarked: Boolean)
    }

    interface Presenter : BasePresenterContract {
        fun getRecipeDetailInformation(recipeId: String)
        fun handleShareMenuClicked()
        fun handleWebViewMenuClicked()
        fun handleBookmarkRecipeMenuClicked()
    }
}