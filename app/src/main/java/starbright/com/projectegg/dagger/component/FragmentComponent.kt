/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.component

import dagger.Component
import starbright.com.projectegg.dagger.module.FragmentModule
import starbright.com.projectegg.dagger.scope.FragmentScope
import starbright.com.projectegg.features.home.list.RecipeHomeFragment

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {
    fun inject(fragment: RecipeHomeFragment)
}