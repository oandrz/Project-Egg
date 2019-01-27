/**
 * Created by Andreas on 20/1/2019.
 */

package starbright.com.projectegg.features.filter

import android.os.Bundle
import android.support.design.chip.Chip
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_filter.*
import starbright.com.projectegg.R


class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var dishTypes: List<String>
    private lateinit var cuisines: List<String>

    var listener: Listener? = null
    var selectedCuisine: String? = null
    var selectedDishType: String? = null

    private val dishTypeIds: MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dishTypes = resources
                .getStringArray(R.array.dishTypes)
                .toList()
        cuisines = resources
                .getStringArray(R.array.cuisines)
                .toList()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderDishTypesChips()
        renderCuisineChips()
        btn_confirm.setOnClickListener {
            dismissAllowingStateLoss()
            listener?.refresh(selectedDishType, selectedCuisine)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun renderDishTypesChips() {
        var counter = 1
        setupChipGroup()
        dishTypes.forEach { type ->
            val chip = Chip(activity).also {
                it.id = counter
                it.text = type
                it.isClickable = true
                it.checkedIcon = null
                it.isCheckable = true
                it.isChecked = type == selectedDishType
                it.setChipBackgroundColorResource(
                        if (it.isChecked) {
                            R.color.colorAccent
                        } else {
                            R.color.gray_300
                        }
                )
                it.setOnCheckedChangeListener { _, isSelected ->
                    if (isSelected) {
                        it.setChipBackgroundColorResource(R.color.colorAccent)
                    } else {
                        it.setChipBackgroundColorResource(R.color.gray_300)
                    }
                }
            }
            container_dish.addView(chip)
            dishTypeIds.add(counter)
            counter++
        }
    }

    private fun setupChipGroup() {
        container_dish.let {
            it.isSingleSelection = true
            it.setOnCheckedChangeListener { chipGroup, id ->
                val selectedChip: Chip? = chipGroup.findViewById(id)
                selectedDishType = selectedChip?.text?.toString() ?: ""
            }
        }
    }

    private fun setupCuisineChipGroup() {
        container_cuisine.let {
            it.isSingleSelection = true
            it.setOnCheckedChangeListener { chipGroup, id ->
                val selectedChip: Chip? = chipGroup.findViewById(id)
                selectedCuisine = selectedChip?.text?.toString() ?: ""
            }
        }
    }

    private fun renderCuisineChips() {
        var counter = 1
        setupCuisineChipGroup()
        cuisines.forEach { type ->
            val chip = Chip(activity).also {
                it.id = counter
                it.text = type
                it.isClickable = true
                it.checkedIcon = null
                it.isCheckable = true
                it.isChecked = type == selectedCuisine
                it.setChipBackgroundColorResource(
                        if (it.isChecked) {
                            R.color.colorAccent
                        } else {
                            R.color.gray_300
                        }
                )
                it.setOnCheckedChangeListener { _, isSelected ->
                    if (isSelected) {
                        it.setChipBackgroundColorResource(R.color.colorAccent)
                    } else {
                        it.setChipBackgroundColorResource(R.color.gray_300)
                    }
                }

            }
            container_cuisine.addView(chip)
            dishTypeIds.add(counter)
            counter++
        }
    }

    interface Listener {
        fun refresh(dishType: String?, cuisine: String?)
    }
}