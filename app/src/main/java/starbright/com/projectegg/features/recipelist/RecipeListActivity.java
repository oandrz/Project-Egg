package starbright.com.projectegg.features.recipelist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import starbright.com.projectegg.R;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.features.base.BaseActivityWithToolbar;
import starbright.com.projectegg.features.detail.RecipeDetailActivity;

public class RecipeListActivity extends BaseActivityWithToolbar
        implements RecipeListFragment.FragmentListener {

    private static final String RECIPE_LIST_FRAGMENT_TAG = "RECIPE_LIST_FRAGMENT_TAG";
    private static final String INGREDIENT_EXTRA_KEY = "INGREDIENT_EXTRA_KEY";

    private RecipeListFragment mFragment;

    public static Intent newIntent(Context context, List<Ingredient> ingredients) {
        Intent intent = new Intent(context, RecipeListActivity.class);
        intent.putExtra(INGREDIENT_EXTRA_KEY, new ArrayList<>(ingredients));
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
        setBackButtonEnabled(true);
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
                .getParcelableArrayList(INGREDIENT_EXTRA_KEY);
        mFragment = (RecipeListFragment) getSupportFragmentManager()
                .findFragmentByTag(RECIPE_LIST_FRAGMENT_TAG);
        if (mFragment == null) {
            mFragment = RecipeListFragment.newInstance(ingredients);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, mFragment, RECIPE_LIST_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void navigateRecipeDetailActivity(String recipeId) {
        startActivity(RecipeDetailActivity.newIntent(this, recipeId));
    }
}
