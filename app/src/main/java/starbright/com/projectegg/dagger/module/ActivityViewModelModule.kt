package starbright.com.projectegg.dagger.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import starbright.com.projectegg.dagger.ViewModelKey
import starbright.com.projectegg.features.recipelist.RecipeListViewModel
import starbright.com.projectegg.features.search.SearchRecipeViewModel

@Module
abstract class ActivityViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchRecipeViewModel::class)
    abstract fun bindSearchRecipeViewModel(vm: SearchRecipeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecipeListViewModel::class)
    abstract fun bindRecipeListViewModel(vm: RecipeListViewModel): ViewModel
}