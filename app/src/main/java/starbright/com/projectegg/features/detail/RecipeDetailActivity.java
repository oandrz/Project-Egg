package starbright.com.projectegg.features.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import starbright.com.projectegg.R;
import starbright.com.projectegg.features.base.BaseActivityWithToolbar;
import starbright.com.projectegg.features.base.WebviewActivity;

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
        setTitle(R.string.detail_title);
        setBackButtonEnabled(true);
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
        mFragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentByTag(RECIPE_FRAGMENT_TAG);
        if (mFragment == null) {
            mFragment = RecipeDetailFragment.newInstance(getIntent()
                    .getStringExtra(EXTRA_RECIPE_ID));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, mFragment, RECIPE_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void navigateToWebViewActivity(String url) {
        startActivity(WebviewActivity.newIntent(this, url));
    }
}