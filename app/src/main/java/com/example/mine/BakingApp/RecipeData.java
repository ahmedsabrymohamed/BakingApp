package com.example.mine.BakingApp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class RecipeData implements Parcelable {

    public static final Parcelable.Creator<RecipeData> CREATOR = new Parcelable.Creator<RecipeData>() {
        public RecipeData createFromParcel(Parcel in) {
            return new RecipeData(in);
        }

        public RecipeData[] newArray(int size) {
            return new RecipeData[size];
        }
    };
    private final int id;
    private final String name;
    private final List<Ingredients> ingredients;
    private final List<RecipeSteps> steps;

    private RecipeData(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
        in.readTypedList(ingredients, Ingredients.CREATOR);
        in.readTypedList(steps, RecipeSteps.CREATOR);

    }

    public String getName() {
        return name;
    }

    List<Ingredients> getIngredients() {
        return ingredients;
    }

    List<RecipeSteps> getSteps() {
        return steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
    }
}
