package starbright.com.projectegg.features.ingredients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import starbright.com.projectegg.R;
import starbright.com.projectegg.features.base.BaseActivityWithoutToolbar;

public class IngredientsActivity extends BaseActivityWithoutToolbar {
    private static final String INGREDIENTS_FRAGMENT_TAG = "INGREDIENTS_FRAGMENT_TAG";

    private IngredientsFragment mFragment;

    public static Intent newIntent(Context context) {
        return new Intent(context, IngredientsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportFragmentManager().beginTransaction()
                .attach(mFragment)
                .commit();
    }

    // TODO: 24/07/18 Research if this needed
//    @Override
//    protected void onStop() {
//        getSupportFragmentManager().beginTransaction()
//                .detach(mFragment)
//                .commitAllowingStateLoss();
//        super.onStop();
//    }

    private void initFragment() {
        mFragment = (IngredientsFragment) getSupportFragmentManager()
                .findFragmentByTag(INGREDIENTS_FRAGMENT_TAG);
        if (mFragment == null) {
            mFragment = IngredientsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, mFragment, INGREDIENTS_FRAGMENT_TAG)
                    .commit();
        }
    }
}
