package starbright.com.projectegg.util.scheduler

import io.reactivex.Scheduler
import javax.inject.Singleton

/**
 * Created by Andreas on 4/14/2018.
 */
@Singleton
interface SchedulerProviderContract {
    fun computation(): Scheduler
    fun io(): Scheduler
    fun ui(): Scheduler
}