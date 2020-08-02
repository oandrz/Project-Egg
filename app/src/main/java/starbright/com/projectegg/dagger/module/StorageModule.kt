/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

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
import starbright.com.projectegg.data.local.database.ApplicationDatabase
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context: Context): ApplicationDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            ApplicationDatabase::class.java,
            "database"
        ).build()
}
