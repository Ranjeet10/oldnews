package com.bidhee.nagariknews.model.epaper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronem on 2/29/16.
 */
public class Epaper implements Parcelable {
    private String date;
    private String mainPageUrl;
    private List<Page> pages;

    public Epaper(Parcel parcel) {
        date = parcel.readString();
        mainPageUrl = parcel.readString();
        pages = new ArrayList<>();
        parcel.readTypedList(pages, Page.CREATOR);
    }

    public Epaper(String date, String mainPageUrl, List<Page> pages) {
        this.date = date;
        this.mainPageUrl = mainPageUrl;
        this.pages = pages;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMainPageUrl() {
        return mainPageUrl;
    }

    public void setMainPageUrl(String mainPageUrl) {
        this.mainPageUrl = mainPageUrl;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(mainPageUrl);
        dest.writeTypedList(pages);
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
