/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.ingredients

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_cart.view.*
import starbright.com.projectegg.R
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.util.GlideApp

class IngredientsCartAdapter(
    private val context: Context,
    private val ingredientCart: List<Ingredient>
) : RecyclerView.Adapter<IngredientsCartAdapter.ViewHolder>() {

    var listener: ((Int) -> (Unit))? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view).also { holder ->
            holder.itemView.iv_clear?.setOnClickListener {
                listener?.invoke(holder.adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(ingredientCart[position])

    override fun getItemCount(): Int = ingredientCart.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(ingredient: Ingredient) {
            itemView.tv_ingredient_name.text = ingredient.name
            GlideApp.with(context)
                .load(
                    if (ingredient.imageUrl?.isNotEmpty() == true) {
                        ingredient.imageUrl
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.ic_room_service)
                    }
                )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.circleCropTransform())
                .into(itemView.iv_ingredient)
        }
    }
}
