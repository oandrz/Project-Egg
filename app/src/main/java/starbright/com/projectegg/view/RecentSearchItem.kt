/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 21 - 8 - 2020.
 */

package starbright.com.projectegg.view

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_single_text.view.*
import starbright.com.projectegg.R

class RecentSearchItem(
    val text: String,
    val listener: ((String, Int) -> Unit)?
) : AbstractItem<RecentSearchItem.ViewHolder>() {

    /** The layout for the given item */
    override val layoutRes: Int
        get() = R.layout.item_single_text

    /** The type of the Item. Can be a hardcoded INT, but preferred is a defined id */
    override val type: Int
        get() = R.id.singleTextItem

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     *
     * @return the ViewHolder for this Item
     */
    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    inner class ViewHolder(itemView: View) : FastAdapter.ViewHolder<RecentSearchItem>(itemView) {
        override fun bindView(item: RecentSearchItem, payloads: List<Any>) {
            itemView.tv_text.text = item.text
            ///TODO: Warning May Affect RecyclerView Performance
            itemView.iv_clear.setOnClickListener {
                listener?.invoke(item.text, adapterPosition)
            }
        }

        /** View needs to release resources when its recycled */
        override fun unbindView(item: RecentSearchItem) {
            itemView.tv_text.text = null
        }
    }
}