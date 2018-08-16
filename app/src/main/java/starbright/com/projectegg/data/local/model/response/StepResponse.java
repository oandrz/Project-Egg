/**
 * Created by Andreas on 15/8/2018.
 */

package starbright.com.projectegg.data.local.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StepResponse {

    @SerializedName("number")
    private String mNumber;

    @SerializedName("step")
    private String mStep;

    @SerializedName("ingredients")
    private List<IngredientResponse> mIngredients;

    public String getNumber() {
        return mNumber;
    }

    public String getStep() {
        return mStep;
    }

    public List<IngredientResponse> getIngredients() {
        return mIngredients;
    }
}
