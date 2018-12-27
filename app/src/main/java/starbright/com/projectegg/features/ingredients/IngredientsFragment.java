package starbright.com.projectegg.features.ingredients;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.util.ClarifaiHelper;
import starbright.com.projectegg.util.Constants;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

import static android.app.Activity.RESULT_OK;

@RuntimePermissions
public class IngredientsFragment extends Fragment implements IngredientsContract.View,
        IngredientsAdapter.Listener {

    private static final int AUTOCOMPLETE_DELAY = 600;
    private static final int CAMERA_REQUEST_CODE = 101;

    private static final String BUNDLE_CART = "BUNDLE_CART";

    @Inject
    AppRepository repository;

    @Inject
    Compressor compressor;

    @Inject
    BaseSchedulerProvider schedulerProvider;

    @BindView(R.id.et_search_ingredients)
    EditText etSearchIngredients;

    @BindView(R.id.img_action_button)
    ImageView imgActionButton;

    @BindView(R.id.rv_ingredients)
    RecyclerView rvIngredients;

    @BindView(R.id.suggestion_progress_bar)
    ProgressBar suggestionProgressBar;

    @BindView(R.id.tv_cart_count)
    TextView tvCartCount;

    private Timer mTimer;

    private IngredientsAdapter mSearchSuggestionAdapter;
    private IngredientsContract.Presenter mPresenter;
    private MaterialDialog mDialog;
    private CartBottomSheetDialogFragment mCartBottomSheetDialogFragment;
    private String mCurrentPhotoPath;

    private FragmentListener mFragmentListener;

    private TextWatcher mIngredientsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mTimer != null) {
                mTimer.cancel();
            }
            mPresenter.handleSuggestionTextChanged(s.toString());
        }

        @Override
        public void afterTextChanged(final Editable s) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.searchIngredient(s.toString());
                            }
                        });
                    }
                }
            }, AUTOCOMPLETE_DELAY);
        }
    };

    public static IngredientsFragment newInstance() {
        return new IngredientsFragment();
    }

    @Override
    public void onAttach(Context context) {
        MyApp.getAppComponent().inject(this);
        super.onAttach(context);
        mFragmentListener = (FragmentListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new IngredientsPresenter(
                repository,
                this,
                schedulerProvider,
                new ClarifaiHelper(getActivity()),
                compressor
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
        if (savedInstanceState != null) {
            mPresenter.setCart(savedInstanceState.<Ingredient>getParcelableArrayList(BUNDLE_CART));
        }
    }

    @Override
    public void onDetach() {
        mFragmentListener = null;
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            mPresenter.handleCameraResult(mCurrentPhotoPath);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_CART, mPresenter.getCart());
    }

    @Override
    public void setPresenter(IngredientsPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setupEtSearchIngredients() {
        etSearchIngredients.addTextChangedListener(mIngredientsTextWatcher);
    }

    @Override
    public void setupRvIngredientSuggestion() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvIngredients.setLayoutManager(layoutManager);

        mSearchSuggestionAdapter = new IngredientsAdapter(getActivity());
        mSearchSuggestionAdapter.setListener(this);
        rvIngredients.setAdapter(mSearchSuggestionAdapter);
    }

    @Override
    public void setupMaterialProgressDialog() {
        mDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.ingredients_dialog_progress_title)
                .content(R.string.ingredients_dialog_progress_content)
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .build();
    }

    @Override
    public void setupBottomSheetDialogFragment() {
        mCartBottomSheetDialogFragment = new CartBottomSheetDialogFragment();
        mCartBottomSheetDialogFragment.setListener(new CartBottomSheetDialogFragment.SheetListener() {
            @Override
            public void updateCart(List<Ingredient> ingredients) {
                mPresenter.setCart(ingredients);
            }

            @Override
            public void submitButtonClicked() {
                mFragmentListener.navigateRecipeListActivity(mPresenter.getCart());
            }
        });
    }

    @Override
    public void clearEtSearchQuery() {
        etSearchIngredients.setText("");
    }

    @Override
    public void openCamera() {
        IngredientsFragmentPermissionsDispatcher.startCameraIntentWithPermissionCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        IngredientsFragmentPermissionsDispatcher.onRequestPermissionsResult(this,
                requestCode, grantResults);
    }

    @Override
    public void showSearchSuggestion(List<Ingredient> ingredients) {
        rvIngredients.setVisibility(View.VISIBLE);
        mSearchSuggestionAdapter.setIngredients(ingredients);
    }

    @Override
    public void showActionCamera() {
        imgActionButton.setVisibility(View.VISIBLE);
        imgActionButton.setImageResource(R.drawable.ic_camera);
    }

    @Override
    public void showActionClear() {
        imgActionButton.setVisibility(View.VISIBLE);
        imgActionButton.setImageResource(R.drawable.ic_clear);
    }

    @Override
    public void hideActionButton() {
        imgActionButton.setVisibility(View.GONE);
    }

    @Override
    public void showSuggestionProgressBar() {
        suggestionProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSuggestionProgressBar() {
        suggestionProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showItemEmptyToast() {
        Toast.makeText(getActivity(), getString(R.string.ingredients_search_empty),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showItemMaxToast() {
        Toast.makeText(getActivity(), getString(R.string.ingredients_cart_error_max),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDuplicateItemToast() {
        Toast.makeText(getActivity(), getString(R.string.ingredients_cart_error_duplicate),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPredictionEmptyToast() {
        Toast.makeText(getActivity(), getString(R.string.ingredients_prediction_empty),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showItemAddedToast() {
        Toast.makeText(getActivity(), getString(R.string.ingredients_added_text),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessPutIngredientToast(String ingredientName) {
        Toast.makeText(getActivity(), getString(R.string.ingredients_cart_included_format,
                ingredientName), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMaterialProgressDialog() {
        mDialog.show();
    }

    @Override
    public void hideMaterialProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void updateIngredientCount(int count) {
        tvCartCount.setText(String.valueOf(count));
    }

    @Override
    public void hideSoftKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    @Override
    public void showBottomSheetDialog() {
        mCartBottomSheetDialogFragment.show(getChildFragmentManager(),
                mCartBottomSheetDialogFragment.getTag());
    }

    @Override
    public void setCartItem(List<Ingredient> cart) {
        mCartBottomSheetDialogFragment.setCartIngredient(cart);
    }

    @Override
    public void hideSearchSuggestion() {
        rvIngredients.setVisibility(View.GONE);
    }

    @Override
    public void onSuggestionItemClicked(Ingredient selectedItem) {
        mPresenter.handleSuggestionItemClicked(selectedItem);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void startCameraIntent() {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(
                    getActivity(),
                    Constants.PROVIDER_PACKAGE_NAME,
                    photoFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    @OnClick({
            R.id.img_action_button,
            R.id.tv_cart_count,
            R.id.iv_more
    })
    void onClickedEvent(View view) {
        switch (view.getId()) {
            case R.id.img_action_button:
                mPresenter.handleActionButtonClicked(etSearchIngredients.getText().toString());
                break;
            case R.id.tv_cart_count:
                mPresenter.handleCartTvClicked();
                break;
            case R.id.iv_more:
                showPopupMenu(view);
                break;
        }
    }

    private File createImageFile() throws IOException {
        final String timeStamp = new SimpleDateFormat(Constants.YYYY_MM_DD_FORMAT, Locale.US)
                .format(new Date());

        final File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        final File image = File.createTempFile(timeStamp, Constants.JPG, storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void showPopupMenu(View view) {
        final PopupMenu popup = new PopupMenu(getActivity(), view);
        final MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.search_ingredient_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_signout:
                        FirebaseAuth.getInstance().signOut();
                        mFragmentListener.navigateUserAccountActivity();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    interface FragmentListener {
        void navigateUserAccountActivity();

        void navigateRecipeListActivity(List<Ingredient> ingredients);
    }
}