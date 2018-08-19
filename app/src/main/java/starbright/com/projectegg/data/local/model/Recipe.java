package starbright.com.projectegg.data.local.model;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;
import starbright.com.projectegg.data.local.model.response.ExtendedIngredientResponse;
import starbright.com.projectegg.data.local.model.response.RecipeDetailResponse;
import starbright.com.projectegg.data.local.model.response.RecipeResponse;
import starbright.com.projectegg.data.local.model.response.StepResponse;

/**
 * Created by Andreas on 4/8/2018.
 */

public class Recipe {
    private int mId;
    private int mUsedIngredientCount;
    private int mMissedIngredientCount;
    private int mLikes;
    private int mPreparationMinutes;
    private int mCookingMinutes;
    private int mServingCount;
    private String mTitle;
    private String mImage;
    @Nullable
    private String mImageType;
    @Nullable
    private String mSourceStringUrl;
    @Nullable
    private String mSourceName;
    @Nullable
    private List<Ingredient> mIngredients;
    @Nullable
    private List<Instruction> mInstruction;

    public Recipe(int id,
                  String title,
                  String image,
                  String imageType,
                  int usedIngredientCount,
                  int missedIngredientCount,
                  int likes) {
        mId = id;
        mTitle = title;
        mImage = image;
        mImageType = imageType;
        mUsedIngredientCount = usedIngredientCount;
        mMissedIngredientCount = missedIngredientCount;
        mLikes = likes;
    }

    public Recipe(RecipeResponse response) {
        mId = response.getId();
        mTitle = response.getTitle();
        mImage = response.getImage();
        mImageType = response.getImageType();
        mUsedIngredientCount = response.getUsedIngredientCount();
        mMissedIngredientCount = response.getMissedingredientCount();
        mLikes = response.getLikes();
    }

    public Recipe(RecipeDetailResponse response) {
        mId = response.getId();
        mTitle = response.getTitle();
        mImage = response.getImageStringUrl();
        mPreparationMinutes = response.getPreparationMinutes();
        mCookingMinutes = response.getCookingMinutes();
        mSourceStringUrl = response.getSourceStringUrl();
        mSourceName = response.getSourceName();
        mServingCount = response.getServings();

        final List<ExtendedIngredientResponse> extendedIngredientResponses =
                response.getExtendedIngredientResponse();
        mIngredients = new ArrayList<>(extendedIngredientResponses.size());
        for (ExtendedIngredientResponse ingredient : extendedIngredientResponses) {
            mIngredients.add(new Ingredient(ingredient));
        }

        final List<StepResponse> stepResponses = response.getAnalyzedInstructions()
                .get(0).getStepResponse();
        mInstruction = new ArrayList<>(stepResponses.size());
        for (StepResponse stepResponse : stepResponses) {
            mInstruction.add(new Instruction(stepResponse.getNumber(),
                    stepResponse.getStep()));
        }
    }


    public int getId() {
        return mId;
    }

    public int getUsedIngredientCount() {
        return mUsedIngredientCount;
    }

    public int getMissedIngredientCount() {
        return mMissedIngredientCount;
    }

    public int getLikes() {
        return mLikes;
    }

    public int getPreparationMinutes() {
        return mPreparationMinutes;
    }

    public int getCookingMinutes() {
        return mCookingMinutes;
    }

    public int getServing() {
        return mServingCount;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImage() {
        return mImage;
    }

    @Nullable
    public String getImageType() {
        return mImageType;
    }

    @Nullable
    public String getSourceStringUrl() {
        return mSourceStringUrl;
    }

    @Nullable
    public String getSourceName() {
        return mSourceName;
    }

    @Nullable
    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    @Nullable
    public List<Instruction> getInstruction() {
        return mInstruction;
    }
}
