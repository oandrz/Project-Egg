package starbright.com.projectegg.features.home.list

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.dagger.scope.FragmentScope
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.RxSchedulerProvider
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

@FragmentScope
class RecipeHomePresenter @Inject constructor(
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    schedulerProvider: SchedulerProviderContract,
    private val repository: AppRepository
): BasePresenter<RecipeHomeContract.View>(schedulerProvider, compositeDisposable, networkHelper) {
    override fun onCreateScreen() {
    }
}