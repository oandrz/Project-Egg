/**
 * Created by Andreas on 20/1/2019.
 */

package starbright.com.projectegg.features.filter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import starbright.com.projectegg.R

class FilterFragment : Fragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_filter, container, false)
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(): FilterFragment {
            return FilterFragment()
        }
    }
}