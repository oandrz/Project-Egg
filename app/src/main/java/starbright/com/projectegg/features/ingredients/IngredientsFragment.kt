/**
 * Created by Andreas on 11/8/2019.
 */

/**
 * Created by Andreas on 9/9/2018.
 */

package starbright.com.projectegg.features.ingredients

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_ingredients.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import starbright.com.projectegg.R
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.util.Constants
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@RuntimePermissions
class IngredientsFragment : Fragment(), IngredientsContract.View {

    @Inject
    lateinit var presenter: IngredientsPresenter

    private lateinit var cartBottomSheetDialogFragment: CartBottomSheetDialogFragment
    private lateinit var searchSuggestionAdapter: IngredientsAdapter
    private lateinit var dialog: MaterialDialog

    private var fragmentListener: FragmentListener? = null
    private var currentPhotoPath: String? = null
    private var timer: Timer? = null

    private val mIngredientsTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            timer?.cancel()
            presenter.handleSuggestionTextChanged(s.toString())
        }

        override fun afterTextChanged(s: Editable) {
            timer = Timer().also {
                it.schedule(AUTOCOMPLETE_DELAY.toLong()) {
                    activity?.runOnUiThread { presenter.searchIngredient(s.toString()) }
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
//        MyApp.getAppComponent().inject(this)
        super.onAttach(context)
        fragmentListener = context as FragmentListener?
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)
        presenter.view = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.start()
    }

    override fun onDetach() {
        fragmentListener = null
        et_search_ingredients.removeTextChangedListener(mIngredientsTextWatcher)
        cartBottomSheetDialogFragment.sheetListener = null
        presenter.onDestroy()
        super.onDetach()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            presenter.handleCameraResult(currentPhotoPath!!)
        }
    }

    override fun setupEtSearchIngredients() {
        et_search_ingredients.addTextChangedListener(mIngredientsTextWatcher)
    }

    override fun setupRvIngredientSuggestion() {
        activity?.let {
            searchSuggestionAdapter = IngredientsAdapter(it).also { adapter ->
                adapter.onClickListener = { selectedIngredient ->
                    presenter.handleSuggestionItemClicked(selectedIngredient)
                }
            }
        }

        rv_ingredients.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = searchSuggestionAdapter
        }
    }

    override fun setupImageActionButton() {
        img_action_button.setOnClickListener {
            presenter.handleActionButtonClicked(et_search_ingredients.text.toString())
        }
    }

    override fun setupTvCartCount() {
        tv_cart_count.setOnClickListener {
            presenter.handleCartTvClicked()
        }
    }

    override fun setupMoreButton() {
        iv_more.setOnClickListener {
            showPopupMenu(it)
        }
    }

    override fun setupMaterialProgressDialog() {
        dialog = MaterialDialog.Builder(activity as Context)
                .title(R.string.ingredients_dialog_progress_title)
                .content(R.string.ingredients_dialog_progress_content)
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .build()
    }

    override fun setupBottomSheetDialogFragment() {
        cartBottomSheetDialogFragment = CartBottomSheetDialogFragment().also {
            it.sheetListener = object : CartBottomSheetDialogFragment.SheetListener {
                override fun updateCart(ingredients: MutableList<Ingredient>) {
                    presenter.cart = ingredients.toMutableList()
                }

                override fun submitButtonClicked() {
                    fragmentListener?.navigateRecipeListActivity(presenter.cart)
                }
            }
        }
    }

    override fun clearEtSearchQuery() {
        et_search_ingredients.setText("")
    }

    override fun openCamera() {
        startCameraIntentWithPermissionCheck()
    }

    override fun showSearchSuggestion(ingredients: List<Ingredient>) {
        rv_ingredients.visibility = View.VISIBLE
        searchSuggestionAdapter.setIngredients(ingredients)
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
        Toast.makeText(activity, getString(R.string.ingredients_search_empty),
                Toast.LENGTH_SHORT).show()
    }

    override fun showItemMaxToast() {
        Toast.makeText(activity, getString(R.string.ingredients_cart_error_max),
                Toast.LENGTH_SHORT).show()
    }

    override fun showDuplicateItemToast() {
        Toast.makeText(activity, getString(R.string.ingredients_cart_error_duplicate),
                Toast.LENGTH_SHORT).show()
    }

    override fun showPredictionEmptyToast() {
        Toast.makeText(activity, getString(R.string.ingredients_prediction_empty),
                Toast.LENGTH_SHORT).show()
    }

    override fun showItemAddedToast() {
        Toast.makeText(activity, getString(R.string.ingredients_added_text),
                Toast.LENGTH_LONG).show()
    }

    override fun showSuccessPutIngredientToast(ingredientName: String) {
        Toast.makeText(activity, getString(R.string.ingredients_cart_included_format,
                ingredientName), Toast.LENGTH_SHORT).show()
    }

    override fun showMaterialProgressDialog() {
        dialog.show()
    }

    override fun hideMaterialProgressDialog() {
        dialog.dismiss()
    }

    override fun updateIngredientCount(count: Int) {
        tv_cart_count.text = count.toString()
    }

    override fun hideSoftKeyboard() {
        (activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).also {
            it.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }

    override fun showBottomSheetDialog() {
        cartBottomSheetDialogFragment.show(childFragmentManager,
                cartBottomSheetDialogFragment.tag)
    }

    override fun setCartItem(cart: MutableList<Ingredient>) {
        cartBottomSheetDialogFragment.cart = cart.toMutableList()
    }

    override fun hideSearchSuggestion() {
        rv_ingredients.visibility = View.GONE
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
                    activity!!,
                    Constants.PROVIDER_PACKAGE_NAME,
                    photoFile)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(Constants.YYYY_MM_DD_FORMAT, Locale.US).format(Date())
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(timeStamp, Constants.JPG, storageDir)

        currentPhotoPath = image.absolutePath
        return image
    }

    private fun showPopupMenu(view: View) {
        PopupMenu(activity, view).also {
            it.menuInflater.inflate(R.menu.search_ingredient_menu, it.menu)
            it.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_signout -> {
                        FirebaseAuth.getInstance().signOut()
                        fragmentListener?.navigateUserAccountActivity()
                        true
                    }
                    else -> false
                }
            }
            it.show()
        }
    }

    internal interface FragmentListener {
        fun navigateUserAccountActivity()
        fun navigateRecipeListActivity(ingredients: MutableList<Ingredient>)
    }

    companion object {
        private const val AUTOCOMPLETE_DELAY: Int = 600
        private const val CAMERA_REQUEST_CODE: Int = 101
        private const val BUNDLE_CART: String = "BUNDLE_CART"

        @JvmStatic
        fun newInstance(): IngredientsFragment = IngredientsFragment()
    }
}