package starbright.com.projectegg.features.home.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import kotlinx.android.synthetic.main.layout_home_item_holder.view.*
import starbright.com.projectegg.R
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.util.GlideApp

class RecipeHomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataSource: MutableList<Recipe> = mutableListOf()

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_HEADER -> HomeHeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_home_header_holder, parent, false)
            )
            else -> HomeViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_home_item_holder, parent, false)
            )
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return dataSource.size + 1
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HomeViewHolder) {
            holder.bindView(dataSource[position - 1])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_HEADER
        } else {
            TYPE_BODY
        }
    }

    fun add(recipe: Recipe) {
        insert(recipe, dataSource.size)
    }

    private fun insert(recipe: Recipe, position: Int) {
        dataSource.add(position, recipe)
        notifyItemInserted(position)
    }

    fun remove(position: Int) {
        dataSource.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeAll() {
        val size: Int = dataSource.size
        dataSource.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun addAll(recipes: List<Recipe>) {
        val startIndex: Int = dataSource.size
        dataSource.addAll(startIndex, recipes)
        notifyItemRangeInserted(startIndex, recipes.size)
    }

    fun refresh(recipes: List<Recipe>) {
        removeAll()
        addAll(recipes)
    }

    private class HomeViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindView(recipe: Recipe) {
            itemView.apply {
                val ivRadius = resources.getDimension(R.dimen.card_corner_radius_12dp)
                iv_banner_food.shapeAppearanceModel = itemView.iv_banner_food.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, ivRadius)
                    .build()

                tv_source_name.text = recipe.sourceName
                tv_food_name.text = recipe.title
                tv_serving_time.text = resources.getString(
                    R.string.home_list_format_time_shorten, recipe.cookingMinutes
                )
                tv_serving_count.text = resources.getString(
                    R.string.detail_serving_format, recipe.servingCount
                )
                GlideApp.with(context)
                    .load(recipe.image)
                    .into(iv_banner_food)
            }
        }
    }

    private class HomeHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val TYPE_HEADER = 0;
        private const val TYPE_BODY = 1;
    }
}