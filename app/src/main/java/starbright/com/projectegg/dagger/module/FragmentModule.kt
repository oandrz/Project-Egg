/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.module

import androidx.fragment.app.Fragment
import dagger.Module
import dagger.Provides
import starbright.com.projectegg.dagger.qualifier.FragmentContext

@Module
class FragmentModule(private val fragment: Fragment) {

    @FragmentContext
    @Provides
    fun provideContext() = fragment

}