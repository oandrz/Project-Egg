/**
 * Created by Andreas on 13/10/2019.
 */

package starbright.com.projectegg.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkHelper(val context: Context) {
    companion object {

    }

    fun isConnectedWithNetwork(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }
}