/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.view

import android.view.View
import com.google.android.material.chip.Chip
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import starbright.com.projectegg.R

class HeaderWithChipsItem(
    val title: String,
    val collection: List<Chip>
) : AbstractItem<HeaderWithChipsItem.ViewHolder>() {

    /** The layout for the given item */
    override val layoutRes: Int
        get() = R.layout.item_header_with_chip

    /** The type of the Item. Can be a hardcoded INT, but preferred is a defined id */
    override val type: Int
        get() = R.id.filterItem

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     *
     * @return the ViewHolder for this Item
     */
    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    inner class ViewHolder(itemView: View) : FastAdapter.ViewHolder<HeaderWithChipsItem>(itemView) {

        /** Binds the data of this item onto the viewHolder */
        override fun bindView(item: HeaderWithChipsItem, payloads: List<Any>) {
            itemView.run {
//                tv_title.text = item.title
//                item.collection.forEachIndexed { index, chip ->
//                    group_filter.addView(chip)
//                }
            }
        }

        /** View needs to release resources when its recycled */
        override fun unbindView(item: HeaderWithChipsItem) {
//            itemView.run {
//                tv_title.text = null
//                group_filter.removeAllViews()
//            }
        }
    }
}
