package starbright.com.projectegg.features.detail;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

class RecipeDetailPresenter implements RecipeDetailContract.Presenter {

    private final AppRepository mRepository;
    private final RecipeDetailContract.View mView;
    private final BaseSchedulerProvider mSchedulerProvider;
    private CompositeDisposable mCompositeDisposable;

    private String mRecipeId;
    private Recipe mRecipe;

    RecipeDetailPresenter(AppRepository repository,
                          RecipeDetailContract.View view,
                          BaseSchedulerProvider schedulerProvider) {
        mRepository = repository;
        mView = view;
        mView.setPresenter(this);
        mSchedulerProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void start() {
        mView.setupAdView();
        mView.setupSwipeRefreshLayout();
        getRecipeDetailInformation(mRecipeId);
    }

    @Override
    public void setRecipeId(String recipeId) {
        mRecipeId = recipeId;
    }

    @Override
    public void getRecipeDetailInformation(String recipeId) {
        mView.hideScrollContainer();
        mView.showProgressBar();
        mCompositeDisposable
                .add(mRepository.getRecipeDetailInformation(recipeId)
                        .subscribeOn(mSchedulerProvider.computation())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe(new Consumer<Recipe>() {
                            @Override
                            public void accept(Recipe recipe) {
                                mView.hideProgressBar();
                                mRecipe = recipe;
                                if (mRecipe != null) {
                                    mView.hideEmptyStateTextView();
                                    updateView();
                                } else {
                                    mView.renderEmptyStateTextView();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                mView.hideProgressBar();
                                if (mRecipe != null) {
                                    mView.hideEmptyStateTextView();
                                    updateView();
                                } else {
                                    mView.renderEmptyStateTextView();
                                }
//                                mView.renderErrorStateTextView(throwable.getMessage());
                            }
                        }));
    }

    @Override
    public void handleShareMenuClicked() {
        mView.createShareIntent(mRecipe.getSourceStringUrl(), mRecipe.getTitle());
    }

    @Override
    public void handleWebViewMenuClicked() {
        mView.navigateToWebViewActivity(mRecipe.getSourceStringUrl());
    }

    private void updateView() {
        mView.showScrollContainer();
        mView.renderBannerFoodImage(mRecipe.getImage());
        mView.renderHeaderContainer(mRecipe.getServingCount(), mRecipe.getPreparationMinutes(),
                mRecipe.getCookingMinutes(), mRecipe.getTitle());
        mView.renderIngredientCard(mRecipe.getIngredients());
        mView.renderInstructionCard(mRecipe.getInstructions());
    }

    private boolean isIngredientsEmpty() {
        return mRecipe.getIngredients() == null || mRecipe.getIngredients().isEmpty();
    }

    private boolean isInstructionEmpty() {
        return mRecipe.getInstructions() == null || mRecipe.getInstructions().isEmpty();
    }
}
