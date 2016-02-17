package com.bidhee.nagariknews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ronem on 2/17/16.
 */
public class Multimedias implements Parcelable {
    private String id;
    private String title;
    private String multimediaPath;

    public Multimedias() {
    }

    public Multimedias(Parcel source) {
        id = source.readString();
        title = source.readString();
        multimediaPath = source.readString();
    }

    public Multimedias(String id, String title, String multimediaPath) {
        this.id = id;
        this.title = title;
        this.multimediaPath = multimediaPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMultimediaPath() {
        return multimediaPath;
    }

    public void setMultimediaPath(String multimediaPath) {
        this.multimediaPath = multimediaPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        applyDefaults();
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(multimediaPath);
    }

    private void applyDefaults() {
        if (id == null)
            id = "";
        if (title == null)
            title = "";
        if (multimediaPath == null)
            multimediaPath = "";
    }

    public static Creator<Multimedias> CREATOR = new Creator<Multimedias>() {
        @Override
        public Multimedias createFromParcel(Parcel source) {
            return new Multimedias(source);
        }

        @Override
        public Multimedias[] newArray(int size) {
            return new Multimedias[size];
        }
    };
}
