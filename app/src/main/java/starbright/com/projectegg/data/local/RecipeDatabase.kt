/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import starbright.com.projectegg.data.model.Converter
import starbright.com.projectegg.data.model.Recipe

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun daoAccess(): DaoAccess
}
