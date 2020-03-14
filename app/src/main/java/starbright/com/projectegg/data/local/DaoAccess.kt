/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

import io.reactivex.Maybe
import starbright.com.projectegg.data.model.Recipe

@Dao
interface DaoAccess {

    @Insert
    fun insertRecipe(recipe: Recipe): Long

    @Update
    fun updateRecipe(recipe: Recipe): Int

    @Query("SELECT * FROM recipe WHERE id =:recipeId")
    fun getRecipe(recipeId: Int): Maybe<Recipe>
}
