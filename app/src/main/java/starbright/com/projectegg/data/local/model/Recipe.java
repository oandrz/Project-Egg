package starbright.com.projectegg.data.local.model;

import starbright.com.projectegg.data.local.model.response.RecipeResponse;

/**
 * Created by Andreas on 4/8/2018.
 */

public class Recipe {
    private int mId;
    private String mTitle;
    private String mImage;
    private String mImageType;
    private int mUsedIngredientCount;
    private int mMissedIngredientCount;
    private int mLikes;

    public Recipe(int id,
                  String title,
                  String image,
                  String imageType,
                  int usedIngredientCount,
                  int missedIngredientCount,
                  int likes) {
        mId = id;
        mTitle = title;
        mImage = image;
        mImageType = imageType;
        mUsedIngredientCount = usedIngredientCount;
        mMissedIngredientCount = missedIngredientCount;
        mLikes = likes;
    }

    public Recipe(RecipeResponse response) {
        mId = response.getId();
        mTitle = response.getTitle();
        mImage = response.getImage();
        mImageType = response.getImageType();
        mUsedIngredientCount = response.getUsedIngredientCount();
        mMissedIngredientCount = response.getMissedingredientCount();
        mLikes = response.getLikes();
    }

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

    public int getMissedIngredientCount() {
        return mMissedIngredientCount;
    }

    public int getLikes() {
        return mLikes;
    }
}
