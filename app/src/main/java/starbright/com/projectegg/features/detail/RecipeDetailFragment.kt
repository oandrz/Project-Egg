/**
 * Created by Andreas on 5/10/2019.
 */

/**
 * Created by Andreas on 11/8/2019.
 */

package starbright.com.projectegg.features.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_recipe_detail.*
import starbright.com.projectegg.MyApp
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.local.model.Ingredient
import starbright.com.projectegg.data.local.model.Instruction
import starbright.com.projectegg.util.GlideApp
import starbright.com.projectegg.util.TextViewRecyclerAdapter
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider
import java.util.*
import javax.inject.Inject

class RecipeDetailFragment : Fragment(), RecipeDetailContract.View {

    @Inject
    lateinit var schedulerProvider: BaseSchedulerProvider
    @Inject
    lateinit var repository: AppRepository

    private lateinit var mPresenter: RecipeDetailContract.Presenter
    private var mFragmentListener: FragmentListener? = null

    override fun onAttach(context: Context?) {
        MyApp.getAppComponent().inject(this)
        super.onAttach(context)
        mFragmentListener = context as FragmentListener?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        RecipeDetailPresenter(repository, this, schedulerProvider)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.apply {
            setRecipeId(arguments!!.getString(BUNDLE_RECIPE_ID)!!)
            start()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipe_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_share -> {
                mPresenter.handleShareMenuClicked()
                return true
            }
            R.id.menu_webview -> {
                mPresenter.handleWebViewMenuClicked()
                return true
            }
        }
        return false
    }

    override fun setPresenter(presenter: RecipeDetailPresenter) {
        mPresenter = presenter
    }

    override fun showProgressBar() {
        swipe_refresh_container.isRefreshing = true
    }

    override fun hideProgressBar() {
        swipe_refresh_container.isRefreshing = false
    }

    override fun hideScrollContainer() {
        scroll_container.visibility = View.GONE
    }

    override fun showScrollContainer() {
        scroll_container.visibility = View.VISIBLE
    }

    override fun hideEmptyStateTextView() {
        tv_empty_text.visibility = View.GONE
    }

    override fun renderErrorStateTextView(errorMessage: String) {
        tv_empty_text.run {
            visibility = View.VISIBLE
            text = errorMessage
        }
    }

    override fun renderEmptyStateTextView() {
        tv_empty_text.run {
            visibility = View.VISIBLE
            text = getString(R.string.detail_empty_label)
        }
    }

    override fun renderBannerFoodImage(imageURL: String) {
        img_banner_food.visibility = View.VISIBLE
        GlideApp.with(activity!!)
                .load(imageURL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(img_banner_food)
    }

    override fun renderHeaderContainer(serving: Int, preparationMinutes: Int, cookingMinutes: Int, recipeName: String) {
        container_header.visibility = View.VISIBLE
        tv_recipe_title.text = recipeName
        tv_serving_count.text = getString(R.string.detail_serving_format, serving)
        tv_preparation_time.text = getString(R.string.detail_time_format, preparationMinutes)
        tv_cooking_time.text = getString(R.string.detail_time_format, cookingMinutes)
    }

    override fun renderIngredientCard(ingredients: MutableList<Ingredient>) {
        val formattedInstructions = ArrayList<String>(ingredients.size)
        var number = 1
        for (ingredient in ingredients) {
            formattedInstructions.add(getString(R.string.general_number_text_unit_format,
                    number, ingredient.name, ingredient.amount, ingredient.unit))
            number++
        }
        card_ingredient.visibility = View.VISIBLE
        val adapter = TextViewRecyclerAdapter(activity,
                formattedInstructions)
        rv_ingredient.run {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(activity,
                    LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }
    }

    override fun renderInstructionCard(instructions: MutableList<Instruction>) {
        val formattedInstructions = ArrayList<String>(instructions.size)
        for (instruction in instructions) {
            formattedInstructions.add(getString(R.string.general_number_text_format,
                    instruction.number, instruction.step))
        }
        card_instruction.visibility = View.VISIBLE
        val adapter = TextViewRecyclerAdapter(activity,
                formattedInstructions)
        rv_instruction?.run {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(activity,
                    LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }
    }

    override fun setupSwipeRefreshLayout() {
        swipe_refresh_container.run {
            setColorSchemeColors(ContextCompat.getColor(activity!!, R.color.red))
            setOnRefreshListener { mPresenter.getRecipeDetailInformation(arguments!!.getString(BUNDLE_RECIPE_ID)!!) }
        }
    }

    override fun createShareIntent(url: String, recipeName: String) {
        val textToShare = getString(R.string.detail_intent_share, recipeName, url)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.general_sharechooser)))
    }

    override fun navigateToWebViewActivity(url: String) {
        mFragmentListener?.navigateToWebViewActivity(url)
    }

    internal interface FragmentListener {
        fun navigateToWebViewActivity(url: String)
    }

    companion object {

        private const val BUNDLE_RECIPE_ID = "BUNDLE_RECIPE_ID"

        fun newInstance(recipeId: String): RecipeDetailFragment {
            val args = Bundle()
            args.putString(BUNDLE_RECIPE_ID, recipeId)
            val fragment = RecipeDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
