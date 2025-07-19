/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.view

import starbright.com.projectegg.databinding.LayoutHomeHeaderHolderBinding
import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import starbright.com.projectegg.R

class RecipeHeader(private var title: String) : AbstractItem<RecipeHeader.ViewHolder>() {

    /** The layout for the given item */
    override val layoutRes: Int
        get() = R.layout.layout_home_header_holder

    /** The type of the Item. Can be a hardcoded INT, but preferred is a defined id */
    override val type: Int
        get() = R.id.recipeHeaderItem

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     *
     * @return the ViewHolder for this Item
     */
    override fun getViewHolder(v: View): ViewHolder = ViewHolder(LayoutHomeHeaderHolderBinding.bind(v))

    inner class ViewHolder(
        val binding: LayoutHomeHeaderHolderBinding
    ) : FastAdapter.ViewHolder<RecipeHeader>(binding.root) {
        override fun bindView(item: RecipeHeader, payloads: List<Any>) {
            binding.tvHeader.text = item.title
        }

        override fun unbindView(item: RecipeHeader) {
            binding.tvHeader.text = null
        }
    }
}