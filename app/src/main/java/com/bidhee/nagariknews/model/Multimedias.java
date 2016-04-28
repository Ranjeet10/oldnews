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

    //for video only
    private String noOfViews;
    private String date;

    public Multimedias() {
    }

    public Multimedias(Parcel source) {
        id = source.readString();
        title = source.readString();
        multimediaPath = source.readString();
        noOfViews = source.readString();
        date = source.readString();
    }

    public Multimedias(String id, String title, String multimediaPath, String noOfViews, String date) {
        this.id = id;
        this.title = title;
        this.multimediaPath = multimediaPath;
        this.noOfViews = noOfViews;
        this.date = date;
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

    public String getNoOfViews() {
        return noOfViews;
    }

    public void setNoOfViews(String noOfViews) {
        this.noOfViews = noOfViews;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
        dest.writeString(noOfViews);
        dest.writeString(date);
    }

    private void applyDefaults() {
        if (id == null)
            id = "";
        if (title == null)
            title = "";
        if (multimediaPath == null)
            multimediaPath = "";
        if (noOfViews == null)
            noOfViews = "";
        if (date == null)
            date = "";
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
