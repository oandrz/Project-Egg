/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.module

import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import starbright.com.projectegg.util.IngredientRecognizer

@Module
class ActivityModule(private val supportFragmentManager: FragmentManager) {

    @Provides
    fun providesClarifaiHelper(client: OkHttpClient): IngredientRecognizer {
        return IngredientRecognizer(client)
    }

    @Provides
    fun provideFragmentManager(): FragmentManager = supportFragmentManager
}