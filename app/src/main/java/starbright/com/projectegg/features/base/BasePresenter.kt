/**
 * Created by Andreas on 13/10/2019.
 */

package starbright.com.projectegg.features.base

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract

abstract class BasePresenter<V : BaseViewContract>(
        protected val schedulerProvider: SchedulerProviderContract,
        protected val compositeDisposable: CompositeDisposable,
        protected val networkHelper: NetworkHelper
) {

    private lateinit var view: V

    private fun attachView(view: V) {
        this.view = view
    }


}