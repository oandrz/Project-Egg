package starbright.com.projectegg.features.base;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import junit.framework.Assert;

import butterknife.ButterKnife;
import starbright.com.projectegg.R;

/**
 * Created by Andreas on 4/8/2018.
 */

public class BaseActivityWithToolbar extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void setToolbarTitle(String title) {
        final ActionBar actionBar = getSupportActionBar();
        Assert.assertNotNull(actionBar);
        actionBar.setTitle(title);
    }

    protected void setBackButtonEnabled(boolean enabled) {
        final ActionBar actionBar = getSupportActionBar();
        Assert.assertNotNull(actionBar);
        actionBar.setDisplayHomeAsUpEnabled(enabled);
        actionBar.setHomeButtonEnabled(enabled);
    }
}
