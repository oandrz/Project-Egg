/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 21 - 8 - 2020.
 */

package starbright.com.projectegg.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import starbright.com.projectegg.R
import starbright.com.projectegg.databinding.ItemSingleTextBinding
import kotlin.coroutines.coroutineContext

class RecentSearchItem(
    val text: String,
    val listener: ((String, Int) -> Unit)?
) : AbstractItem<RecentSearchItem.ViewHolder>() {

    private lateinit var binding: ItemSingleTextBinding

    /** The layout for the given item */
    override val layoutRes: Int
        get() = R.layout.item_single_text

    /** The type of the Item. Can be a hardcoded INT, but preferred is a defined id */
    override val type: Int
        get() = R.id.singleTextItem

    override fun createView(ctx: Context, parent: ViewGroup?): View {
        binding = ItemSingleTextBinding.inflate(LayoutInflater.from(ctx), parent, false)

        return binding.root
    }

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     *
     * @return the ViewHolder for this Item
     */
    override fun getViewHolder(v: View): ViewHolder = ViewHolder(binding)

    inner class ViewHolder(
        private val binding: ItemSingleTextBinding
    ) : FastAdapter.ViewHolder<RecentSearchItem>(binding.root) {

        override fun bindView(item: RecentSearchItem, payloads: List<Any>) {
            binding.tvText.text = item.text
            binding.ivClear.setOnClickListener {
                listener?.invoke(item.text, bindingAdapterPosition)
            }
        }

        /** View needs to release resources when its recycled */
        override fun unbindView(item: RecentSearchItem) {
            binding.tvText.text = null
            binding.ivClear.setOnClickListener(null)
        }
    }
}