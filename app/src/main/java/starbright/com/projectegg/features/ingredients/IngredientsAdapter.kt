package starbright.com.projectegg.features.ingredients

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.item_ingredient.view.*
import starbright.com.projectegg.R
import starbright.com.projectegg.data.local.model.Ingredient

//import starbright.com.projectegg.util.GlideApp;

internal class IngredientsAdapter(private val context: Context) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    private var dataSource: List<Ingredient>? = null
    private var listener: Listener? = null

    fun setIngredients(ingredients: List<Ingredient>) {
        dataSource = ingredients
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false)
        val viewHolder = IngredientViewHolder(view)
        view.setOnClickListener {
            dataSource?.get(viewHolder.adapterPosition)?.let {
                ingredient -> listener?.onSuggestionItemClicked(ingredient)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) =
        holder.bind(dataSource!![position])

    override fun getItemCount(): Int = dataSource!!.size

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    internal inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(ingredient: Ingredient) {
            itemView.tv_ingredient.text = ingredient.name
            //            GlideApp.with(context)
            //                    .load(ingredient.getImageUrl())
            //                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            //                    .centerCrop()
            //                    .into(ivIngredient);
        }
    }

    internal interface Listener {
        fun onSuggestionItemClicked(selectedItem: Ingredient)
    }
}