package starbright.com.projectegg.features.home

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

class HomePresenter @Inject constructor(
    schedulerProvider: SchedulerProviderContract,
    compositeDiposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val repository: AppRepository
) : BasePresenter<HomeContract.View>(
    schedulerProvider, compositeDiposable, networkHelper
), HomeContract.Presenter {

    override fun onCreateScreen() {

    }
}