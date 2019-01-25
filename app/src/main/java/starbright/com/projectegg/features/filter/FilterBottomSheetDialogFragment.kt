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
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_filter.*
import starbright.com.projectegg.R


class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var dishTypes: List<String>

    var listener: Listener? = null

    private val dishTypeIds: MutableList<Int> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dishTypes = resources
                .getStringArray(R.array.dishTypes)
                .toList()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderChipView()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun renderChipView() {
        var counter = 1
        setupChipGroup()
        dishTypes.forEach { type ->
            val chip = Chip(activity).also {
                it.id = counter
                it.text = type
                it.isClickable = true
                it.checkedIcon = null
                it.isCheckable = true
                it.setChipBackgroundColorResource(R.color.gray_300)
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
                selectedChip?.let {
                    Toast.makeText(activity, selectedChip.text, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    interface Listener {

    }
}