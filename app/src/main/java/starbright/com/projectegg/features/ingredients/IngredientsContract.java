package starbright.com.projectegg.features.ingredients;

import java.util.List;

import starbright.com.projectegg.BasePresenter;
import starbright.com.projectegg.BaseView;
import starbright.com.projectegg.data.local.model.Ingredient;

class IngredientsContract {

    interface View extends BaseView<IngredientsPresenter> {
        void setupEtSearchIngredients();
        void setupRvIngredientSuggestion();
        void clearEtSearchQuery();
        void openCamera();
        void showSearchSuggestion(List<Ingredient> ingredients);
        void hideSearchSuggestion();
    }

    interface Presenter extends BasePresenter {
        void onActionButtonClicked(String query);
        void searchIngredient(String query);
    }
}