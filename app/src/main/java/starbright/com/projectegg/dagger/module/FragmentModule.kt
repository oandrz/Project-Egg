/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.module

import dagger.Module
import dagger.Provides
import starbright.com.projectegg.dagger.qualifier.FragmentContext
import starbright.com.projectegg.features.base.BaseFragment

@Module
class FragmentModule(private val fragment: BaseFragment<*, *>) {

    @FragmentContext
    @Provides
    fun provideContext() = fragment
}