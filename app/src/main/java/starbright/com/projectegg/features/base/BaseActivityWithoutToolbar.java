package starbright.com.projectegg.features.base;

import android.os.Bundle;

import starbright.com.projectegg.R;

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
