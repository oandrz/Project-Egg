package starbright.com.projectegg.features.ingredients

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.partial_bottom_sheet.*
import starbright.com.projectegg.R
import starbright.com.projectegg.data.model.Ingredient

class CartBottomSheetDialogFragment : BottomSheetDialogFragment() {

    var cart: MutableList<Ingredient> = mutableListOf()
    var sheetListener: SheetListener? = null
    private lateinit var adapter: IngredientsCartAdapter

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
        updateCartCountView()
        setupRvIngredientCart()
        updateButtonView()
    }

    override fun onDetach() {
        super.onDetach()
        sheetListener = null
    }

    private fun setupRvIngredientCart() {
        adapter = IngredientsCartAdapter(activity!!, cart).also {
            it.listener = { selectedPosition ->
                updateView(selectedPosition)
                sheetListener?.updateCart(cart)
            }
        }

        rv_ingredients_cart.let {
            it.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = adapter
        }

        tv_empty.visibility = if (cart.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun updateView(position: Int) {
        cart.removeAt(position)
        updateCartCountView()
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

    private fun updateCartCountView() {
        tv_total_ingredient.text = cart.size.toString()
    }

    interface SheetListener {
        fun updateCart(ingredients: MutableList<Ingredient>)
        fun submitButtonClicked()
    }
}
