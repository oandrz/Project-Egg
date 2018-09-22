/**
 * Created by Andreas on 22/9/2018.
 */

/**
 * Created by Andreas on 10/9/2018.
 */

package starbright.com.projectegg.features.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;

import starbright.com.projectegg.BuildConfig;
import starbright.com.projectegg.R;

/**
 * Created by Andreas on 4/8/2018.
 */

abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, BuildConfig.ADMOB_APP_ID);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (!(fragment instanceof IOnBackPressedListener)
                || !((IOnBackPressedListener) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}
