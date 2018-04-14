package starbright.com.projectegg.data.local.model;

import starbright.com.projectegg.data.local.model.response.RecipeResponse;

/**
 * Created by Andreas on 4/8/2018.
 */

public class Recipe {
    private String mIngredients;
    private String mThumbnail;
    private String mTitle;
    private String mHref;

    public Recipe(String ingredients, String thumbnail, String title, String href) {
        mIngredients = ingredients;
        mThumbnail = thumbnail;
        mTitle = title;
        mHref = href;
    }

    public Recipe(RecipeResponse recipeResponse) {
        mIngredients = recipeResponse.getmIngredients();
        mThumbnail = recipeResponse.getThumbnail();
        mTitle = recipeResponse.getmTitle();
        mHref = recipeResponse.getmHref();
    }

    public String getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(String mIngredients) {
        this.mIngredients = mIngredients;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmHref() {
        return mHref;
    }

    public void setmHref(String mHref) {
        this.mHref = mHref;
    }
}
