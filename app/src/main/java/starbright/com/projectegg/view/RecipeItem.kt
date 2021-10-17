/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.shape.CornerFamily
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import starbright.com.projectegg.R
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.databinding.LayoutHomeItemHolderBinding
import starbright.com.projectegg.util.GlideApp

class RecipeItem(val recipe: Recipe) : AbstractItem<RecipeItem.ViewHolder>() {

    private var binding: LayoutHomeItemHolderBinding? = null

    override fun createView(ctx: Context, parent: ViewGroup?): View {
        binding = LayoutHomeItemHolderBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return binding!!.root
    }

    /** The layout for the given item */
    override val layoutRes: Int
        get() = R.layout.layout_home_item_holder

    /** The type of the Item. Can be a hardcoded INT, but preferred is a defined id */
    override val type: Int
        get() = R.id.recipeBodyItem

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     *
     * @return the ViewHolder for this Item
     */
    override fun getViewHolder(v: View): ViewHolder = ViewHolder(binding!!)

    inner class ViewHolder(
        binding: LayoutHomeItemHolderBinding
    ) : FastAdapter.ViewHolder<RecipeItem>(binding.root) {

        /** Binds the data of this item onto the viewHolder */
        override fun bindView(item: RecipeItem, payloads: List<Any>) {
            binding?.apply {
                item.recipe.let {
                    val ivRadius = itemView.resources.getDimension(R.dimen.card_corner_radius_12dp)
                    ivBannerFood.shapeAppearanceModel = ivBannerFood.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, ivRadius)
                        .build()

                    tvSourceName.text = itemView.resources.getString(
                        R.string.home_list_format_by, it.sourceName
                    )
                    tvFoodName.text = it.title
                    tvServingTime.text = itemView.resources.getString(
                        R.string.home_list_format_time_shorten, it.cookingMinutes
                    )
                    tvServingCount.text = itemView.resources.getString(
                        R.string.detail_serving_format, it.servingCount
                    )
                    GlideApp.with(itemView.context)
                        .load(it.image)
                        .into(ivBannerFood)
                }
            }
        }

        /** View needs to release resources when its recycled */
        override fun unbindView(item: RecipeItem) {
            binding?.apply {
                tvSourceName.text = null
                tvFoodName.text = null
                tvServingTime.text = null
                tvServingCount.text = null
                ivBannerFood.setImageDrawable(null)
            }
        }
    }
}