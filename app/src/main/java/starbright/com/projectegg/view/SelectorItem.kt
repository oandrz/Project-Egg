/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.view

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import starbright.com.projectegg.R
import starbright.com.projectegg.util.GlideApp

class SelectorItem(
    val text: String,
    val imageUrl: String,
    val isCheckShown: Boolean
) : AbstractItem<SelectorItem.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.item_single_line

    override val type: Int
        get() = R.id.singleLineItem

    override fun getViewHolder(v: View): SelectorItem.ViewHolder = ViewHolder(v)

    inner class ViewHolder(itemView: View) : FastAdapter.ViewHolder<SelectorItem>(itemView) {
        override fun bindView(item: SelectorItem, payloads: List<Any>) {
            itemView.run {
//                tv_item.text = item.text
//                GlideApp.with(context)
//                    .load(item.imageUrl)
//                    .into(iv_icon)
//                iv_check.visibility = if (item.isCheckShown) View.VISIBLE else View.GONE
            }
        }

        override fun unbindView(item: SelectorItem) {
            itemView.run {
//                tv_item.text = null
//                iv_icon.setImageDrawable(null)
//                iv_check.setImageDrawable(null)
            }
        }
    }
}