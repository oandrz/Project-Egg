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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.data.local.model.Instruction;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.util.GlideApp;
import starbright.com.projectegg.util.TextViewRecyclerAdapter;
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

    @BindView(R.id.rv_ingredient)
    RecyclerView rvIngredient;

    @BindView(R.id.rv_instruction)
    RecyclerView rvInstruction;

    @BindView(R.id.tv_serving_count)
    TextView tvServingCount;

    @BindView(R.id.tv_preparation_time)
    TextView tvPreparationTime;

    @BindView(R.id.tv_cooking_time)
    TextView tvCookingTime;

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
        mPresenter.setRecipeId(getArguments().getString(BUNDLE_RECIPE_ID));
        mPresenter.start();
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
        renderBodyContainer(recipe);
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
        tvServingCount.setText(getString(R.string.detail_serving_format, recipe.getServing()));
        tvPreparationTime.setText(getString(R.string.detail_time_format,
                recipe.getPreparationMinutes()));
        tvCookingTime.setText(getString(R.string.detail_time_format, recipe.getCookingMinutes()));
    }

    private void renderBodyContainer(Recipe recipe) {
        renderIngredientCard(recipe.getIngredients());
        renderInstructionCard(recipe.getInstruction());
    }

    private void renderIngredientCard(List<Ingredient> ingredients) {
        final List<String> formattedInstructions = new ArrayList<>(ingredients.size());
        int number = 1;
        for (Ingredient ingredient : ingredients) {
            formattedInstructions.add(getString(R.string.general_number_text_unit_format,
                    number, ingredient.getName(), ingredient.getAmount(), ingredient.getUnit()));
            number++;
        }
        cardIngredient.setVisibility(View.VISIBLE);
        rvIngredient.setNestedScrollingEnabled(false);
        rvIngredient.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        final TextViewRecyclerAdapter adapter = new TextViewRecyclerAdapter(getActivity(),
                formattedInstructions);
        rvIngredient.setAdapter(adapter);
    }

    private void renderInstructionCard(List<Instruction> instructions) {
        final List<String> formattedInstructions = new ArrayList<>(instructions.size());
        for (Instruction instruction : instructions) {
            formattedInstructions.add(getString(R.string.general_number_text_format,
                    instruction.getNumber(), instruction.getStep()));
        }
        cardInstruction.setVisibility(View.VISIBLE);
        rvInstruction.setNestedScrollingEnabled(false);
        rvInstruction.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        final TextViewRecyclerAdapter adapter = new TextViewRecyclerAdapter(getActivity(),
                formattedInstructions);
        rvInstruction.setAdapter(adapter);
    }

    interface FragmentListener {

    }
}
