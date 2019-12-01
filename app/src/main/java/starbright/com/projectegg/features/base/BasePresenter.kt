/**
 * Created by Andreas on 13/10/2019.
 */

package starbright.com.projectegg.features.base

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.R
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.net.ssl.HttpsURLConnection

abstract class BasePresenter<V : BaseViewContract>(
    protected val schedulerProvider: SchedulerProviderContract,
    protected val compositeDisposable: CompositeDisposable,
    protected val networkHelper: NetworkHelper
) : BasePresenterContract {

    protected lateinit var view: V

    override fun onStopScreen() {
        unSubscribe()
    }

    fun attachView(view: V) {
        this.view = view
    }

    private fun unSubscribe() {
        compositeDisposable.dispose()
    }

    protected fun isConnectedToInternet(): Boolean = networkHelper.isConnectedWithNetwork()

    protected fun handleNetworkError(error: Throwable) {
        networkHelper.castToNetworkError(error).run {
            val baseView = view as BaseViewContract
            when (statusCode) {
                -1 -> baseView.showError(R.string.network_default_error)
                0 -> baseView.showError(R.string.server_connection_error)
                HttpsURLConnection.HTTP_UNAUTHORIZED -> {
                    forceLogout()
                    baseView.showError(R.string.server_connection_error)
                }
                HttpsURLConnection.HTTP_INTERNAL_ERROR -> {
                    baseView.showError(R.string.network_internal_error)
                }
                HttpsURLConnection.HTTP_UNAVAILABLE -> {
                    baseView.showError(R.string.network_server_not_available)
                }
                else -> view.showError(text = message)
            }
        }
    }

    protected fun forceLogout() {
        val baseView = view as BaseViewContract
        baseView.navigateToHome()
    }
}