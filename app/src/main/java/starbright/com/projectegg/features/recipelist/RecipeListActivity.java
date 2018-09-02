package starbright.com.projectegg.features.recipelist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import starbright.com.projectegg.R;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.features.base.BaseActivityWithToolbar;

public class RecipeListActivity extends BaseActivityWithToolbar {

    private static final String RECIPE_LIST_FRAGMENT_TAG = "RECIPE_LIST_FRAGMENT_TAG";
    private static final String RECIPE_ID_EXTRA_KEY = "RECIPE_ID_EXTRA_KEY";

    private RecipeListFragment mFragment;

    public static Intent newIntent(Context context, List<Ingredient> ingredients) {
        Intent intent = new Intent(context, RecipeListActivity.class);
        intent.putExtra(RECIPE_ID_EXTRA_KEY, new ArrayList<>(ingredients));
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
        final ArrayList<Ingredient> ingredients = getIntent().getExtras()
                .getParcelableArrayList(RECIPE_LIST_FRAGMENT_TAG);
        mFragment = (RecipeListFragment) getSupportFragmentManager()
                .findFragmentByTag(RECIPE_LIST_FRAGMENT_TAG);
        if (mFragment == null) {
            mFragment = RecipeListFragment.newInstance(ingredients);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, mFragment, RECIPE_LIST_FRAGMENT_TAG)
                    .commit();
        }
    }
}
