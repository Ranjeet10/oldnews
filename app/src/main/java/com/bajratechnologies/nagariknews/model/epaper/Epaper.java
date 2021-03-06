package com.bajratechnologies.nagariknews.model.epaper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ronem on 2/29/16.
 */
public class Epaper implements Parcelable {
    private int id;
    private String media;
    private String engDate;
    private String nepDate;
    private String showDate;
    private String coverImage;
    private String pdf;


    public Epaper(Parcel parcel) {
        id = parcel.readInt();
        engDate = parcel.readString();
        nepDate = parcel.readString();
        showDate = parcel.readString();
        coverImage = parcel.readString();
        pdf = parcel.readString();
    }

    public Epaper(int id, String media, String engDate, String nepDate, String showDate, String coverImage, String pdf) {
        this.id = id;
        this.media = media;
        this.engDate = engDate;
        this.nepDate = nepDate;
        this.showDate = showDate;
        this.coverImage = coverImage;
        this.pdf = pdf;

    }

    public int getId() {
        return id;
    }

    public String getMedia() {
        return media;
    }

    public String getEngDate() {
        return engDate;
    }

    public String getNepDate() {
        return nepDate;
    }

    public String getShowDate() {
        return showDate;
    }


    public String getCoverImage() {
        return coverImage;
    }


    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(media);
        dest.writeString(engDate);
        dest.writeString(nepDate);
        dest.writeString(showDate);
        dest.writeString(coverImage);
        dest.writeString(pdf);
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
