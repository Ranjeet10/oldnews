package com.bidhee.nagariknews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ronem on 2/24/16.
 */
public class TabModel implements Parcelable {
    public String cat_id;
    public String cat_name;

    public TabModel() {
    }

    public TabModel(String cat_id, String cat_name) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
    }

    public TabModel(Parcel parcel) {
        cat_id = parcel.readString();
        cat_name = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        applyDefault();

        dest.writeString(cat_id);
        dest.writeString(cat_name);
    }

    private void applyDefault() {
        if (cat_id == null) {
            cat_id = "";
        }

        if (cat_name == null) {
            cat_name = "";
        }
    }

    public static Creator<TabModel> CREATOR = new Creator<TabModel>() {
        @Override
        public TabModel createFromParcel(Parcel source) {
            return new TabModel(source);
        }

        @Override
        public TabModel[] newArray(int size) {
            return new TabModel[size];
        }
    };
}
