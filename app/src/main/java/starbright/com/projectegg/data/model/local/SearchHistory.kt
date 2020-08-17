/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 17 - 8 - 2020.
 */

package starbright.com.projectegg.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = ROW_SEARCH_QUERY) val query: String,
    @ColumnInfo(name = ROW_CREATED_AT) val createdAt: Long
)

private const val ROW_SEARCH_QUERY = "search_query"
private const val ROW_CREATED_AT = "created_at"