/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.data.local


import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

import starbright.com.projectegg.data.local.model.Converter
import starbright.com.projectegg.data.local.model.Recipe

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun daoAccess(): DaoAccess
}
