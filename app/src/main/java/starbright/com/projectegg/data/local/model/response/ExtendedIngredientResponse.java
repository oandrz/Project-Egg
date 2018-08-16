/**
 * Created by Andreas on 15/8/2018.
 */

package starbright.com.projectegg.data.local.model.response;

import com.google.gson.annotations.SerializedName;

public class ExtendedIngredientResponse {

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("amount")
    private int mAmount;

    @SerializedName("unit")
    private String mUnit;

    @SerializedName("image")
    private String mImage;

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getAmount() {
        return mAmount;
    }

    public String getUnit() {
        return mUnit;
    }

    public String getImage() {
        return mImage;
    }
}
