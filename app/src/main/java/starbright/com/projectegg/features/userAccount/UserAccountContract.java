package starbright.com.projectegg.features.userAccount;

import starbright.com.projectegg.BasePresenter;
import starbright.com.projectegg.BaseView;

class UserAccountContract {

    interface View extends BaseView<Presenter> {
        void updateView(boolean isLogin);
        void showProgressDialog();
        void navigatePage();
        void disableConfirmButton();
        void enableConfirmButton();
        void showLoginErrorDialog(String errorMessage);
    }

    interface Presenter extends BasePresenter {
        void onNavigationTextButtonClicked();
        void onConfirmButtonClicked(String email, String password);
        boolean isLoginAuthentication();
    }
}
