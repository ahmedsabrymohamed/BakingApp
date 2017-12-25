package com.example.mine.BakingApp;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeSteps implements Parcelable {

    static final Parcelable.Creator<RecipeSteps> CREATOR = new Parcelable.Creator<RecipeSteps>() {
        public RecipeSteps createFromParcel(Parcel in) {
            return new RecipeSteps(in);
        }

        public RecipeSteps[] newArray(int size) {
            return new RecipeSteps[size];
        }
    };
    private final int id;
    private final String shortDescription;
    private final String description;
    private final String videoURL;
    private final String thumbnailURL;

    private RecipeSteps(Parcel in) {

        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public int getId() {
        return id;
    }

    String getShortDescription() {
        return shortDescription;
    }

    String getDescription() {
        return description;
    }

    String getThumbnailURL(){return thumbnailURL;}
    String getVideoURL() {
        return videoURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }
}
