package starbright.com.projectegg.features.ingredients;

import java.util.List;

import starbright.com.projectegg.BasePresenter;
import starbright.com.projectegg.BaseView;
import starbright.com.projectegg.data.local.model.Ingredient;

class IngredientsContract {

    interface View extends BaseView<IngredientsPresenter> {
        void setupEtSearchIngredients();
        void setupRvIngredientSuggestion();
        void setupMaterialProgressDialog();

        void clearEtSearchQuery();
        void openCamera();

        void showSearchSuggestion(List<Ingredient> ingredients);
        void hideSearchSuggestion();

        void showActionCamera();
        void showActionClear();
        void hideActionButton();

        void showSuggestionProgressBar();
        void hideSuggestionProgressBar();

        void showItemEmptyToast();

        void showMaterialProgressDialog();
        void hideMaterialProgressDialog();
    }

    interface Presenter extends BasePresenter {
        void handleActionButtonClicked(String query);
        void handleOnSuggestionTextChanged(String query);
        void searchIngredient(String query);
        void handleCameraResult(String filePath);
    }
}