package starbright.com.projectegg;

import android.app.Application;
import android.os.Build;

import starbright.com.projectegg.dagger.component.AppComponent;
import starbright.com.projectegg.dagger.component.DaggerAppComponent;
import starbright.com.projectegg.dagger.module.AppModule;
import starbright.com.projectegg.dagger.module.DataModule;

/**
 * Created by Andreas on 4/8/2018.
 */

public class MyApp extends Application {

    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule(BuildConfig.BASE_URL))
                .build();
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static boolean isPreLolipop() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }
}
