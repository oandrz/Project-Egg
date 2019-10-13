/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.component

import dagger.Component
import starbright.com.projectegg.dagger.module.ActivityModule
import starbright.com.projectegg.dagger.scope.ActivityScope

@ActivityScope
@Component(
        dependencies = [ApplicationComponent::class],
        modules = [ActivityModule::class]
)
interface ActivityComponent {

}