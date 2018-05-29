package starbright.com.projectegg.features.login;

import starbright.com.projectegg.BasePresenter;
import starbright.com.projectegg.BaseView;

class LoginContract {

    interface View extends BaseView<Presenter> {
        void updateView(boolean isLogin);
    }

    interface Presenter extends BasePresenter {
        void onNavigationTextButtonClicked();
        boolean isLogin();
    }
}
