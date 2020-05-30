package starbright.com.projectegg.features.home.list

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.layout_home_header_holder.view.*
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
    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    inner class ViewHolder(itemVIew: View) : FastAdapter.ViewHolder<RecipeHeader>(itemVIew) {
        override fun bindView(item: RecipeHeader, payloads: List<Any>) {
            itemView.tv_header.text = item.title
        }

        override fun unbindView(item: RecipeHeader) {}
    }
}