/**
 * Created by Andreas on 2/11/2019.
 */

package starbright.com.projectegg.features.base

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.lang.ref.WeakReference

const val UNKNOWN_RESOURCE: Int = -1
class NormalToolbar(
    private val activity: WeakReference<AppCompatActivity>,
    private val toolbarId: Int,
    @StringRes private val toolbarTitleRes: Int,
    private val isShowBackButton: Boolean = true
) : ToolbarBehavior {

    override fun buildToolbar() {
        val toolbar = activity.get()?.findViewById<Toolbar>(toolbarId)
        if (toolbar != null) {
            activity.get()?.let { appCompatActivity ->
                appCompatActivity.setSupportActionBar(toolbar)
                appCompatActivity.supportActionBar?.let { supportActionBar ->
                    supportActionBar.title = if (toolbarTitleRes == UNKNOWN_RESOURCE) {
                        ""
                    } else {
                        activity.get()?.resources?.getString(toolbarTitleRes)
                    }
                    supportActionBar.setDisplayHomeAsUpEnabled(isShowBackButton)
                }
            }
        } else {
            throw UnsupportedOperationException("Do not forget to add your toolbar in the layout")
        }
    }

}