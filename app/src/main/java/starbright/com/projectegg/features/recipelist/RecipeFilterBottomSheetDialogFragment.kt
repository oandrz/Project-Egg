package starbright.com.projectegg.features.recipelist

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.sheet_filter_recipe.*
import starbright.com.projectegg.R

class RecipeFilterBottomSheetDialogFragment: BottomSheetDialogFragment() {

    var selectedCuisine: String? = null
    var cuisines: List<String> = listOf()
    var onBottomSheetDismissListener: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sheet_filter_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newContext = ContextThemeWrapper(activity, R.style.Widget_MaterialComponents_Chip_Filter)
        var counter = 1
        cuisines.forEach { cuisine ->
            val chip = Chip(newContext).also {
                it.id = counter
                it.text = cuisine
                it.isCheckable = true
                it.isChecked = cuisine == selectedCuisine
            }
            group_filter.addView(chip)
            counter++
        }

        group_filter.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId > View.NO_ID) {
                onBottomSheetDismissListener?.invoke(cuisines[checkedId - 1])
            } else {
                onBottomSheetDismissListener?.invoke("")
            }
            dismiss()
        }
    }
}