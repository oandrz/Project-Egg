/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.view

import starbright.com.projectegg.databinding.ItemSingleLineBinding
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

    override fun getViewHolder(v: View): SelectorItem.ViewHolder = ViewHolder(ItemSingleLineBinding.bind(v))

    inner class ViewHolder(
        val binding: ItemSingleLineBinding
    ) : FastAdapter.ViewHolder<SelectorItem>(binding.root) {
        override fun bindView(item: SelectorItem, payloads: List<Any>) {
            binding.tvItem.text = item.text
            GlideApp.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.ivIcon)
            binding.ivCheck.visibility = if (item.isCheckShown) View.VISIBLE else View.GONE
        }

        override fun unbindView(item: SelectorItem) {
            binding.tvItem.text = null
            binding.ivIcon.setImageDrawable(null)
            binding.ivCheck.setImageDrawable(null)
        }
    }
}