/**
 * Created by Andreas on 11/8/2019.
 */

package starbright.com.projectegg.features.ingredients

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.fragment_ingredients.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import starbright.com.projectegg.BuildConfig
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.features.recipelist.RecipeListActivity
import starbright.com.projectegg.util.Constants
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@RuntimePermissions
class IngredientsActivity : BaseActivity<IngredientsContract.View,
    IngredientsPresenter>(), IngredientsContract.View {

    private lateinit var searchSuggestionAdapter: IngredientsAdapter
    private lateinit var dialog: MaterialDialog

    private var currentPhotoPath: String? = null

    private val mIngredientsTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            presenter.handleSuggestionTextChanged(s.toString())
        }

        override fun afterTextChanged(s: Editable) {
            presenter.searchIngredient(s.toString())
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_ingredients

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun getView(): IngredientsContract.View = this

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            presenter.handleCameraResult(currentPhotoPath!!)
        }
    }

    override fun setupView() {
        setupEtSearchIngredients()
        setupRvIngredientSuggestion()
        setupMaterialProgressDialog()
        setupImageActionButton()
        setupTvCartCount()
    }

    private fun setupEtSearchIngredients() {
        et_search_ingredients.addTextChangedListener(mIngredientsTextWatcher)
    }

    private fun setupRvIngredientSuggestion() {
        setupIngredientsRecyclerAdapter()
        rv_ingredients.let {
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.adapter = searchSuggestionAdapter
        }
    }

    private fun setupIngredientsRecyclerAdapter() {
        searchSuggestionAdapter = IngredientsAdapter(this).also { adapter ->
            adapter.onClickListener = { selectedIngredient ->
                presenter.handleSuggestionItemClicked(selectedIngredient)
            }
        }
    }

    private fun setupMaterialProgressDialog() {
        dialog = MaterialDialog.Builder(this)
            .title(R.string.ingredients_dialog_progress_title)
            .content(R.string.ingredients_dialog_progress_content)
            .progress(true, 0)
            .canceledOnTouchOutside(false)
            .build()
    }

    private fun setupImageActionButton() {
        img_action_button.setOnClickListener {
            presenter.handleActionButtonClicked(et_search_ingredients.text.toString())
        }
    }

    private fun setupTvCartCount() {
        tv_cart_count.setOnClickListener {
            presenter.handleCartTvClicked()
        }
    }

    override fun clearEtSearchQuery() {
        et_search_ingredients.setText("")
    }

    override fun openCamera() {
        startCameraIntentWithPermissionCheck()
    }

    override fun updateSuggestion(ingredients: List<Ingredient>) {
        rv_ingredients.visibility = View.VISIBLE
        searchSuggestionAdapter.setIngredients(ingredients)
    }

    override fun hideSearchSuggestion() {
        rv_ingredients.visibility = View.GONE
    }

    override fun showActionCamera() {
        img_action_button.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_camera)
        }
    }

    override fun showActionClear() {
        img_action_button.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_clear)
        }
    }

    override fun hideActionButton() {
        img_action_button.visibility = View.GONE
    }

    override fun showSuggestionProgressBar() {
        suggestion_progress_bar.visibility = View.VISIBLE
    }

    override fun hideSuggestionProgressBar() {
        suggestion_progress_bar.visibility = View.GONE
    }

    override fun showItemEmptyToast() {
        Toast.makeText(this, getString(R.string.ingredients_search_empty),
            Toast.LENGTH_SHORT).show()
    }

    override fun showItemMaxToast() {
        Toast.makeText(this, getString(R.string.ingredients_cart_error_max),
            Toast.LENGTH_SHORT).show()
    }

    override fun showDuplicateItemToast() {
        Toast.makeText(this, getString(R.string.ingredients_cart_error_duplicate),
            Toast.LENGTH_SHORT).show()
    }

    override fun showPredictionEmptyToast() {
        Toast.makeText(this, getString(R.string.ingredients_prediction_empty),
            Toast.LENGTH_SHORT).show()
    }

    override fun showItemAddedToast() {
        Toast.makeText(this, getString(R.string.ingredients_added_text),
            Toast.LENGTH_SHORT).show()
    }

    override fun showSuccessPutIngredientToast(ingredientName: String) {
        Toast.makeText(this, getString(R.string.ingredients_cart_included_format,
            ingredientName), Toast.LENGTH_SHORT).show()
    }

    override fun showMaterialProgressDialog() {
        dialog.show()
    }

    override fun hideMaterialProgressDialog() {
        dialog.dismiss()
    }

    override fun showBottomSheetDialog(cart: MutableList<Ingredient>) {
        val cartBottomSheetDialogFragment = CartBottomSheetDialogFragment().also {
            it.sheetListener = object : CartBottomSheetDialogFragment.SheetListener {
                override fun onItemRemovedFromCart() {
                    presenter.handleItemRemovedFromCart()
                }

                override fun submitButtonClicked() {
                    presenter.handleSubmitButtonClicked()
                }
            }
            it.cart = cart
        }
        cartBottomSheetDialogFragment.show(supportFragmentManager, cartBottomSheetDialogFragment.tag)
    }

    override fun updateIngredientCount(count: Int) {
        tv_cart_count.text = count.toString()
    }

    override fun navigateToRecipeList(cart: List<Ingredient>) {
        startActivity(RecipeListActivity.newIntent(this, cart))
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun startCameraIntent() {
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (photoFile != null) {
            val photoURI = FileProvider.getUriForFile(
                this,
                "${BuildConfig.APPLICATION_ID}.FileProvider",
                photoFile)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(Constants.YYYY_MM_DD_FORMAT, Locale.US).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(timeStamp, Constants.JPG, storageDir)

        currentPhotoPath = image.absolutePath
        return image
    }

    companion object {
        private const val CAMERA_REQUEST_CODE: Int = 101

        fun newIntent(context: Context): Intent = Intent(context, IngredientsActivity::class.java)
    }
}
