/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.module

import dagger.Module
import dagger.Provides
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.util.ClarifaiHelper

@Module
class ActivityModule(private val activity: BaseActivity<*, *>) {

    @Provides
    fun providesClarifaiHelper(): ClarifaiHelper {
        return ClarifaiHelper(activity)
    }
}