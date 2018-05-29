package starbright.com.projectegg.features.userAccount;

class UserAccountPresenter implements UserAccountContract.Presenter{

    private final UserAccountContract.View mView;
    private boolean mIsLogin;

    UserAccountPresenter(UserAccountContract.View view, boolean isLogin) {
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
