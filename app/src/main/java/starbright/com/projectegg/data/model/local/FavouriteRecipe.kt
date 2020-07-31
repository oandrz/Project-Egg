/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 31 - 7 - 2020.
 */

package starbright.com.projectegg.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = ROW_RECIPE_ID) val recipeId: Int,
    @ColumnInfo(name = ROW_RECIPE_TITLE) val recipeTitle: String,
    @ColumnInfo(name = ROW_RECIPE_COOK_TIME) val cookingTimeInMinutes: Int,
    @ColumnInfo(name = ROW_RECIPE_SERVING_COUNT) val servingCount: Int
)

private const val ROW_RECIPE_ID = "recipe_id"
private const val ROW_RECIPE_TITLE = "recipe_title"
private const val ROW_RECIPE_COOK_TIME = "recipe_image_url"
private const val ROW_RECIPE_SERVING_COUNT = "recipe_serving_count"