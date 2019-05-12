/**
 * Created by Andreas on 12/5/2019.
 */

package starbright.com.projectegg.dagger.qualifier

import javax.inject.Qualifier
import kotlin.annotation.Retention

@Qualifier
annotation class LocalData

@Qualifier
annotation class ApplicationContext

@Qualifier
annotation class RemoteData
