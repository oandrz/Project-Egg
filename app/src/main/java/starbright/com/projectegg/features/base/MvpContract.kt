/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 13/10/2019.
 */

package starbright.com.projectegg.features.base

import androidx.annotation.StringRes

interface BaseViewContract {
    fun showError(@StringRes res: Int? = null, text: String? = null)
    fun navigateToHome()
}

interface BasePresenterContract {
    fun onCreateScreen()
    fun onStopScreen()
}