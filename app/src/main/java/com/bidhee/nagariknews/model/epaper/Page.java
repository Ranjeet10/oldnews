package com.bidhee.nagariknews.model.epaper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ronem on 2/29/16.
 */
public class Page implements Parcelable {
    private String pageNo;
    private String pageUrl;

    public Page() {
    }


    public Page(String pageNo, String pageUrl) {
        this.pageNo = pageNo;
        this.pageUrl = pageUrl;
    }

    public Page(Parcel source) {
        pageNo = source.readString();
        pageUrl = source.readString();
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        applyDefault();
        dest.writeString(pageNo);
        dest.writeString(pageUrl);
    }

    private void applyDefault() {
        if (pageNo == null)
            pageNo = "";

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
