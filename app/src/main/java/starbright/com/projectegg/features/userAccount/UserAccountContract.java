package starbright.com.projectegg.features.userAccount;

import starbright.com.projectegg.BasePresenter;
import starbright.com.projectegg.BaseView;

class UserAccountContract {

    interface View extends BaseView<Presenter> {
        void updateView(boolean isLogin);

        void navigateToSearchRecipePage();

        void disableView();

        void enableView();

        void setupProgressBar();
        void showProgressBar();
        void hideProgressBar();

        void showEmailEmptyErrorToast();
        void showEmailFormatWrongErrorToast();
        void showPasswordErrorToast();

        void showLoginErrorToast(String errorMessage);

        void showVerificationEmailSentDialog();
    }

    interface Presenter extends BasePresenter {
        void handleNavigationTextButtonClicked();

        void handleAuthenticationButtonClicked(String email, String password);

        void onDestroy();
        boolean isLoginAuthentication();
    }
}
