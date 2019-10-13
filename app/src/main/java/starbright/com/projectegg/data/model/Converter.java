/**
 * Created by Andreas on 29/9/2018.
 */

/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.data.model;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converter {

    @TypeConverter
    public static List<Ingredient> fromStringIntoIngredients(String value) {
        Type listType = new TypeToken<List<Ingredient>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromIngredients(List<Ingredient> ingredients) {
        return new Gson().toJson(ingredients);
    }

    @TypeConverter
    public static List<Instruction> fromStringIntoInstructions(String value) {
        Type listType = new TypeToken<List<Instruction>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromInstructions(List<Instruction> instructions) {
        return new Gson().toJson(instructions);
    }
}
