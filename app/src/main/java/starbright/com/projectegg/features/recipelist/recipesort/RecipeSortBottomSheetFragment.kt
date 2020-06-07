package starbright.com.projectegg.features.recipelist.recipesort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.sheet_sort_recipe.*
import starbright.com.projectegg.R
import starbright.com.projectegg.data.model.SortOption
import starbright.com.projectegg.view.RecipeHeader
import starbright.com.projectegg.view.SelectorItem

class RecipeSortBottomSheetFragment: BottomSheetDialogFragment() {

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val headerItemAdapter: ItemAdapter<RecipeHeader> by lazy {
        ItemAdapter<RecipeHeader>()
    }

    private val sortItemAdapter: ItemAdapter<SelectorItem> by lazy {
        ItemAdapter<SelectorItem>()
    }

    private lateinit var sortOptions: ArrayList<SortOption>
    private var selectedSortOption: String? = null
    var listener: ((String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            selectedSortOption = getString(SELECTED_SORT_OPTION_BUNDLE)
            sortOptions = getParcelableArrayList(SORT_OPTION_BUNDLE) ?: arrayListOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sheet_sort_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fastAdapter = FastAdapter.with(listOf(headerItemAdapter, sortItemAdapter)).apply {
            onClickListener =  { view, _, item, _ ->
                if (view != null && item is SelectorItem) {
                    listener?.invoke(item.text)
                    dismiss()
                }
                false
            }
        }
        rv_sort.run {
            itemAnimator = DefaultItemAnimator()
            layoutManager = linearLayoutManager
            adapter = fastAdapter
        }
        headerItemAdapter.add(RecipeHeader(getString(R.string.recipelist_sort_sheet_title)))
        sortOptions.map {
            sortItemAdapter.add(
                SelectorItem(it.id, it.imageUrl, it.id.equals(selectedSortOption, true))
            )
        }
    }

    companion object {

        private const val SELECTED_SORT_OPTION_BUNDLE = "SELECTED_SORT_OPTION_BUNDLE"
        private const val SORT_OPTION_BUNDLE = "SORT_OPTION_BUNDLE"

        fun newInstance(
            dataSource: ArrayList<SortOption>, selectedSortOption: String? = null
        ) : RecipeSortBottomSheetFragment {
            return RecipeSortBottomSheetFragment().apply {
                arguments = Bundle().also {
                    it.putString(SELECTED_SORT_OPTION_BUNDLE, selectedSortOption)
                    it.putParcelableArrayList(SORT_OPTION_BUNDLE, dataSource)
                }
            }
        }
    }
}