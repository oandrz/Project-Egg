/**
 * Created by Andreas on 5/8/2018.
 */

package starbright.com.projectegg.features.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.util.GlideApp;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

public class RecipeDetailFragment extends Fragment implements RecipeDetailContract.View {

    private static final String BUNDLE_RECIPE_ID = "BUNDLE_RECIPE_ID";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.img_banner_food)
    ImageView imgBannerFood;

    @BindView(R.id.container_header)
    RelativeLayout containerHeader;

    @BindView(R.id.card_ingredient)
    CardView cardIngredient;

    @BindView(R.id.card_instruction)
    CardView cardInstruction;

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
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
        mPresenter.getRecipeDetailInformation(getArguments().getString(BUNDLE_RECIPE_ID));
    }

    @Override
    public void setPresenter(RecipeDetailPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateView(Recipe recipe) {
        renderBannerFoodImage(recipe.getImage());
        renderHeaderContainer(recipe);
    }

    private void renderBannerFoodImage(String imageUrl) {
        imgBannerFood.setVisibility(View.VISIBLE);
        GlideApp.with(getActivity())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imgBannerFood);
    }

    private void renderHeaderContainer(Recipe recipe) {
        containerHeader.setVisibility(View.VISIBLE);

    }

    interface FragmentListener {

    }
}
