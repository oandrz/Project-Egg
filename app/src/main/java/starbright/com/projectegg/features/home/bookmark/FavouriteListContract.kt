/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.home.bookmark

import starbright.com.projectegg.features.base.BasePresenterContract
import starbright.com.projectegg.features.base.BaseViewContract

class FavouriteListContract {
    interface View : BaseViewContract {
        fun setupView()
    }

    interface Presenter : BasePresenterContract {

    }
}