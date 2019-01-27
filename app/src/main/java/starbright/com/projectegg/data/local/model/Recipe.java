/**
 * Created by Andreas on 7/10/2018.
 */

/**
 * Created by Andreas on 29/9/2018.
 */

/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.data.local.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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

@Entity(tableName = "recipe")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "usedIngredientCount")
    private int mUsedIngredientCount;

    @ColumnInfo(name = "missedIngredientCounts")
    private int mMissedIngredientCount;

    @ColumnInfo(name = "likes")
    private int mLikes;

    @ColumnInfo(name = "preparationMinutes")
    private int mPreparationMinutes;

    @ColumnInfo(name = "cookingMinutes")
    private int mCookingMinutes;

    @ColumnInfo(name = "servingCounts")
    private int mServingCount;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "image")
    private String mImage;

    @Nullable
    @ColumnInfo(name = "imageType")
    private String mImageType;

    @Nullable
    @ColumnInfo(name = "sourcesStringUrl")
    private String mSourceStringUrl;

    @Nullable
    @ColumnInfo(name = "sourceName")
    private String mSourceName;

    @Nullable
    @ColumnInfo(name = "ingredients")
    private List<Ingredient> mIngredients;

    @Nullable
    @ColumnInfo(name = "instructions")
    private List<Instruction> mInstructions;

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
        mUsedIngredientCount = response.getUsedIngredientsCount();
        mMissedIngredientCount = response.getMissedIngredientsCount();
        mLikes = response.getLikes();
    }

    public Recipe(RecipeDetailResponse response) {
        mId = response.getId();
        mTitle = response.getTitle();
        mImage = response.getImage();
        mPreparationMinutes = response.getPreparationMinute();
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
                .get(0).getSteps();
        mInstructions = new ArrayList<>(stepResponses.size());
        for (StepResponse stepResponse : stepResponses) {
            mInstructions.add(new Instruction(stepResponse.getNumber(),
                    stepResponse.getStep()));
        }
    }


    public int getUid() {
        return uid;
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

    public int getServingCount() {
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
    public List<Instruction> getInstructions() {
        return mInstructions;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setUsedIngredientCount(int usedIngredientCount) {
        mUsedIngredientCount = mUsedIngredientCount;
    }

    public void setMissedIngredientCount(int missedIngredientCount) {
        mMissedIngredientCount = mMissedIngredientCount;
    }

    public void setLikes(int likes) {
        mLikes = likes;
    }

    public void setPreparationMinutes(int preparationMinutes) {
        mPreparationMinutes = preparationMinutes;
    }

    public void setCookingMinutes(int cookingMinutes) {
        mCookingMinutes = cookingMinutes;
    }

    public void setServingCount(int servingCount) {
        mServingCount = servingCount;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public void setImageType(String imageType) {
        mImageType = imageType;
    }

    public void setSourceStringUrl(String sourceStringUrl) {
        mSourceStringUrl = sourceStringUrl;
    }

    public void setSourceName(String sourceName) {
        mSourceName = sourceName;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public void setInstructions(List<Instruction> instructions) {
        mInstructions = instructions;
    }
}
