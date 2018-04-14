package starbright.com.projectegg.data;

import java.util.List;

import io.reactivex.Observable;
import starbright.com.projectegg.data.local.model.Recipe;

/**
 * Created by Andreas on 4/8/2018.
 */

public interface AppDataStore {
    Observable<List<Recipe>> getRecipes(String ingredients);
}
