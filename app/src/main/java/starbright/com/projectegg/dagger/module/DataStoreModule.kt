/**
 * Created by Andreas on 5/5/2019.
 */

package starbright.com.projectegg.dagger.module

import dagger.Binds
import dagger.Module
import starbright.com.projectegg.dagger.qualifier.LocalData
import starbright.com.projectegg.dagger.qualifier.RemoteData
import starbright.com.projectegg.data.AppDataStore
import starbright.com.projectegg.data.local.AppLocalDataStore
import starbright.com.projectegg.data.remote.AppRemoteDataStore

@Module
abstract class DataStoreModule {

    @Binds
    @LocalData
    abstract fun provideLocalDataStore(dataStore: AppLocalDataStore): AppDataStore

    @Binds
    @RemoteData
    abstract fun provideRemoteDataStore(dataStore: AppRemoteDataStore): AppDataStore
}
