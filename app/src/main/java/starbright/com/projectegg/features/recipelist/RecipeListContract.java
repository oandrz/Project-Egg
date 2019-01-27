package starbright.com.projectegg.features.recipelist;

import java.util.List;

import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.features.base.BasePresenter;
import starbright.com.projectegg.features.base.BaseView;

/**
 * Created by Andreas on 4/8/2018.
 */

class RecipeListContract {

    interface View extends BaseView<Presenter> {
        void setupRecyclerView();
        void bindRecipesToList(List<Recipe> recipes);
        void showLoadingBar();
        void hideLoadingBar();
    }

    interface Presenter extends BasePresenter{
        void getRecipesBasedIngredients(String ingredients);
    }
}
