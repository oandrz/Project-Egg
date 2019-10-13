/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.dagger.scope

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class ActivityScope

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class FragmentScope