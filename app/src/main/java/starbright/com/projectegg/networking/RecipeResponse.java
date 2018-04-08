package starbright.com.projectegg.networking;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Andreas on 4/8/2018.
 */

public class RecipeResponse {
    @SerializedName("title")
    private String mTitle;

    @SerializedName("href")
    private String mHref;

    @SerializedName("ingredients")
    private String mIngredients;

    @SerializedName("thumbnail")
    private String mThumbnail;

    public String getmTitle() {
        return mTitle;
    }

    public String getmHref() {
        return mHref;
    }

    public String getmIngredients() {
        return mIngredients;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }
}
