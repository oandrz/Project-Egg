package starbright.com.projectegg.features.recipelist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

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
        mClarifaiHelper = new ClarifaiHelper();
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
            mClarifaiHelper.predict(data, this);
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void openCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE);
    }

    @Override
    public void onPredictionCompleted(String ingredients) {
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
}
