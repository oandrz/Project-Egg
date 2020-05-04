package starbright.com.projectegg.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior

private const val FLING_TRESSHOLD_SLIDE_UP = -2100
private const val FLING_TRESSHOLD_SLIDE_DOWN = 2100

class StickyHideBehaviour<V : View?> : HideBottomViewOnScrollBehavior<V> {

    constructor() : super()

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        if (dyConsumed > 0) {
            slideDown(child)
        } else if (dyConsumed == 0 && dyUnconsumed < 0) {
            slideUp(child)
        }
    }

    override fun onNestedFling(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        if (velocityY >= FLING_TRESSHOLD_SLIDE_DOWN) {
            slideDown(child)
        } else if (velocityY <= FLING_TRESSHOLD_SLIDE_UP) {
            slideUp(child)
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
    }
}
