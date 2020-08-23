/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 17 - 8 - 2020.
 */

package starbright.com.projectegg.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.data.model.local.SearchHistory

@Database(entities = [FavouriteRecipe::class, SearchHistory::class], version = 2)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun favoriteRecipeDao(): FavouriteRecipeDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}