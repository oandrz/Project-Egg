/**
 * Created by Andreas on 5/8/2018.
 */

package starbright.com.projectegg.features.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

public class RecipeDetailFragment extends Fragment
        implements RecipeDetailContract.View {

    private static final String BUNDLE_RECIPE_ID = "BUNDLE_RECIPE_ID";

    @Inject
    AppRepository repository;

    @Inject
    BaseSchedulerProvider schedulerProvider;

    private RecipeDetailContract.Presenter mPresenter;
    private FragmentListener mFragmentListener;

    public static RecipeDetailFragment newInstance(String recipeId) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_RECIPE_ID, recipeId);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(args);
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
        new RecipeDetailPresenter(repository, this, schedulerProvider);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        return view;
    }

    @Override
    public void setPresenter(RecipeDetailPresenter presenter) {
        mPresenter = presenter;
    }

    interface FragmentListener {

    }
}
