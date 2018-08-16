package starbright.com.projectegg.data.local.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import starbright.com.projectegg.data.local.model.response.ExtendedIngredientResponse;
import starbright.com.projectegg.data.local.model.response.IngredientResponse;
import starbright.com.projectegg.util.Constants;

public class Ingredient implements Parcelable {

    private String mId;
    private String mName;
    private String mImageUrl;
    private String mUnit;
    private int mAmount;

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public Ingredient(IngredientResponse response) {
        mId = response.getId();
        mName = response.getName();
        mImageUrl = Constants.IMAGE_INGREDIENT_URL.concat(response.getImagePath());
    }

    public Ingredient(ExtendedIngredientResponse response) {
        mId = response.getId();
        mName = response.getName();
        mImageUrl = Constants.IMAGE_INGREDIENT_URL.concat(response.getImage());
        mAmount = response.getAmount();
        mUnit = response.getUnit();
    }

    public Ingredient(String ingredientName) {
        mName = ingredientName;
    }

    protected Ingredient(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mImageUrl = in.readString();
        mUnit = in.readString();
        mAmount = in.readInt();
    }

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
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String unit) {
        mUnit = unit;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mImageUrl);
        dest.writeString(mUnit);
        dest.writeInt(mAmount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return mAmount == that.mAmount &&
                Objects.equals(mId, that.mId) &&
                Objects.equals(mName, that.mName) &&
                Objects.equals(mImageUrl, that.mImageUrl) &&
                Objects.equals(mUnit, that.mUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mName, mImageUrl, mUnit, mAmount);
    }
}