/**
 * Created by Andreas on 15/8/2018.
 */

package starbright.com.projectegg.data.local.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeDetailResponse {

    @SerializedName("id")
    private int mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("image")
    private String mImageStringUrl;

    @SerializedName("preparationMinutes")
    private int mPreparationMinutes;

    @SerializedName("cookingMinutes")
    private int mCookingMinutes;

    @SerializedName("sourceUrl")
    private String mSourceStringUrl;

    @SerializedName("sourceName")
    private String mSourceName;

    @SerializedName("analyzedInstructions")
    private InstructionResponse mAnalyzedInstructions;

    @SerializedName("extendedIngredients")
    private List<ExtendedIngredientResponse> mIngredients;

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageStringUrl() {
        return mImageStringUrl;
    }

    public int getPreparationMinutes() {
        return mPreparationMinutes;
    }

    public int getCookingMinutes() {
        return mCookingMinutes;
    }

    public String getSourceStringUrl() {
        return mSourceStringUrl;
    }

    public String getSourceName() {
        return mSourceName;
    }

    public InstructionResponse getAnalyzedInstructions() {
        return mAnalyzedInstructions;
    }

    public List<ExtendedIngredientResponse> getExtendedIngredientResponse() {
        return mIngredients;
    }
}
