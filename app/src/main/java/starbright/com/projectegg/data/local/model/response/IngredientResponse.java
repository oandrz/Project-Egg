package starbright.com.projectegg.data.local.model.response;

import com.google.gson.annotations.SerializedName;

public class IngredientResponse {

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("image")
    private String mImagePath;

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getImagePath() {
        return mImagePath;
    }
}
