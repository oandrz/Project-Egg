package starbright.com.projectegg.data.local.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Andreas on 4/8/2018.
 */

public class BaseResponse {
    @SerializedName("results")
    private List<RecipeResponse> mResults;

    public List<RecipeResponse> getmResults() {
        return mResults;
    }
}
