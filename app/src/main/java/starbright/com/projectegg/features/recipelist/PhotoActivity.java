package starbright.com.projectegg.features.recipelist;

import android.os.Bundle;

import starbright.com.projectegg.features.base.BaseActivity;
import starbright.com.projectegg.R;

public class PhotoActivity extends BaseActivity  {

    private static final String RECIPE_LIST_FRAGMENT_TAG = "RECIPE_LIST_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(getString(R.string.app_name));
        RecipeListFragment fragment = (RecipeListFragment) getSupportFragmentManager()
                .findFragmentByTag(RECIPE_LIST_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = RecipeListFragment.newInstance();
        }
        setFragment(RECIPE_LIST_FRAGMENT_TAG, fragment);
    }
}
