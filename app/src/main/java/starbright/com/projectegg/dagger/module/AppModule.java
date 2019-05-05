package starbright.com.projectegg.dagger.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import id.zelory.compressor.Compressor;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import starbright.com.projectegg.BuildConfig;
import starbright.com.projectegg.dagger.qualifier.ApplicationContext;
import starbright.com.projectegg.data.local.RecipeDatabase;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;
import starbright.com.projectegg.util.scheduler.SchedulerProvider;

/**
 * Created by Andreas on 4/8/2018.
 */

@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    @Singleton
    Context provideContext() { return mApplication; }

    @Provides
    @Singleton
    BaseSchedulerProvider providesSchedulerComposer() {
        return SchedulerProvider.getInstance();
    }

    @Provides
    @Singleton
    Compressor providesCompressor(@ApplicationContext Context context) {
        return new Compressor(context);
    }
}
