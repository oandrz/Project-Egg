/**
 * Created by Andreas on 5/5/2019.
 */

package starbright.com.projectegg.dagger.module

import dagger.Module
import dagger.Provides
import starbright.com.projectegg.dagger.qualifier.LocalData
import starbright.com.projectegg.dagger.qualifier.RemoteData
import starbright.com.projectegg.data.AppDataStore
import starbright.com.projectegg.data.local.AppLocalDataStore
import starbright.com.projectegg.data.remote.AppRemoteDataStore

@Module
class DataStoreModule {

    @Provides
    @LocalData
    fun provideLocalDataStore(dataStore: AppLocalDataStore): AppDataStore {
        return dataStore
    }

    @Provides
    @RemoteData
    fun provideRemoteDataStore(dataStore: AppRemoteDataStore): AppDataStore {
        return dataStore
    }
}
