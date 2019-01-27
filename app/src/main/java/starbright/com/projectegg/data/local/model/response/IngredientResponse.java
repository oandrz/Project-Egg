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

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        this.mImagePath = imagePath;
    }
}
