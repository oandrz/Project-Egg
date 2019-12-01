/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.dagger.component

import dagger.Component
import id.zelory.compressor.Compressor
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import starbright.com.projectegg.dagger.module.AppModule
import starbright.com.projectegg.dagger.module.DataStoreModule
import starbright.com.projectegg.dagger.module.NetworkModule
import starbright.com.projectegg.dagger.module.StorageModule
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    StorageModule::class,
    DataStoreModule::class
])
interface ApplicationComponent {
    fun getSchedulerProvider(): SchedulerProviderContract
    fun getCompositeDiposable(): CompositeDisposable
    fun getCompressor(): Compressor
    fun getNetworkHelper(): NetworkHelper
    fun getAppRepository(): AppRepository
    fun getClient(): OkHttpClient
}
