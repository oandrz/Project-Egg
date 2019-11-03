/**
 * Created by Andreas on 5/10/2019.
 */

/**
 * Created by Andreas on 10/9/2018.
 */

package starbright.com.projectegg.features.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_recipe_detail.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Instruction
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.features.base.NormalToolbar
import starbright.com.projectegg.util.GlideApp
import starbright.com.projectegg.util.TextViewRecyclerAdapter
import java.lang.ref.WeakReference

class RecipeDetailActivity : BaseActivity<RecipeDetailContract.View, RecipeDetailPresenter>(), RecipeDetailContract.View {

    private val recipeId: String by lazy {
        intent?.extras?.getString(RECIPE_ID_EXTRA_KEY) ?: ""
    }

    override fun getLayoutRes(): Int = R.layout.fragment_recipe_detail

    override fun getView(): RecipeDetailContract.View = this

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setToolbarBehavior(NormalToolbar(
            WeakReference(this), R.id.toolbar, R.string.detail_title
        ))
        super.onCreate(savedInstanceState)
        presenter.getRecipeDetailInformation(recipeId)
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
        GlideApp.with(this)
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
        val adapter = TextViewRecyclerAdapter(this,
            formattedInstructions)
        rv_ingredient.let {
            it.isNestedScrollingEnabled = false
            it.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }
    }

    override fun renderInstructionCard(instructions: MutableList<Instruction>) {
        val formattedInstructions = ArrayList<String>(instructions.size)
        for (instruction in instructions) {
            formattedInstructions.add(getString(R.string.general_number_text_format,
                instruction.number, instruction.step))
        }
        card_instruction.visibility = View.VISIBLE
        val adapter = TextViewRecyclerAdapter(this,
            formattedInstructions)
        rv_instruction?.let {
            it.isNestedScrollingEnabled = false
            it.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }
    }

    override fun setupSwipeRefreshLayout() {
        swipe_refresh_container.let {
            it.setColorSchemeColors(ContextCompat.getColor(this, R.color.red))
            it.setOnRefreshListener {
                presenter.getRecipeDetailInformation(recipeId)
            }
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

    }

    companion object {
        private const val RECIPE_ID_EXTRA_KEY = "RECIPE_ID_EXTRA_KEY"

        fun getIntent(context: Context, recipeId: String): Intent {
            return Intent(context, RecipeDetailActivity::class.java).also {
                it.putExtra(RECIPE_ID_EXTRA_KEY, recipeId)
            }
        }
    }
}