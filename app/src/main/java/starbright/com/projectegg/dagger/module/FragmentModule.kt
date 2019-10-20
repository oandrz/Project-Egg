/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.module

import android.support.v4.app.Fragment
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.dagger.qualifier.FragmentContext
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.features.recipelist.RecipeListPresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract

@Module
class FragmentModule(private val fragment: Fragment) {

    @FragmentContext
    @Provides
    fun provideContext() = fragment

    @Provides
    fun provideRecipeListPresenter(
            schedulerProvider: SchedulerProviderContract,
            compositeDisposable: CompositeDisposable,
            networkHelper: NetworkHelper,
            repository: AppRepository
    ): RecipeListPresenter =
            RecipeListPresenter(
                    schedulerProvider, compositeDisposable, networkHelper, repository
            )
}