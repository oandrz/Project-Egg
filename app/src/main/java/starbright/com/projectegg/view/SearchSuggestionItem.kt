/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 17 - 8 - 2020.
 */

package starbright.com.projectegg.view

import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import starbright.com.projectegg.R
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.util.GlideApp

class SearchSuggestionItem(val recipe: Recipe) : AbstractItem<SearchSuggestionItem.ViewHolder>() {

    /** The layout for the given item */
    override val layoutRes: Int
        get() = R.layout.item_single_with_image

    /** The type of the Item. Can be a hardcoded INT, but preferred is a defined id */
    override val type: Int
        get() = R.id.searchSuggestionItem

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     *
     * @return the ViewHolder for this Item
     */
    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    inner class ViewHolder(
        itemView: View
    ) : FastAdapter.ViewHolder<SearchSuggestionItem>(itemView) {

        /** Binds the data of this item onto the viewHolder */
        override fun bindView(item: SearchSuggestionItem, payloads: List<Any>) {
            itemView.apply {
//                tv_ingredient.text = item.recipe.title
//                GlideApp.with(context)
//                    .load(item.recipe.image)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .centerCrop()
//                    .into(iv_ingredient)
            }
        }

        /** View needs to release resources when its recycled */
        override fun unbindView(item: SearchSuggestionItem) {
            itemView.apply {
//                tv_ingredient.text = null
//                iv_ingredient.setImageDrawable(null)
            }
        }
    }
}