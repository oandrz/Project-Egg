/**
 * Created by Andreas on 11/8/2019.
 */

package starbright.com.projectegg.features.recipelist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_recipe.view.*
import starbright.com.projectegg.R
import starbright.com.projectegg.data.local.model.Recipe
import starbright.com.projectegg.util.GlideApp
import java.util.*

/**
 * Created by Andreas on 4/15/2018.
 */

internal class RecipeListAdapter(private val mContext: Context) :
        RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {
    private val mRecipes: MutableList<Recipe>
    var mListener: Listener? = null

    init {
        mRecipes = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_recipe, parent, false)
        val holder = ViewHolder(view)
        view.setOnClickListener { mListener!!.onItemClicked(holder.adapterPosition) }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(mRecipes[position])
    }

    override fun getItemCount(): Int = mRecipes.size

    fun setRecipes(recipes: List<Recipe>) {
        mRecipes.clear()
        mRecipes.addAll(recipes)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(recipe: Recipe) {
            itemView.tv_recipe_name.text = recipe.title
            GlideApp.with(mContext)
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