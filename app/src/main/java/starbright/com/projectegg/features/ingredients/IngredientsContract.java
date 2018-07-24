package starbright.com.projectegg.features.ingredients;

import java.util.ArrayList;
import java.util.List;

import starbright.com.projectegg.BasePresenter;
import starbright.com.projectegg.BaseView;
import starbright.com.projectegg.data.local.model.Ingredient;

class IngredientsContract {

    interface View extends BaseView<IngredientsPresenter> {
        void setupEtSearchIngredients();
        void setupRvIngredientSuggestion();
        void setupMaterialProgressDialog();
        void setupBottomSheetDialogFragment();

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
        void showItemMaxToast();
        void showDuplicateItemToast();

        void showPredictionEmptyToast();

        void showMaterialProgressDialog();
        void hideMaterialProgressDialog();

        void showBottomSheetDialog();

        void setCartItem(List<Ingredient> cart);

        void updateIngredientCount(int count);

        void hideSoftkeyboard();
    }

    interface Presenter extends BasePresenter {
        void handleActionButtonClicked(String query);
        void handleCartTvClicked();

        void handleSuggestionTextChanged(String query);
        void handleSuggestionItemClicked(Ingredient ingredient);
        void handleCameraResult(String filePath);

        void searchIngredient(String query);

        void setCart(List<Ingredient> cart);
        ArrayList<Ingredient> getCart();
    }
}