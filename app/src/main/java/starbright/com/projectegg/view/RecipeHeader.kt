/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import starbright.com.projectegg.R
import starbright.com.projectegg.databinding.LayoutHomeHeaderHolderBinding

class RecipeHeader(private var title: String) : AbstractItem<RecipeHeader.ViewHolder>() {

    private lateinit var binding: LayoutHomeHeaderHolderBinding

    override fun createView(ctx: Context, parent: ViewGroup?): View {
        binding = LayoutHomeHeaderHolderBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return binding.root
    }

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
    override fun getViewHolder(v: View): ViewHolder = ViewHolder(binding)

    inner class ViewHolder(private val binding: LayoutHomeHeaderHolderBinding) : FastAdapter.ViewHolder<RecipeHeader>(binding.root) {
        override fun bindView(item: RecipeHeader, payloads: List<Any>) {
            binding.tvHeader.text = item.title
        }

        override fun unbindView(item: RecipeHeader) {
            binding.tvHeader.text = null
        }
    }
}