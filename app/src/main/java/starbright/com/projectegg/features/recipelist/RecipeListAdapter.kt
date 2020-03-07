/**
 * Created by Andreas on 11/8/2019.
 */

package starbright.com.projectegg.features.recipelist

import android.R.attr.data
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_recipe.view.*
import starbright.com.projectegg.R
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.util.GlideApp
import java.util.*


/**
 * Created by Andreas on 4/15/2018.
 */

internal class RecipeListAdapter : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {
    private val mRecipes: MutableList<Recipe> = mutableListOf()

    var mListener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        val holder = ViewHolder(view, parent.context)
        view.setOnClickListener { mListener?.onItemClicked(holder.adapterPosition) }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(mRecipes[position])
    }

    override fun getItemCount(): Int = mRecipes.size

    fun add(recipe: Recipe) {
        insert(recipe, mRecipes.size)
    }

    fun insert(recipe: Recipe, position: Int) {
        mRecipes.add(position, recipe)
        notifyItemInserted(position)
    }

    fun remove(position: Int) {
        mRecipes.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        val size: Int = mRecipes.size
        mRecipes.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun addAll(recipes: List<Recipe>) {
        val startIndex: Int = mRecipes.size
        mRecipes.addAll(startIndex, recipes)
        notifyItemRangeInserted(startIndex, recipes.size)
    }

    inner class ViewHolder(
        itemView: View,
        private val context: Context
    ) : RecyclerView.ViewHolder(itemView) {

        fun bindView(recipe: Recipe) {
            itemView.tv_recipe_name.text = recipe.title
            GlideApp.with(context)
                .load(recipe.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(itemView.img_thumbnail)
        }
    }

    internal interface Listener {
        fun onItemClicked(position: Int)
    }
}