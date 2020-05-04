package starbright.com.projectegg.features.home.setting

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

class SettingPresenter @Inject constructor(
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    schedulerProvider: SchedulerProviderContract,
    private val repository: AppRepository
) : BasePresenter<SettingContract.View>(schedulerProvider, compositeDisposable, networkHelper) {
    override fun onCreateScreen() {

    }
}