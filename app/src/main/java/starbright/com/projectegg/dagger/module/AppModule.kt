/**
 * Created by Andreas on 4/8/2018.
 */

package starbright.com.projectegg.dagger.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import id.zelory.compressor.Compressor
import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.dagger.qualifier.ApplicationContext
import starbright.com.projectegg.util.ClarifaiHelper
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider
import starbright.com.projectegg.util.scheduler.SchedulerProvider
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun providesSchedulerComposer(): BaseSchedulerProvider {
        return SchedulerProvider.getInstance()
    }

    @Provides
    @Singleton
    fun providesCompressor(@ApplicationContext context: Context): Compressor {
        return Compressor(context)
    }

    @Provides
    @Singleton
    fun providesClarifaiHelper(@ApplicationContext context: Context): ClarifaiHelper {
        return ClarifaiHelper(context)
    }

    @Provides
    @Singleton
    fun providesCompositeDiposable(): CompositeDisposable {
        return CompositeDisposable()
    }
}
