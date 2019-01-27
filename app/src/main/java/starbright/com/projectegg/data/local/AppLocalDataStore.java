/**
 * Created by Andreas on 7/10/2018.
 */

/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.data.local;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.data.AppDataStore;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.util.Constants;

/**
 * Created by Andreas on 4/8/2018.
 */

public class AppLocalDataStore implements AppDataStore {

    @Inject
    RecipeDatabase db;

    public AppLocalDataStore() {
        MyApp.getAppComponent().inject(this);
    }

    @NotNull
    @Override
    public Observable<List<Recipe>> getRecipes(@NotNull String ingredients, @Nullable String dishTypes, @Nullable String cuisines) {
        System.err.print(Constants.OPERATION_NOT_SUPPORTED);
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<List<Ingredient>> searchIngredient(String query) {
        System.err.println(Constants.OPERATION_NOT_SUPPORTED);
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<Recipe> getRecipeDetailInformation(String recipeId) {
        return db.daoAccess().getRecipe(Integer.parseInt(recipeId)).toObservable();
    }

    @Override
    public void saveDetailInformation(Recipe recipe) {
        db.daoAccess().insertRecipe(recipe);
    }
}
