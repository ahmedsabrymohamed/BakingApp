package com.example.mine.BakingApp;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {

    static final Parcelable.Creator<Ingredients> CREATOR = new Parcelable.Creator<Ingredients>() {
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };
    private final Double quantity;
    private final String measure;
    private final String ingredient;

    private Ingredients(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    Double getQuantity() {
        return quantity;
    }

    String getMeasure() {
        return measure;
    }

    String getIngredient() {
        return ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);


    }
}
