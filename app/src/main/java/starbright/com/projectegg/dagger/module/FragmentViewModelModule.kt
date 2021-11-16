package starbright.com.projectegg.dagger.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import starbright.com.projectegg.dagger.ViewModelKey
import starbright.com.projectegg.features.home.bookmark.FavoriteListViewModel
import starbright.com.projectegg.features.home.list.RecipeHomeViewModel

@Module
abstract class FragmentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecipeHomeViewModel::class)
    abstract fun bindRecipeHomeViewModel(vm: RecipeHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteListViewModel::class)
    abstract fun bindFavoriteListViewModel(vm: FavoriteListViewModel): ViewModel
}