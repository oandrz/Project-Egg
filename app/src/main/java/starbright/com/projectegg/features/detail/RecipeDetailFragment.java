/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.features.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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
//import starbright.com.projectegg.util.GlideApp;
import starbright.com.projectegg.util.TextViewRecyclerAdapter;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

public class RecipeDetailFragment extends Fragment implements RecipeDetailContract.View {

    private static final String BUNDLE_RECIPE_ID = "BUNDLE_RECIPE_ID";

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

    @BindView(R.id.scroll_container)
    NestedScrollView scrollContainer;

    @BindView(R.id.tv_empty_text)
    TextView tvEmptyText;

    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.tv_recipe_title)
    TextView tvRecipeName;

    @BindView(R.id.ad_view)
    AdView adView;

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
        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recipe_detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                mPresenter.handleShareMenuClicked();
                return true;
            case R.id.menu_webview:
                mPresenter.handleWebViewMenuClicked();
                return true;
        }
        return false;
    }

    @Override
    public void setPresenter(RecipeDetailPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hideScrollContainer() {
        scrollContainer.setVisibility(View.GONE);
    }

    @Override
    public void showScrollContainer() {
        scrollContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyStateTextView() {
        tvEmptyText.setVisibility(View.GONE);
    }

    @Override
    public void renderErrorStateTextView(String errorMessage) {
        tvEmptyText.setVisibility(View.VISIBLE);
        tvEmptyText.setText(errorMessage);
    }

    @Override
    public void renderEmptyStateTextView() {
        tvEmptyText.setVisibility(View.VISIBLE);
        tvEmptyText.setText(getString(R.string.detail_empty_label));
    }

    @Override
    public void renderBannerFoodImage(String imageUrl) {
        imgBannerFood.setVisibility(View.VISIBLE);
//        GlideApp.with(getActivity())
//                .load(imageUrl)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .centerCrop()
//                .into(imgBannerFood);
    }

    @Override
    public void renderHeaderContainer(int serving, int preparationMinutes, int cookingMinutes
            , String recipeName) {
        containerHeader.setVisibility(View.VISIBLE);
        tvRecipeName.setText(recipeName);
        tvServingCount.setText(getString(R.string.detail_serving_format, serving));
        tvPreparationTime.setText(getString(R.string.detail_time_format, preparationMinutes));
        tvCookingTime.setText(getString(R.string.detail_time_format, cookingMinutes));
    }

    @Override
    public void renderIngredientCard(List<Ingredient> ingredients) {
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

    @Override
    public void renderInstructionCard(List<Instruction> instructions) {
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

    @Override
    public void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.red));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getRecipeDetailInformation(getArguments().getString(BUNDLE_RECIPE_ID));
            }
        });
    }

    @Override
    public void setupAdView() {
        adView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void createShareIntent(String url, String recipeName) {
        final String textToShare = getString(R.string.detail_intent_share, recipeName, url);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.general_sharechooser)));
    }

    @Override
    public void navigateToWebViewActivity(String url) {
        mFragmentListener.navigateToWebViewActivity(url);
    }

    interface FragmentListener {
        void navigateToWebViewActivity(String url);
    }
}
