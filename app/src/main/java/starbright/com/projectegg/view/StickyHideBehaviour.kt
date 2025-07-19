/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior

private const val FLING_TRESSHOLD_SLIDE_UP = -2100
private const val FLING_TRESSHOLD_SLIDE_DOWN = 2100

class StickyHideBehaviour<V : View> : HideBottomViewOnScrollBehavior<V> {

    constructor() : super()

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    @Suppress("DEPRECATION")
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        if (dyConsumed > 0) {
            slideDown(child)
        } else if (dyConsumed == 0 && dyUnconsumed < 0) {
            slideUp(child)
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (velocityY >= FLING_TRESSHOLD_SLIDE_DOWN) {
            slideDown(child)
        } else if (velocityY <= FLING_TRESSHOLD_SLIDE_UP) {
            slideUp(child)
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }
}
