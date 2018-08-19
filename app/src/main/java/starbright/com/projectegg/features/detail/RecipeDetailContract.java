/**
 * Created by Andreas on 15/8/2018.
 */

package starbright.com.projectegg.features.detail;

import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.features.base.BasePresenter;
import starbright.com.projectegg.features.base.BaseView;

class RecipeDetailContract {

    interface View extends BaseView<RecipeDetailPresenter> {
        void showProgressBar();
        void hideProgressBar();

        void updateView(Recipe recipe);
    }

    interface Presenter extends BasePresenter {
        void getRecipeDetailInformation(String recipeId);
    }
}