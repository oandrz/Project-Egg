/**
 * Created by Andreas on 4/8/2018.
 */

package starbright.com.projectegg.dagger.module

import android.app.Application
import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import id.zelory.compressor.Compressor
import starbright.com.projectegg.dagger.qualifier.ApplicationContext
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider
import starbright.com.projectegg.util.scheduler.SchedulerProvider

@Module
class AppModule(private val application: Application) {

    @Provides
    @ApplicationContext
    @Singleton
    internal fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    internal fun providesSchedulerComposer(): BaseSchedulerProvider {
        return SchedulerProvider.getInstance()
    }

    @Provides
    @Singleton
    internal fun providesCompressor(@ApplicationContext context: Context): Compressor {
        return Compressor(context)
    }
}
