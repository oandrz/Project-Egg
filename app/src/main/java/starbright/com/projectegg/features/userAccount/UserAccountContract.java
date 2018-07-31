package starbright.com.projectegg.features.userAccount;

import starbright.com.projectegg.features.base.BasePresenter;
import starbright.com.projectegg.features.base.BaseView;

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
        void handleVerificationEmailDialogClicked();
        void handleAuthenticationButtonClicked(String email, String password);

        void updateUserAuthRole();
        void onDestroy();

        boolean isLoginAuthentication();
    }
}
