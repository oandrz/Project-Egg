package starbright.com.projectegg.features.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import junit.framework.Assert;

import butterknife.ButterKnife;
import starbright.com.projectegg.R;

/**
 * Created by Andreas on 4/8/2018.
 */

public class BaseActivity extends AppCompatActivity {


    private boolean mFirstAttach = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void setToolbarTitle(String title) {
        final ActionBar actionBar = getSupportActionBar();
        Assert.assertNotNull(actionBar);
        actionBar.setTitle(title);
    }

    protected void setFragment(String tag, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, fragment, tag);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (mFirstAttach) {
            fragmentTransaction.addToBackStack(null);
            mFirstAttach = false;
        }
        fragmentTransaction.commit();
    }

    protected void setBackButtonEnabled(boolean enabled) {
        final ActionBar actionBar = getSupportActionBar();
        Assert.assertNotNull(actionBar);
        actionBar.setDisplayHomeAsUpEnabled(enabled);
        actionBar.setHomeButtonEnabled(enabled);
    }

    protected String getLastBackStackTag() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final int count = fragmentManager.getBackStackEntryCount();
        return count > 0 ? fragmentManager.getBackStackEntryAt(count - 1).getName() : "";
    }
}
