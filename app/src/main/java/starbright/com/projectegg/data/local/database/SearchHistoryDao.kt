/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 21 - 8 - 2020.
 */

package starbright.com.projectegg.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import starbright.com.projectegg.data.model.local.SearchHistory

@Dao
interface SearchHistoryDao {
    @Insert
    fun addSearchHistory(history: SearchHistory): Completable

    @Query("SELECT * FROM SearchHistory ORDER BY created_at DESC LIMIT 5")
    fun getSearchHistory(): Observable<List<SearchHistory>>

    @Query("DELETE FROM SearchHistory WHERE search_query = :query")
    fun removeSearchHistory(query: String): Completable
}