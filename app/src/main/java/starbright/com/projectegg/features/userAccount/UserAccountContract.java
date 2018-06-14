package starbright.com.projectegg.features.userAccount;

import starbright.com.projectegg.BasePresenter;
import starbright.com.projectegg.BaseView;

class UserAccountContract {

    interface View extends BaseView<Presenter> {
        void updateView(boolean isLogin);
        void navigatePage();
        void disableConfirmButton();
        void enableConfirmButton();
        void showLoginErrorDialog(String errorMessage);

        void showProgressBar();
        void hideProgressBar();
        void setupProgressBar();

        void showEmailEmptyErrorToast();
        void showEmailFormatWrongErrorToast();
        void showPasswordErrorToast();
    }

    interface Presenter extends BasePresenter {
        void onNavigationTextButtonClicked();
        void onAuthenticationButtonClicked(String email, String password);
        boolean isLoginAuthentication();
    }
}
