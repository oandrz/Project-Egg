/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.module

import android.app.Activity
import dagger.Module
import starbright.com.projectegg.dagger.qualifier.ActivityContext

@Module
class ActivityModule(private val activity: Activity) {

    @ActivityContext
    fun provideContext() = activity
}