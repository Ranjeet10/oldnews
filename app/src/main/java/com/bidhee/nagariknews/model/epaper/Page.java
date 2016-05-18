package com.bidhee.nagariknews.model.epaper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ronem on 2/29/16.
 */
public class Page implements Parcelable {
    private int pageId;
    private String pageUrl;

    public Page() {
    }


    public Page(int pageId, String pageUrl) {
        this.pageId = pageId;
        this.pageUrl = pageUrl;
    }

    public Page(Parcel source) {
        pageId = source.readInt();
        pageUrl = source.readString();
    }

    public int getPageId() {
        return pageId;
    }


    public String getPageUrl() {
        return pageUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        applyDefault();
        dest.writeInt(pageId);
        dest.writeString(pageUrl);
    }

    private void applyDefault() {

        if (pageUrl == null)
            pageUrl = "";
    }

    public static Creator<Page> CREATOR = new Creator<Page>() {
        @Override
        public Page createFromParcel(Parcel source) {
            return new Page(source);
        }

        @Override
        public Page[] newArray(int size) {
            return new Page[size];
        }
    };
}
