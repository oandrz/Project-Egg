/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import io.reactivex.Maybe
import starbright.com.projectegg.data.local.model.Recipe

@Dao
interface DaoAccess {

    @Insert
    fun insertRecipe(recipe: Recipe): Long

    @Update
    fun updateRecipe(recipe: Recipe): Int

    @Query("SELECT * FROM recipe WHERE id =:recipeId")
    fun getRecipe(recipeId: Int): Maybe<Recipe>
}
