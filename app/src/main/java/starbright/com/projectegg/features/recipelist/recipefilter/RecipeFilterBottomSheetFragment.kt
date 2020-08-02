/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.recipelist.recipefilter

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.sheet_filter_recipe.*
import starbright.com.projectegg.R
import starbright.com.projectegg.view.BottomSheetHeader
import starbright.com.projectegg.view.FooterSubmitButton
import starbright.com.projectegg.view.HeaderWithChipsItem

class RecipeFilterBottomSheetFragment: BottomSheetDialogFragment() {

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val headerItemAdapter: ItemAdapter<BottomSheetHeader> by lazy {
        ItemAdapter<BottomSheetHeader>()
    }

    private val bodyItemAdapter: ItemAdapter<HeaderWithChipsItem> by lazy {
        ItemAdapter<HeaderWithChipsItem>()
    }

    private val footerItemAdapter: ItemAdapter<FooterSubmitButton> by lazy {
        ItemAdapter<FooterSubmitButton>()
    }

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
        val fastAdapter = FastAdapter.with(
            listOf(headerItemAdapter, bodyItemAdapter, footerItemAdapter)
        ).apply {
            addEventHook(FooterSubmitButton.ItemClickListener {
                onBottomSheetDismissListener?.invoke(selectedCuisine ?: "")
                dismiss()
            })
        }
        rv_filter.run {
            itemAnimator = DefaultItemAnimator()
            layoutManager = linearLayoutManager
            adapter = fastAdapter
        }
        headerItemAdapter.add(BottomSheetHeader(getString(R.string.recipelist_sortfilter_filter_title)))

        bodyItemAdapter.add(
            HeaderWithChipsItem(
                getString(R.string.recipelist_filter_sheet_cuisine_title),
                cuisines.mapIndexed { index, text ->
                    val themedContext = ContextThemeWrapper(
                        context, R.style.Widget_MaterialComponents_Chip_Filter
                    )
                    Chip(themedContext).also {
                        it.id = index
                        it.text = text
                        it.isCheckable = true
                        it.isChecked = text == selectedCuisine
                        it.setOnCheckedChangeListener { buttonView, isChecked ->
                            selectedCuisine = if (isChecked) {
                                buttonView.text.toString()
                            } else {
                                ""
                            }
                        }
                    }
                }
            )
        )
        footerItemAdapter.add(FooterSubmitButton(getString(R.string.recipelist_filter_sheet_submit)))
    }
}