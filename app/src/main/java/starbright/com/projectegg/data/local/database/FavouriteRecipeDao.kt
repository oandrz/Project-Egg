/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import starbright.com.projectegg.data.model.local.FavouriteRecipe

@Dao
interface FavouriteRecipeDao {
    @Query("SELECT * FROM FavouriteRecipe")
    fun getFavouriteRecipe(): Observable<List<FavouriteRecipe>>

    @Insert
    fun addFavouriteRecipe(recipe: FavouriteRecipe): Completable

    @Delete
    fun removeFavouriteRecipe(recipe: FavouriteRecipe): Completable
}