/**
 * Created by Andreas on 15/8/2018.
 */

package starbright.com.projectegg.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstructionResponse {

    @SerializedName("name")
    private String mName;

    @SerializedName("steps")
    private List<StepResponse> mStepResponse;

    public String getName() {
        return mName;
    }

    public List<StepResponse> getStepResponse() {
        return mStepResponse;
    }
}
