package starbright.com.projectegg.data.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Andreas on 4/8/2018.
 */

public class RecipeResponse {

    @SerializedName("id")
    private int mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("image")
    private String mImage;

    @SerializedName("imageType")
    private String mImageType;

    @SerializedName("usedIngredientCount")
    private int mUsedIngredientCount;

    @SerializedName("missedIngredientCount")
    private int mMissedingredientCount;

    @SerializedName("likes")
    private int mLikes;

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImage() {
        return mImage;
    }

    public String getImageType() {
        return mImageType;
    }

    public int getUsedIngredientCount() {
        return mUsedIngredientCount;
    }

    public int getMissedingredientCount() {
        return mMissedingredientCount;
    }

    public int getLikes() {
        return mLikes;
    }
}
