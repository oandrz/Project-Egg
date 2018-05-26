package starbright.com.projectegg.features.recipelist;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.util.ClarifaiHelper;
import starbright.com.projectegg.util.Constants;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Andreas on 4/8/2018.
 */

@RuntimePermissions
public class RecipeListFragment extends Fragment
        implements ClarifaiHelper.Callback, RecipeListContract.View {

    private static final int CAMERA_REQUEST_CODE = 100;

    @Inject
    AppRepository repo;

    @Inject
    BaseSchedulerProvider schedulerProvider;

    @BindView(R.id.rv_recipe)
    RecyclerView rvRecipe;

    @BindView(R.id.loading_view)
    ProgressBar loadingView;

    private ClarifaiHelper mClarifaiHelper;
    private RecipeListContract.Presenter mPresenter;
    private RecipeListAdapter mAdapter;


    String mCurrentPhotoPath;

    public static RecipeListFragment newInstance() {
        Bundle args = new Bundle();
        RecipeListFragment fragment = new RecipeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        MyApp.getAppComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RecipeListPresenter(repo, this, schedulerProvider);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mClarifaiHelper = new ClarifaiHelper(getActivity());
        RecipeListFragmentPermissionsDispatcher.openCameraWithPermissionCheck(this);
        mPresenter.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RecipeListFragmentPermissionsDispatcher
                .onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            showLoadingBar();
            mClarifaiHelper.predict(Uri.fromFile(new File(mCurrentPhotoPath)), this);
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void openCamera() {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "starbright.com.projectegg.fileprovider",
                    photoFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onPredictionCompleted(String ingredients) {
        System.out.println(ingredients);
        mPresenter.getRecipesBasedIngredients(ingredients);
    }

    @Override
    public void setPresenter(RecipeListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setupRecyclerView() {
        rvRecipe.setLayoutManager(
                new LinearLayoutManager(getActivity()
                , LinearLayoutManager.VERTICAL
                , false)
        );
        mAdapter = new RecipeListAdapter(getActivity());
        rvRecipe.setAdapter(mAdapter);
    }

    @Override
    public void bindRecipesToList(List<Recipe> recipes) {
        rvRecipe.setVisibility(View.VISIBLE);
        mAdapter.setRecipes(recipes);
    }

    @Override
    public void showLoadingBar() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingBar() {
        loadingView.setVisibility(View.GONE);
    }

    private File createImageFile() throws IOException {
        final String timeStamp = new SimpleDateFormat(Constants.YYYY_MM_DD_FORMAT, Locale.US)
                .format(new Date());

        final File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        final File image = File.createTempFile(timeStamp, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
