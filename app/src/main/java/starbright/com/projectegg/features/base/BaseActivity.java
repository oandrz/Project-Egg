package starbright.com.projectegg.features.base;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import starbright.com.projectegg.R;

/**
 * Created by Andreas on 4/8/2018.
 */

abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (!(fragment instanceof IOnBackPressedListener) || !((IOnBackPressedListener) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}
