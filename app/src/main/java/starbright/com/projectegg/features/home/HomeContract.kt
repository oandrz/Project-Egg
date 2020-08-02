/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.home

import starbright.com.projectegg.features.base.BasePresenterContract
import starbright.com.projectegg.features.base.BaseViewContract

class HomeContract {
    interface View : BaseViewContract {
        fun setupBottomSheet()
    }

    interface Presenter : BasePresenterContract {

    }
}