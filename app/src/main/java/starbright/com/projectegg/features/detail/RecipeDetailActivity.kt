/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.ads.AdRequest
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_recipe_detail_revamped.*
import kotlinx.android.synthetic.main.content_recipe_detail_body.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Instruction
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.features.base.NormalToolbar
import starbright.com.projectegg.features.base.UNKNOWN_RESOURCE
import starbright.com.projectegg.features.base.WebviewActivity
import starbright.com.projectegg.util.GlideApp
import starbright.com.projectegg.util.TextViewRecyclerAdapter
import java.lang.ref.WeakReference

class RecipeDetailActivity : BaseActivity<RecipeDetailContract.View, RecipeDetailPresenter>(),
    RecipeDetailContract.View {

    private val recipeId: String by lazy {
        intent?.extras?.getString(RECIPE_ID_EXTRA_KEY) ?: ""
    }

    override fun getLayoutRes(): Int = R.layout.activity_recipe_detail_revamped

    override fun getView(): RecipeDetailContract.View = this

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null)
        setToolbarBehavior(
            NormalToolbar(
                WeakReference(this), R.id.toolbar, UNKNOWN_RESOURCE
            )
        )
        super.onCreate(savedInstanceState)
        presenter.getRecipeDetailInformation(recipeId)
        adView.loadAd(AdRequest.Builder().build())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_share -> {
                presenter.handleShareMenuClicked()
                true
            }
            R.id.menu_webview -> {
                presenter.handleWebViewMenuClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

    override fun hideEmptyView() {
        layout_empty.visibility = View.GONE
    }

    override fun renderErrorView(errorMessage: String) {
        layout_empty.visibility = View.VISIBLE
    }

    override fun renderEmptyView() {
        layout_empty.visibility = View.VISIBLE
    }

    override fun renderBannerFoodImage(imageURL: String) {
        img_banner_food.visibility = View.VISIBLE
        GlideApp.with(this)
            .load(imageURL)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(img_banner_food)
    }

    override fun renderHeaderContainer(
        serving: Int,
        cookingMinutes: Int,
        recipeName: String,
        dishType: String,
        calories: Int
    ) {
        tv_calories.text = getString(R.string.recipe_list_calories_title, calories)
        tv_dish.text = dishType
        tv_recipe_title.text = recipeName
        tv_plater.text = getString(R.string.detail_serving_format, serving)
        tv_cook_time.text = getString(R.string.detail_time_format, cookingMinutes)
    }

    override fun renderIngredientsList(ingredients: MutableList<Ingredient>) {
        val adapter = TextViewRecyclerAdapter(
            this,
            ingredients.map { it.name }
        )
        rv_ingredient.let {
            it.isNestedScrollingEnabled = false
            it.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL, false
            )
            it.adapter = adapter
        }
    }

    override fun renderInstructionsList(instructions: MutableList<Instruction>) {
        val formattedInstructions = ArrayList<String>(instructions.size)
        for (instruction in instructions) {
            formattedInstructions.add(
                getString(
                    R.string.general_number_text_format,
                    instruction.number, instruction.step
                )
            )
        }
        val adapter = TextViewRecyclerAdapter(
            this,
            formattedInstructions
        )
        rv_instruction?.let {
            it.isNestedScrollingEnabled = false
            it.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL, false
            )
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
        startActivity(WebviewActivity.newIntent(this, url))
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