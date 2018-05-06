package starbright.com.projectegg.dagger.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import starbright.com.projectegg.BuildConfig;
import starbright.com.projectegg.data.local.AppLocalDataStore;
import starbright.com.projectegg.data.remote.AppRemoteDataStore;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;
import starbright.com.projectegg.util.scheduler.SchedulerProvider;

/**
 * Created by Andreas on 4/8/2018.
 */

@Module
public class DataModule {

    private String mBaseUrl;

    public DataModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder()
                        .addHeader("X-Mashape-Key", BuildConfig.SPOON_KEY)
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(request);
            }
        });
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    AppLocalDataStore porvidesAppLocalDataStore(Application context) {
        return new AppLocalDataStore();
    }

    @Provides
    @Singleton
    AppRemoteDataStore providesAppRemoteDataStore() {
        return new AppRemoteDataStore();
    }

    @Provides
    @Singleton
    BaseSchedulerProvider providesSchedulerComposer() {
        return SchedulerProvider.getInstance();
    }
}
