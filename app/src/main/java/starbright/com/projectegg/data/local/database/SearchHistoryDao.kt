/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

package starbright.com.projectegg.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import starbright.com.projectegg.data.model.local.SearchHistory

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSearchHistory(history: SearchHistory): Completable

    @Query("SELECT * FROM SearchHistory ORDER BY created_at DESC")
    fun getSearchHistory(): Maybe<List<SearchHistory>>

    @Query("DELETE FROM SearchHistory WHERE search_query = :query")
    fun removeSearchHistory(query: String): Completable

    @Query("UPDATE SearchHistory SET created_at = :updatedAt WHERE search_query = :query")
    fun updateExistingQueryTimestamp(query: String, updatedAt: Long): Completable

    @Query("SELECT * FROM SearchHistory WHERE search_query = :query LIMIT 1")
    fun getRecentSearchByQuery(query: String): Maybe<List<SearchHistory>>
}