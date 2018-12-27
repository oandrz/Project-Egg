package starbright.com.projectegg.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import io.reactivex.Maybe;
import starbright.com.projectegg.data.local.model.Recipe;

@Dao
public interface DaoAccess {

    @Insert
    long insertRecipe(Recipe recipe);

    @Update
    int updateRecipe(Recipe recipe);

    @Query("SELECT * FROM recipe WHERE id =:recipeId")
    Maybe<Recipe> getRecipe(int recipeId);
}
