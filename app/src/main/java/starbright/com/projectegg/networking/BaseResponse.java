package starbright.com.projectegg.networking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Andreas on 4/8/2018.
 */

public class BaseResponse {

    @SerializedName("results")
    private List<RecipeResponse> mRecipeResponse;

    public List<RecipeResponse> getRecipesResponse() {
        return mRecipeResponse;
    }
}
