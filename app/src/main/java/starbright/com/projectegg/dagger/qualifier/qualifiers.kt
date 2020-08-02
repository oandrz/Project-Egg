/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 12/5/2019.
 */

package starbright.com.projectegg.dagger.qualifier

import javax.inject.Qualifier

@Qualifier
annotation class LocalData

@Qualifier
annotation class ApplicationContext

@Qualifier
annotation class ActivityContext

@Qualifier
annotation class FragmentContext

@Qualifier
annotation class RemoteData
