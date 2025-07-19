/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 31 - 7 - 2020.
 */


package starbright.com.projectegg.features.detail

import starbright.com.projectegg.databinding.ActivityRecipeDetailRevampedBinding
import starbright.com.projectegg.databinding.ContentRecipeDetailBodyBinding
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import android.view.WindowManager
import android.os.Build
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Instruction
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.features.base.NormalToolbar
import starbright.com.projectegg.features.base.UNKNOWN_RESOURCE
import starbright.com.projectegg.features.base.WebviewActivity
import starbright.com.projectegg.features.ingredients.IngredientsAdapter
import starbright.com.projectegg.util.GlideApp
import starbright.com.projectegg.util.TextViewRecyclerAdapter
import java.lang.ref.WeakReference
import android.view.ViewGroup

class RecipeDetailActivity : BaseActivity<RecipeDetailContract.View, RecipeDetailPresenter>(),
    RecipeDetailContract.View {

    private lateinit var binding: ActivityRecipeDetailRevampedBinding
    private lateinit var contentBinding: ContentRecipeDetailBodyBinding
    private val scrollContainer: View by lazy {
        contentBinding.root.findViewById(R.id.scroll_container)
    }
    private val layoutEmpty: View by lazy {
        binding.layoutEmpty.root
    }

    private val recipeId: String by lazy {
        intent?.extras?.getString(RECIPE_ID_EXTRA_KEY) ?: ""
    }

    private var isBookmarked: Boolean = false

    override fun getLayoutRes(): Int = R.layout.activity_recipe_detail_revamped

    override fun getView(): RecipeDetailContract.View = this

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        // Make status bar translucent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        setToolbarBehavior(
            NormalToolbar(
                WeakReference(this), R.id.toolbar, UNKNOWN_RESOURCE
            )
        )
        super.onCreate(savedInstanceState)
        
        presenter.getRecipeDetailInformation(recipeId)
    }
    
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        // Initialize bindings after setContentView
        binding = ActivityRecipeDetailRevampedBinding.bind((findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0))
        contentBinding = ContentRecipeDetailBodyBinding.bind(binding.root.findViewById(R.id.swipe_refresh_container))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_detail_menu, menu)
        menu?.findItem(R.id.menu_favorite)?.apply {
            if (isBookmarked) {
                setTitle(R.string.detail_menu_unfavourite_label)
            } else {
                setTitle(R.string.detail_menu_favourite_label)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_share -> {
                presenter.handleShareMenuClicked()
                true
            }
            R.id.menu_webview -> {
                presenter.handleWebViewMenuClicked()
                true
            }
            R.id.menu_favorite -> {
                presenter.handleBookmarkRecipeMenuClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showProgressBar() {
        contentBinding.swipeRefreshContainer.isRefreshing = true
    }

    override fun hideProgressBar() {
        contentBinding.swipeRefreshContainer.isRefreshing = false
    }

    override fun hideScrollContainer() {
        scrollContainer.visibility = View.GONE
    }

    override fun showScrollContainer() {
        scrollContainer.visibility = View.VISIBLE
    }

    override fun hideEmptyView() {
        layoutEmpty.visibility = View.GONE
    }

    override fun renderErrorView(errorMessage: String) {
        layoutEmpty.visibility = View.VISIBLE
    }

    override fun renderEmptyView() {
        layoutEmpty.visibility = View.VISIBLE
    }

    override fun renderBannerFoodImage(imageURL: String) {
        binding.imgBannerFood.visibility = View.VISIBLE
        GlideApp.with(this)
            .load(imageURL)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(binding.imgBannerFood)
    }

    override fun renderHeaderContainer(
        serving: Int,
        cookingMinutes: Int,
        recipeName: String,
        dishType: String,
        calories: Int
    ) {
        contentBinding.tvCalories.text = getString(R.string.recipe_list_calories_title, calories)
        contentBinding.tvDish.text = dishType
        contentBinding.tvRecipeTitle.text = recipeName
        contentBinding.tvPlater.text = getString(R.string.detail_serving_format, serving)
        contentBinding.tvCookTime.text = getString(R.string.detail_time_format, cookingMinutes)
    }

    override fun renderIngredientsList(ingredients: MutableList<Ingredient>) {
        val adapter = IngredientsAdapter(this)
        adapter.setIngredients(ingredients)
        contentBinding.rvIngredient.let {
            it.isNestedScrollingEnabled = false
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
        contentBinding.rvInstruction.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(
                this@RecipeDetailActivity,
                LinearLayoutManager.VERTICAL, false
            )
            this.adapter = adapter
        }
    }

    override fun setupSwipeRefreshLayout() {
        contentBinding.swipeRefreshContainer.let {
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

    override fun showSnackbar(text: Int) {
        Snackbar.make(binding.rootLayout, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun updateMenu(bookmarked: Boolean) {
        isBookmarked = bookmarked
        invalidateOptionsMenu()
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