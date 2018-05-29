package starbright.com.projectegg.features.userAccount;

import android.os.Bundle;

import starbright.com.projectegg.R;
import starbright.com.projectegg.features.base.BaseActivityWithoutToolbar;

public class UserAccountActivity extends BaseActivityWithoutToolbar {

    private static final String LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";

    private UserAccountFragment mFragment;

    @Override
    protected void onStart() {
        super.onStart();
        getSupportFragmentManager().beginTransaction()
                .attach(mFragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment();
    }

    @Override
    protected void onStop() {
        getSupportFragmentManager().beginTransaction()
                .detach(mFragment)
                .commitAllowingStateLoss();
        super.onStop();
    }

    private void initFragment() {
        mFragment = (UserAccountFragment) getSupportFragmentManager()
                .findFragmentByTag(LOGIN_FRAGMENT_TAG);
        if (mFragment == null) {
            mFragment = UserAccountFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, mFragment, LOGIN_FRAGMENT_TAG)
                    .commit();
        }
    }
}
