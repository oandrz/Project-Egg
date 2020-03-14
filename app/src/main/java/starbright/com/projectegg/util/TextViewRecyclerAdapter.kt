/**
 * Created by Andreas on 19/8/2018.
 */

/**
 * Created by Andreas on 19/8/2018.
 */

/**
 * Created by Andreas on 19/8/2018.
 */

package starbright.com.projectegg.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_text_only.view.*
import starbright.com.projectegg.R

class TextViewRecyclerAdapter(private val mContext: Context, private val mDataSource: List<String>)
    : RecyclerView.Adapter<TextViewRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.item_text_only, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_text.text = mDataSource[position]
    }

    override fun getItemCount(): Int {
        return mDataSource.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
