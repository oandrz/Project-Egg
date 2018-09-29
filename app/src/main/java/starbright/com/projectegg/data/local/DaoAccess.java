/**
 * Created by Andreas on 29/9/2018.
 */

/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import io.reactivex.Single;
import starbright.com.projectegg.data.local.model.Recipe;

@Dao
public interface DaoAccess {

    @Insert
    long insertRecipe(Recipe recipe);

    @Update
    int updateRecipe(Recipe recipe);

    @Query("SELECT * FROM recipe WHERE id=:recipeId")
    Single<Recipe> getRecipe(int recipeId);
}
