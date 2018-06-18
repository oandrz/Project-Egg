package starbright.com.projectegg.features.ingredients;

import starbright.com.projectegg.BasePresenter;
import starbright.com.projectegg.BaseView;

class IngredientsContract {

    interface View extends BaseView<IngredientsPresenter> {
        void setupEtSearchIngredients();
        void clearEtSearchQuery();
        void openCamera();
    }

    interface Presenter extends BasePresenter {
        void onActionButtonClicked(String query);
    }
}
