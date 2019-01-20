/**
 * Created by Andreas on 10/9/2018.
 */

/**
 * Created by Andreas on 9/9/2018.
 */

package starbright.com.projectegg.features.recipelist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.features.filter.FilterActivity;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

/**
 * Created by Andreas on 4/8/2018.
 */

public class RecipeListFragment extends Fragment implements RecipeListContract.View,
        RecipeListAdapter.Listener {

    private static String INGREDIENT_LIST_BUNDLE = "INGREDIENT_LIST_BUNDLE";

    @Inject
    AppRepository repo;

    @Inject
    BaseSchedulerProvider schedulerProvider;

    @BindView(R.id.rv_recipe)
    RecyclerView rvRecipe;

    @BindView(R.id.root_layout)
    FrameLayout rootLayout;

    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshContainer;

    private FragmentListener mFragmentListener;
    private RecipeListContract.Presenter mPresenter;
    private RecipeListAdapter mAdapter;

    public static RecipeListFragment newInstance(ArrayList<Ingredient> ingredients) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(INGREDIENT_LIST_BUNDLE, ingredients);
        RecipeListFragment fragment = new RecipeListFragment();
        fragment.setArguments(bundle);
        return fragment;
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
        setHasOptionsMenu(true);
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
        mPresenter.setIngredients(getArguments().
                <Ingredient>getParcelableArrayList(INGREDIENT_LIST_BUNDLE));
        mPresenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.setListener(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recipe_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                startActivity(FilterActivity.newIntent(getActivity()));
                ;
                break;
        }
        return super.onOptionsItemSelected(item);
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
        mAdapter.setListener(this);
        rvRecipe.setAdapter(mAdapter);
    }

    @Override
    public void setupSwipeRefreshLayout() {
        swipeRefreshContainer.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.red));
        swipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.handleRefresh();
            }
        });
    }

    @Override
    public void bindRecipesToList(List<Recipe> recipes) {
        rvRecipe.setVisibility(View.VISIBLE);
        mAdapter.setRecipes(recipes);
    }

    @Override
    public void showLoadingBar() {
        swipeRefreshContainer.setRefreshing(true);
    }

    @Override
    public void hideLoadingBar() {
        swipeRefreshContainer.setRefreshing(false);
    }

    @Override
    public void showDetail(String recipeId) {
        mFragmentListener.navigateRecipeDetailActivity(recipeId);
    }

    @Override
    public void showErrorSnackBar(String errorMessage) {
        Snackbar.make(rootLayout, errorMessage, Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onItemClicked(int position) {
        mPresenter.handleListItemClicked(position);
    }

    interface FragmentListener {
        void navigateRecipeDetailActivity(String recipeId);
    }
}