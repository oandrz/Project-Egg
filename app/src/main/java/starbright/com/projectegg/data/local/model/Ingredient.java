package starbright.com.projectegg.data.local.model;

import starbright.com.projectegg.data.local.model.response.IngredientResponse;
import starbright.com.projectegg.util.Constants;

public class Ingredient {
    private String mId;
    private String mName;
    private String mImageUrl;

    public Ingredient(IngredientResponse response) {
        mId = response.getId();
        mName = response.getName();
        mImageUrl = Constants.IMAGE_INGREDIENT_URL.concat(response.getImagePath());
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}