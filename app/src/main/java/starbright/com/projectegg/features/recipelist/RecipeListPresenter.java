/**
 * Created by Andreas on 22/9/2018.
 */

/**
 * Created by Andreas on 10/9/2018.
 */

/**
 * Created by Andreas on 9/9/2018.
 */

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

    private String lastSelectedDishType;
    private String lastSelectedCuisine;

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
        mView.setupSwipeRefreshLayout();
        getRecipesBasedIngredients(mapIngredients(), lastSelectedDishType, lastSelectedCuisine);
    }

    public void getRecipesBasedIngredients(String ingredients, String dishType, String cuisine) {
        mView.showLoadingBar();
        mCompositeDisposable.add(
                mRepository.getRecipes(ingredients, dishType, cuisine)
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
                        mView.hideLoadingBar();
                        mView.showErrorSnackBar(throwable.getMessage());
                    }
                })
        );
    }

    @Override
    public void handleListItemClicked(int position) {
        mView.showDetail(String.valueOf(mRecipes.get(position).getId()));
    }

    @Override
    public void handleRefresh() {
        getRecipesBasedIngredients(mapIngredients(), lastSelectedDishType, lastSelectedCuisine);
    }

    @Override
    public void handleFilter(String dishType, String cuisine) {
        lastSelectedDishType = dishType;
        lastSelectedCuisine = cuisine;
        getRecipesBasedIngredients(mapIngredients(), dishType, cuisine);
    }

    @Override
    public void setLastSelectedDishType(String dishType) {
        lastSelectedDishType = dishType;
    }

    @Override
    public void setLastSelectedCuisine(String cuisine) {
        lastSelectedCuisine = cuisine;
    }

    @Override
    public String getLastSelectedDishType() {
        return lastSelectedDishType;
    }

    @Override
    public String getLastSelectedCuisine() {
        return lastSelectedCuisine;
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
