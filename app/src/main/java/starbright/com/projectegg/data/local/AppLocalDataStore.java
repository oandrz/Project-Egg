package starbright.com.projectegg.data.local;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.data.AppDataStore;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.util.Constants;

public class AppLocalDataStore implements AppDataStore {

    @Inject
    RecipeDatabase db;

    public AppLocalDataStore() {
        MyApp.getAppComponent().inject(this);
    }

    @Override
    public Observable<List<Recipe>> getRecipes(String ingredients) {
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
