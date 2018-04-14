package starbright.com.projectegg.features.recipelist;

import starbright.com.projectegg.BasePresenter;
import starbright.com.projectegg.BaseView;

/**
 * Created by Andreas on 4/8/2018.
 */

public class RecipeListContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter{
        void getRecipesBasedIngredients(String ingredients);
    }
}
