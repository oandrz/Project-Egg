/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import starbright.com.projectegg.data.model.local.FavouriteRecipe

@Database(entities = [FavouriteRecipe::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun favoriteRecipeDao(): FavouriteRecipeDao
}