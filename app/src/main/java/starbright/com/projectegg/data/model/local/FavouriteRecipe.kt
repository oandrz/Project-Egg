/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 1 - 8 - 2020.
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
    @ColumnInfo(name = ROW_RECIPE_IMAGE_URL) val recipeImageUrl: String,
    @ColumnInfo(name = ROW_RECIPE_COOK_TIME) val cookingTimeInMinutes: Int,
    @ColumnInfo(name = ROW_RECIPE_SERVING_COUNT) val servingCount: Int,
    @ColumnInfo(name = ROW_RECIPE_SOURCE) val source: String
)

private const val ROW_RECIPE_ID = "recipe_id"
private const val ROW_RECIPE_TITLE = "recipe_title"
private const val ROW_RECIPE_COOK_TIME = "recipe_cooking_time"
private const val ROW_RECIPE_SERVING_COUNT = "recipe_serving_count"
private const val ROW_RECIPE_IMAGE_URL = "recipe_image_url"
private const val ROW_RECIPE_SOURCE = "recipe_source_name"