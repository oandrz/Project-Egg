/**
 * Created by Andreas on 5/5/2019.
 */

package starbright.com.projectegg.dagger.module

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import starbright.com.projectegg.dagger.qualifier.ApplicationContext
import starbright.com.projectegg.data.local.RecipeDatabase
import starbright.com.projectegg.util.Constants

@Module
class StorageModule {

    @Provides
    @Singleton
    internal fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context): RecipeDatabase {
        return Room.databaseBuilder(context, RecipeDatabase::class.java,
                Constants.DATABASE_NAME).build()
    }
}
