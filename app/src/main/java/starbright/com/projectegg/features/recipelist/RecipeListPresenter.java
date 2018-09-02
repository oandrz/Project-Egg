package starbright.com.projectegg.features.recipelist;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

/**
 * Created by Andreas on 4/14/2018.
 */

class RecipeListPresenter implements RecipeListContract.Presenter {

    private final AppRepository mRepository;
    private final RecipeListContract.View mView;
    private final BaseSchedulerProvider mSchedulerProvider;
    private CompositeDisposable mCompositeDisposable;

    private List<Recipe> mRecipes = new ArrayList<>();
    private List<Ingredient> mIngredients;

    RecipeListPresenter(AppRepository repo,
                               RecipeListContract.View view,
                               BaseSchedulerProvider schedulerProvider) {
        mRepository = repo;
        mView = view;
        mView.setPresenter(this);
        mSchedulerProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void start() {
        mView.setupRecyclerView();
        getRecipesBasedIngredients(mapIngredients());
    }

    @Override
    public void getRecipesBasedIngredients(String ingredients) {
        mView.showLoadingBar();
        mCompositeDisposable.add(
                mRepository.getRecipes(ingredients)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<List<Recipe>>() {
                    @Override
                    public void accept(List<Recipe> recipes) {
                        mRecipes = recipes;
                        mView.hideLoadingBar();
                        mView.bindRecipesToList(recipes);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        // TODO: 02/09/18 Show Error Snackbar
                        mView.hideLoadingBar();
                        System.out.println(throwable.getMessage());
                    }
                })
        );
    }

    @Override
    public void handleListItemClicked(int position) {
        mView.showDetail(String.valueOf(mRecipes.get(position).getId()));
    }

    @Override
    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = new ArrayList<>(ingredients);
    }

    private String mapIngredients() {
        final StringBuilder stringBuilder = new StringBuilder();
        int ingredientSize = mIngredients.size();
        for (Ingredient ingredient : mIngredients) {
            stringBuilder.append(ingredient.getName());
            ingredientSize--;
            if (ingredientSize > 0) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
