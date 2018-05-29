package starbright.com.projectegg.features.login;

class LoginPresenter implements LoginContract.Presenter{

    private final LoginContract.View mView;
    private boolean mIsLogin;

    LoginPresenter(LoginContract.View view, boolean isLogin) {
        mView = view;
        mView.setPresenter(this);
        mIsLogin = isLogin;
    }

    @Override
    public void onNavigationTextButtonClicked() {
        mIsLogin = !mIsLogin;
        mView.updateView(mIsLogin);
    }

    @Override
    public void start() {
       mView.updateView(mIsLogin);
    }

    @Override
    public boolean isLogin() {
        return mIsLogin;
    }
}
