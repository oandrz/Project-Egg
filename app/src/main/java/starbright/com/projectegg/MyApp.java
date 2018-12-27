package starbright.com.projectegg;

import android.app.Application;
import android.os.Build;

import com.google.android.gms.ads.MobileAds;

import starbright.com.projectegg.dagger.component.AppComponent;
import starbright.com.projectegg.dagger.component.DaggerAppComponent;
import starbright.com.projectegg.dagger.module.AppModule;
import starbright.com.projectegg.dagger.module.DataModule;
import starbright.com.projectegg.util.Constants;

public class MyApp extends Application {

    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule(BuildConfig.BASE_URL, Constants.DATABASE_NAME))
                .build();
        MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID));
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static boolean isPreLolipop() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }
}
