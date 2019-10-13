/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.module

import android.support.v4.app.Fragment
import dagger.Module
import starbright.com.projectegg.dagger.qualifier.FragmentContext

@Module
class FragmentModule(private val fragment: Fragment) {

    @FragmentContext
    fun provideContext() = fragment
}