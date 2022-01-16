/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 31 - 7 - 2020.
 */


package starbright.com.projectegg.features.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Instruction
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.databinding.ActivityRecipeDetailRevampedBinding
import starbright.com.projectegg.features.base.*
import starbright.com.projectegg.util.GlideApp
import starbright.com.projectegg.util.TextViewRecyclerAdapter
import java.lang.ref.WeakReference
import javax.inject.Inject

class RecipeDetailActivity : BaseActivityRevamped() {

    private val binding: ActivityRecipeDetailRevampedBinding by lazy {
        ActivityRecipeDetailRevampedBinding.inflate(layoutInflater)
    }

    private val recipeId: String by lazy {
        intent?.extras?.getString(RECIPE_ID_EXTRA_KEY) ?: ""
    }

    private var isBookmarked: Boolean = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: RecipeDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[RecipeDetailViewModel::class.java]
    }

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setToolbarBehavior(
            NormalToolbar(
                WeakReference(this), R.id.toolbar, UNKNOWN_RESOURCE
            )
        )
        super.onCreate(savedInstanceState)
        setupView()
        setupObserver()
        viewModel.getRecipeDetailInformation(recipeId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.recipe_detail_menu, menu)
        menu.findItem(R.id.menu_favorite)?.apply {
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
                viewModel.handleShareMenuClicked()
                true
            }
            R.id.menu_webview -> {
                viewModel.handleWebViewMenuClicked()
                true
            }
            R.id.menu_favorite -> {
                viewModel.handleBookmarkRecipeMenuClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupView() {
        setupSwipeRefreshLayout()
    }

    private fun setupObserver() {
        viewModel.state.observe(this) {
            when(it) {
                is RecipeDetailState.ShowLoading -> {
                    hideScrollContainer()
                    showProgressBar()
                }
                is RecipeDetailState.Success -> {
                    hideProgressBar()
                    hideEmptyView()
                    render(it.recipe)
                }
                is RecipeDetailState.Error -> {
                    renderErrorView(getString(it.res))
                }
                is RecipeDetailState.Empty -> {
                    hideProgressBar()
                    renderEmptyView()
                }
                is RecipeDetailState.NavigateShareIntent -> {
                    createShareIntent(it.url, it.title)
                }
                is RecipeDetailState.NavigateWebView -> {
                    navigateToWebViewActivity(it.url)
                }
                is RecipeDetailState.BookmarkUpdate -> {
                    if (it.showSnackBar) {
                        showSnackbar(R.string.detail_message_favourite_add)
                    }
                    updateMenu(it.isBookMarked)
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.recipeBody.swipeRefreshContainer.isRefreshing = true
    }

    private fun hideProgressBar() {
        binding.recipeBody.swipeRefreshContainer.isRefreshing = false
    }

    private fun hideScrollContainer() {
        binding.recipeBody.scrollContainer.visibility = View.GONE
    }

    private fun showScrollContainer() {
        binding.recipeBody.scrollContainer.visibility = View.VISIBLE
    }

    private fun hideEmptyView() {
        binding.layoutEmpty.root.visibility = View.GONE
    }

    private fun renderErrorView(errorMessage: String) {
        binding.layoutEmpty.root.visibility = View.VISIBLE
        showSnackbar(text = errorMessage)
    }

    private fun renderEmptyView() {
        binding.layoutEmpty.root.visibility = View.VISIBLE
    }

    private fun renderBannerFoodImage(imageURL: String) {
        binding.imgBannerFood.visibility = View.VISIBLE
        GlideApp.with(this)
            .load(imageURL)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(binding.imgBannerFood)
    }

    private fun renderHeaderContainer(
        serving: Int,
        cookingMinutes: Int,
        recipeName: String,
        dishType: String,
        calories: Int
    ) {
        binding.recipeBody.apply {
            tvCalories.text = getString(R.string.recipe_list_calories_title, calories)
            tvDish.text = dishType
            tvRecipeTitle.text = recipeName
            tvPlater.text = getString(R.string.detail_serving_format, serving)
            tvCookTime.text = getString(R.string.detail_time_format, cookingMinutes)
        }
    }

    private fun renderIngredientsList(ingredients: List<Ingredient>) {
        val textRecycleAdapter = TextViewRecyclerAdapter(ingredients.map { it.name })
        binding.recipeBody.rvIngredient.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@RecipeDetailActivity,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = textRecycleAdapter
        }
    }

    private fun renderInstructionsList(instructions: List<Instruction>) {
        val formattedInstructions = ArrayList<String>(instructions.size)
        for (instruction in instructions) {
            formattedInstructions.add(
                getString(
                    R.string.general_number_text_format,
                    instruction.number, instruction.step
                )
            )
        }
        val recyclerAdapter = TextViewRecyclerAdapter(formattedInstructions)
        binding.recipeBody.rvInstruction.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(
                this@RecipeDetailActivity,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = recyclerAdapter
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.recipeBody.swipeRefreshContainer.run {
            setColorSchemeColors(
                ContextCompat.getColor(this@RecipeDetailActivity, R.color.red)
            )
            setOnRefreshListener {
                viewModel.getRecipeDetailInformation(recipeId)
            }
        }
    }

    private fun createShareIntent(url: String, recipeName: String) {
        val textToShare = getString(R.string.detail_intent_share, recipeName, url)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.general_sharechooser)))
    }

    private fun navigateToWebViewActivity(url: String) {
        startActivity(WebviewActivity.newIntent(this, url))
    }

    private fun showSnackbar(textRes: Int? = null, text: String? = null) {
        if (text != null) {
            Snackbar.make(binding.rootLayout, text, Snackbar.LENGTH_SHORT).show()
        } else if (textRes != null) {
            Snackbar.make(binding.rootLayout, textRes, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun updateMenu(bookmarked: Boolean) {
        isBookmarked = bookmarked
        invalidateOptionsMenu()
    }

    private fun render(recipe: Recipe) {
        showScrollContainer()
        renderBannerFoodImage(recipe.image.orEmpty())
        renderHeaderContainer(
            recipe.servingCount ?: 0,
            recipe.cookingMinutes ?: 0,
            recipe.title,
            recipe.dishTypes?.first().orEmpty(),
            recipe.calories ?: 0
        )
        recipe.ingredients?.let {
            renderIngredientsList(it)
        }
        recipe.instructions?.let {
            renderInstructionsList(it)
        }
    }

    companion object {
        private const val RECIPE_ID_EXTRA_KEY = "RECIPE_ID_EXTRA_KEY"

        fun getIntent(context: Context, recipeId: String): Intent {
            return Intent(context, RecipeDetailActivity::class.java).also {
                it.putExtra(RECIPE_ID_EXTRA_KEY, recipeId)
            }
        }
    }

    override fun bindActivity() {
        setContentView(binding.root)
    }
}