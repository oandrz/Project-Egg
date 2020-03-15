/**
 * Created by Andreas on 5/5/2019.
 */

package starbright.com.projectegg.dagger.module

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import starbright.com.projectegg.dagger.qualifier.ApplicationContext
import starbright.com.projectegg.data.local.RecipeDatabase
import starbright.com.projectegg.util.Constants
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RecipeDatabase {
        return Room.databaseBuilder(context, RecipeDatabase::class.java,
            Constants.DATABASE_NAME).build()
    }
}
