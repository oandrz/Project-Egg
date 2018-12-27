package starbright.com.projectegg.features.recipelist;

import java.util.List;

import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.features.base.BasePresenter;
import starbright.com.projectegg.features.base.BaseView;

class RecipeListContract {

    interface View extends BaseView<Presenter> {
        void setupRecyclerView();

        void setupSwipeRefreshLayout();

        void showLoadingBar();
        void hideLoadingBar();

        void bindRecipesToList(List<Recipe> recipes);
        void showDetail(String recipeId);

        void showErrorSnackBar(String errorMessage);
    }

    interface Presenter extends BasePresenter{
        void handleListItemClicked(int position);
        void handleRefresh();

        void setIngredients(List<Ingredient> ingredients);
    }
}
