/**
 * Created by Andreas on 2/11/2019.
 */

package starbright.com.projectegg.features.base

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.lang.ref.WeakReference

class NormalToolbar(
    private val activity: WeakReference<AppCompatActivity>,
    private val toolbarId: Int,
    @StringRes private val toolbarTitleRes: Int
) : ToolbarBehavior {

    override fun buildToolbar() {
        val toolbar = activity.get()?.findViewById<Toolbar>(toolbarId)
        if (toolbar != null) {
            activity.get()?.let { appCompatActivity ->
                appCompatActivity.setSupportActionBar(toolbar)
                appCompatActivity.supportActionBar?.let { supportActionBar ->
                    supportActionBar.title = activity.get()?.resources?.getString(toolbarTitleRes)
                    supportActionBar.setDisplayHomeAsUpEnabled(true)
                }
            }
        } else {
            throw UnsupportedOperationException("Do not forget to add your toolbar in the layout")
        }
    }

}