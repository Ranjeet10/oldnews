package com.bidhee.nagariknews.model.epaper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronem on 2/29/16.
 */
public class Epaper implements Parcelable {
    private int id;
    private String media;
    private String date;
    private String coverImage;
    private int noOfpages;


    public Epaper(Parcel parcel) {
        id = parcel.readInt();
        date = parcel.readString();
        coverImage = parcel.readString();
        noOfpages = parcel.readInt();
    }

    public Epaper(int id, String media, String date, String coverImage, int noOfpages) {
        this.id = id;
        this.media = media;
        this.date = date;
        this.coverImage = coverImage;
        this.noOfpages = noOfpages;

    }

    public int getId() {
        return id;
    }

    public String getMedia() {
        return media;
    }

    public String getDate() {
        return date;
    }


    public String getCoverImage() {
        return coverImage;
    }


    public int getNoOfpages() {
        return noOfpages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(media);
        dest.writeString(date);
        dest.writeString(coverImage);
        dest.writeInt(noOfpages);
    }

    public static Creator<Epaper> CREATOR = new Creator<Epaper>() {
        @Override
        public Epaper createFromParcel(Parcel source) {
            return new Epaper(source);
        }

        @Override
        public Epaper[] newArray(int size) {
            return new Epaper[size];
        }
    };
}
