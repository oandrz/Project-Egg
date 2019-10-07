/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.features.userAccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import starbright.com.projectegg.R;
import starbright.com.projectegg.features.base.BaseActivityWithoutToolbar;
import starbright.com.projectegg.features.ingredients.IngredientsActivity;

public class UserAccountActivity extends BaseActivityWithoutToolbar
        implements UserAccountFragment.FragmentListener {

    private static final String LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";

    private UserAccountFragment mFragment;

    public static Intent newIntent(Context context) {
        return new Intent(context, UserAccountActivity.class);
    }

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
    protected void onDestroy() {
        getSupportFragmentManager().beginTransaction()
                .detach(mFragment)
                .commitAllowingStateLoss();
        super.onDestroy();
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

    @Override
    public void navigateToIngredientsActivity() {
        startActivity(IngredientsActivity.Companion.newIntent(this));
        finish();
    }
}
