/**
 * Created by Andreas on 5/8/2018.
 */

package starbright.com.projectegg.features.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import starbright.com.projectegg.R;
import starbright.com.projectegg.features.base.BaseActivityWithToolbar;

public class RecipeDetailActivity extends BaseActivityWithToolbar
        implements RecipeDetailFragment.FragmentListener {

    private static final String RECIPE_FRAGMENT_TAG = "RECIPE_FRAGMENT_TAG";
    private static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";

    private RecipeDetailFragment mFragment;

    public static Intent newIntent(Context context, String recipeId) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
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
    protected void onStop() {
        getSupportFragmentManager().beginTransaction()
                .detach(mFragment)
                .commitAllowingStateLoss();
        super.onStop();
    }

    private void initFragment() {
        mFragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentByTag(RECIPE_FRAGMENT_TAG);
        if (mFragment == null) {
            mFragment = RecipeDetailFragment.newInstance("556476");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, mFragment, RECIPE_FRAGMENT_TAG)
                    .commit();
        }
    }
}