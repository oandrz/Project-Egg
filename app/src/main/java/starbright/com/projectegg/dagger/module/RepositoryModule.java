/**
 * Created by Andreas on 5/5/2019.
 */

package starbright.com.projectegg.dagger.module;

import dagger.Module;
import dagger.Provides;
import starbright.com.projectegg.dagger.qualifier.LocalDataStore;
import starbright.com.projectegg.dagger.qualifier.RemoteDataStore;
import starbright.com.projectegg.data.AppDataStore;
import starbright.com.projectegg.data.local.AppLocalDataStore;
import starbright.com.projectegg.data.remote.AppRemoteDataStore;

@Module
public class RepositoryModule {

    @Provides
    @LocalDataStore
    AppDataStore provideLocalDataStore(AppLocalDataStore dataStore) {
        return dataStore;
    }

    @Provides
    @RemoteDataStore
    AppDataStore provideRemoteDataStore(AppRemoteDataStore dataStore) {
        return dataStore;
    }
}
