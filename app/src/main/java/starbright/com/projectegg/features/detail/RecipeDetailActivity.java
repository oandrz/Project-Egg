/**
 * Created by Andreas on 5/8/2018.
 */

package starbright.com.projectegg.features.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import starbright.com.projectegg.features.base.BaseActivityWithToolbar;

public class RecipeDetailActivity extends BaseActivityWithToolbar {

    public static Intent newIntent(Context context) {
        return new Intent(context, RecipeDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
