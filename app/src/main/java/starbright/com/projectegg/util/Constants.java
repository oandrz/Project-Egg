/**
 * Created by Andreas on 29/9/2018.
 */

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
    public static final String JPG = ".jpg";

    public static final String PROVIDER_PACKAGE_NAME = "starbright.com.projectegg.fileprovider";


    //  Networking
    public static final String IMAGE_INGREDIENT_URL = "https://spoonacular.com/cdn/ingredients_100x100/";
    public static final String QUERY_PARAM_META_INFORMATION_KEY = "metaInformation";
    public static final String QUERY_PARAM_NUMBER_KEY = "number";
    public static final String QUERY_PARAM_LIMIT_LICENSE_KEY = "limitLicense";
    public static final String QUERY_PARAM_OFFSET_KEY = "offset";
    public static final String QUERY_PARAM_RANKING_KEY = "ranking";
    public static final String QUERY_PARAM_CUISINE_KEY = "cuisine";
    public static final String QUERY_PARAM_TYPE_KEY = "type";

    public static final String OFFSET_VALUE = "0";

    public static final String OPERATION_NOT_SUPPORTED = "Operation not supported";

    public static final String DATABASE_NAME = "db_recipe";
    private Constants() {}
}
