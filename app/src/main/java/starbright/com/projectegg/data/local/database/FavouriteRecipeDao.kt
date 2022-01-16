/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 31 - 7 - 2020.
 */

package starbright.com.projectegg.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import starbright.com.projectegg.data.model.local.FavouriteRecipe

@Dao
interface FavouriteRecipeDao {
    @Query("SELECT * FROM FavouriteRecipe")
    fun getFavouriteRecipe(): Flow<List<FavouriteRecipe>>

    @Query("SELECT * FROM FavouriteRecipe WHERE recipe_id=:recipeId")
    suspend fun getFavouriteRecipeWith(recipeId: Int): FavouriteRecipe?

    @Insert
    suspend fun addFavouriteRecipe(recipe: FavouriteRecipe)

    @Query("DELETE FROM FavouriteRecipe WHERE recipe_id = :recipeId")
    suspend fun removeFavouriteRecipe(recipeId: Int)
}