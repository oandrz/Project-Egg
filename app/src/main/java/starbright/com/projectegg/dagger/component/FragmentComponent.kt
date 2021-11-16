/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.component

import dagger.Component
import starbright.com.projectegg.dagger.module.FragmentModule
import starbright.com.projectegg.dagger.module.ViewModelFactoryModule
import starbright.com.projectegg.dagger.module.FragmentViewModelModule
import starbright.com.projectegg.dagger.scope.FragmentScope
import starbright.com.projectegg.features.home.bookmark.FavouriteListFragment
import starbright.com.projectegg.features.home.list.RecipeHomeFragment

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [
        FragmentModule::class,
        FragmentViewModelModule::class,
        ViewModelFactoryModule::class
    ]
)
interface FragmentComponent {
    fun inject(fragment: RecipeHomeFragment)
    fun inject(fragment: FavouriteListFragment)
}