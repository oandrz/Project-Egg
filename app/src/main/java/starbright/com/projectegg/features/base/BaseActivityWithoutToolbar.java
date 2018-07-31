package starbright.com.projectegg.features.base;

import android.os.Bundle;

import starbright.com.projectegg.R;

/**
 * Created by Andreas on 4/8/2018.
 */

public class BaseActivityWithoutToolbar extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_without_toolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
