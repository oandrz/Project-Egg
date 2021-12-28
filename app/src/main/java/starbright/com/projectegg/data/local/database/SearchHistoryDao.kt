/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

package starbright.com.projectegg.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import starbright.com.projectegg.data.model.local.SearchHistory

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSearchHistory(history: SearchHistory)

    @Query("SELECT * FROM SearchHistory ORDER BY created_at DESC")
    fun getSearchHistory(): Flow<List<SearchHistory>>

    @Query("DELETE FROM SearchHistory WHERE search_query = :query")
    fun removeSearchHistory(query: String)

    @Query("UPDATE SearchHistory SET created_at = :updatedAt WHERE search_query = :query")
    suspend fun updateExistingQueryTimestamp(query: String, updatedAt: Long)

    @Query("SELECT * FROM SearchHistory WHERE search_query = :query")
    fun getRecentSearchByQuery(query: String): Flow<List<SearchHistory>>
}