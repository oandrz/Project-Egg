/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */


package starbright.com.projectegg.dagger.component

import dagger.Component
import starbright.com.projectegg.dagger.module.ActivityModule
import starbright.com.projectegg.dagger.module.ActivityViewModelModule
import starbright.com.projectegg.dagger.module.FragmentFactoryModule
import starbright.com.projectegg.dagger.module.ViewModelFactoryModule
import starbright.com.projectegg.dagger.scope.ActivityScope
import starbright.com.projectegg.features.detail.RecipeDetailActivity
import starbright.com.projectegg.features.home.HomeActivity
import starbright.com.projectegg.features.ingredients.IngredientsActivity
import starbright.com.projectegg.features.recipelist.RecipeListActivity
import starbright.com.projectegg.features.search.SearchRecipeActivity

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [
        ActivityModule::class,
        ViewModelFactoryModule::class,
        ActivityViewModelModule::class,
        FragmentFactoryModule::class
    ]
)
interface ActivityComponent {
    fun inject(activity: RecipeListActivity)
    fun inject(activity: IngredientsActivity)
    fun inject(activity: RecipeDetailActivity)
    fun inject(activity: HomeActivity)
    fun inject(activity: SearchRecipeActivity)
}