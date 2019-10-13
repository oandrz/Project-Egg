/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.component

import dagger.Component
import starbright.com.projectegg.dagger.module.FragmentModule

@Component(
        dependencies = [ApplicationComponent::class],
        modules = [FragmentModule::class]
)
interface FragmentComponent {
}