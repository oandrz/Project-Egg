/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.dagger.module

import dagger.Binds
import dagger.Module
import starbright.com.projectegg.features.home.BottomNavigationFactory
import starbright.com.projectegg.features.home.HomeBottomNavigationFactory

@Module
abstract class FragmentFactoryModule() {
    @Binds
    abstract fun provideBottomNavigationFactory(factory: HomeBottomNavigationFactory) : BottomNavigationFactory
}