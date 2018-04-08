package starbright.com.projectegg.data.remote;

import javax.inject.Inject;

import retrofit2.Retrofit;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.data.AppDataStore;
import starbright.com.projectegg.data.local.AppLocalDataStore;

/**
 * Created by Andreas on 4/8/2018.
 */

public class AppRemoteDataStore implements AppDataStore {
    @Inject
    Retrofit retrofit;

    @Inject
    AppLocalDataStore appLocalDataStore;

    public AppRemoteDataStore() {
        MyApp.getAppComponent().inject(this);
    }

}
