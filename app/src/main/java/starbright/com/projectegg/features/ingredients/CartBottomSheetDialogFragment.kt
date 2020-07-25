/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.ingredients

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.partial_bottom_sheet.*
import starbright.com.projectegg.R
import starbright.com.projectegg.data.model.Ingredient

private const val ITEM_WIDTH = 120F
class CartBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var adapter: IngredientsCartAdapter
    var cart: MutableList<Ingredient> = mutableListOf()
    var sheetListener: SheetListener? = null

    override fun onCreateView(@NonNull inflater: LayoutInflater,
                              @NonNull container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.partial_bottom_sheet, container, false)
    }

    override fun onViewCreated(@NonNull view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_submit.setOnClickListener {
            sheetListener?.submitButtonClicked()
        }
        setupRvIngredientCart()
        updateButtonView()
    }

    override fun onDetach() {
        super.onDetach()
        sheetListener = null
    }

    private fun setupRvIngredientCart() {
        activity?.let {
            adapter = IngredientsCartAdapter(it, cart).also {
                it.listener = { selectedPosition ->
                    updateView(selectedPosition)
                    sheetListener?.onItemRemovedFromCart()
                }
            }
        }

        rv_ingredients_cart.let {
            it.layoutManager = GridLayoutManager(activity, calculateColumns())
            it.adapter = adapter
        }

        tv_empty.visibility = if (cart.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun calculateColumns(): Int {
        val displayMetrics: DisplayMetrics = context?.resources?.displayMetrics ?: DisplayMetrics()
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / ITEM_WIDTH + 0.5).toInt()
    }

    private fun updateView(position: Int) {
        cart.removeAt(position)
        updateList()
        updateButtonView()
    }

    private fun updateList() {
        adapter.notifyDataSetChanged()
        tv_empty.visibility = if (cart.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun updateButtonView() {
        btn_submit.isEnabled = cart.isNotEmpty()
    }

    interface SheetListener {
        fun onItemRemovedFromCart()
        fun submitButtonClicked()
    }
}
