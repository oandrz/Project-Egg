/**
 * Created by Andreas on 22/9/2018.
 */

/**
 * Created by Andreas on 15/8/2018.
 */

package starbright.com.projectegg.features.detail;

import java.util.List;

import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.data.local.model.Instruction;
import starbright.com.projectegg.features.base.BasePresenter;
import starbright.com.projectegg.features.base.BaseView;

class RecipeDetailContract {

    interface View extends BaseView<RecipeDetailPresenter> {
        void showProgressBar();
        void hideProgressBar();

        void hideScrollContainer();
        void showScrollContainer();
        void hideEmptyStateTextView();

        void renderErrorStateTextView(String errorMessage);
        void renderEmptyStateTextView();
        void renderBannerFoodImage(String imageURL);

        void renderHeaderContainer(int serving, int preparationMinutes, int cookingMinutes, String recipeName);
        void renderIngredientCard(List<Ingredient> ingredients);
        void renderInstructionCard(List<Instruction> instructions);

        void setupSwipeRefreshLayout();

        void setupAdView();

        void createShareIntent(String url, String recipeName);

        void navigateToWebViewActivity(String url);
    }

    interface Presenter extends BasePresenter {
        void setRecipeId(String recipeId);
        void getRecipeDetailInformation(String recipeId);

        void handleShareMenuClicked();

        void handleWebViewMenuClicked();
    }
}