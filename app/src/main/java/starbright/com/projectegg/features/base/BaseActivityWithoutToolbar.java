package starbright.com.projectegg.features.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import starbright.com.projectegg.R;

public class BaseActivityWithoutToolbar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_without_toolbar);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    protected void setFragment(String tag, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.contentFrame, fragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(tag)
                .commit();
    }

    protected String getLastBackStackTag() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final int count = fragmentManager.getBackStackEntryCount();
        return count > 0 ? fragmentManager.getBackStackEntryAt(count - 1).getName() : "";
    }

    private boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
