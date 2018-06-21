package starbright.com.projectegg.util;

/**
 * Created by Andreas on 4/8/2018.
 */

public final class Constants {

    public static final String COMMA = ",";
    public static final String YYYY_MM_DD_FORMAT = "yyyyMMdd";
    public static final String EMAIL_FORMAT = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";


//  Networking
    public static final String IMAGE_INGREDIENT_URL = "https://spoonacular.com/cdn/ingredients_100x100/";
    public static final String QUERY_PARAM_META_INFORMATION_KEY = "metaInformation";
    public static final String QUERY_PARAM_NUMBER_KEY = "number";

    private Constants() {}
}
