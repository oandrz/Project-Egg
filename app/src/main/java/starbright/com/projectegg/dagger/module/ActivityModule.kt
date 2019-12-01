/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.util.IngredientRecognizer

@Module
class ActivityModule(private val activity: BaseActivity<*, *>) {

    @Provides
    fun providesClarifaiHelper(client: OkHttpClient): IngredientRecognizer {
        return IngredientRecognizer(client)
    }
}