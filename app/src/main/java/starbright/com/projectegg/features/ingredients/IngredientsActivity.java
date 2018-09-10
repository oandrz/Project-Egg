/**
 * Created by Andreas on 10/9/2018.
 */

package starbright.com.projectegg.features.ingredients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import starbright.com.projectegg.R;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.features.base.BaseActivityWithoutToolbar;
import starbright.com.projectegg.features.recipelist.RecipeListActivity;
import starbright.com.projectegg.features.userAccount.UserAccountActivity;

public class IngredientsActivity extends BaseActivityWithoutToolbar
        implements IngredientsFragment.FragmentListener {

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

    @Override
    protected void onDestroy() {
        getSupportFragmentManager().beginTransaction()
                .detach(mFragment)
                .commitAllowingStateLoss();
        super.onDestroy();
    }

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

    @Override
    public void navigateUserAccountActivity() {
        startActivity(UserAccountActivity.newIntent(this));
        finish();
    }

    @Override
    public void navigateRecipeListActivity(List<Ingredient> ingredients) {
        startActivity(RecipeListActivity.newIntent(this, ingredients));
    }
}
