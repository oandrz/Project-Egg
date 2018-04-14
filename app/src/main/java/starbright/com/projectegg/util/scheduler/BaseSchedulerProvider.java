package starbright.com.projectegg.util.scheduler;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * Created by Andreas on 4/14/2018.
 */

public interface BaseSchedulerProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
