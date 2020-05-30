package starbright.com.projectegg.features.home.list

import android.view.View
import com.google.android.material.shape.CornerFamily
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.layout_home_item_holder.view.*
import starbright.com.projectegg.R
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.util.GlideApp

class RecipeItem(val recipe: Recipe) : AbstractItem<RecipeItem.ViewHolder>() {

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
    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    inner class ViewHolder(
        itemView: View
    ) : FastAdapter.ViewHolder<RecipeItem>(itemView) {

        /** Binds the data of this item onto the viewHolder */
        override fun bindView(item: RecipeItem, payloads: List<Any>) {
            itemView.apply {
                item.recipe.let {
                    val ivRadius = resources.getDimension(R.dimen.card_corner_radius_12dp)
                    iv_banner_food.shapeAppearanceModel = itemView.iv_banner_food.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, ivRadius)
                        .build()

                    tv_source_name.text = resources.getString(
                        R.string.home_list_format_by, it.sourceName
                    )
                    tv_food_name.text = it.title
                    tv_serving_time.text = resources.getString(
                        R.string.home_list_format_time_shorten, it.cookingMinutes
                    )
                    tv_serving_count.text = resources.getString(
                        R.string.detail_serving_format, it.servingCount
                    )
                    GlideApp.with(context)
                        .load(it.image)
                        .into(iv_banner_food)
                }
            }
        }

        /** View needs to release resources when its recycled */
        override fun unbindView(item: RecipeItem) {
            itemView.apply {
                tv_source_name.text = null
                tv_food_name.text = null
                tv_serving_time.text = null
                tv_serving_count.text = null
                iv_banner_food.setImageDrawable(null)
            }
        }
    }
}