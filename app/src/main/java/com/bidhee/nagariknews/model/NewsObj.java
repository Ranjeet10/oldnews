package com.bidhee.nagariknews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ronem on 2/9/16.
 */
public class NewsObj implements Parcelable {
    private String id;
    private String newsCategory;
    private String title;
    private String desc;
    private String date;
    private String img;
    private String reportedBy;

    public NewsObj() {
    }


    public NewsObj(String id, String newsCategory, String img, String title, String reportedBy, String date, String desc) {
        this.id = id;
        this.newsCategory = newsCategory;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.img = img;
        this.reportedBy = reportedBy;
    }

    public NewsObj(Parcel source) {
        id = source.readString();
        newsCategory = source.readString();
        title = source.readString();
        desc = source.readString();
        date = source.readString();
        img = source.readString();
        reportedBy = source.readString();
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

    public String getDate() {
        return date;
    }


    public String getImg() {
        return img;
    }


    public String getReportedBy() {
        return reportedBy;
    }


    public String getDesc() {
        return desc;
    }

    public String getNewsCategory() {
        return newsCategory;
    }

    public void setNewsCategory(String newsCategory) {
        this.newsCategory = newsCategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        applyDefault();

        //writing the variables to destination of parcel

        dest.writeString(id);
        dest.writeString(newsCategory);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(date);
        dest.writeString(img);
        dest.writeString(reportedBy);
    }

    private void applyDefault() {

        if (id == null)
            id = "";

        if (newsCategory == null)
            newsCategory = "";

        if (title == null)
            title = "";

        if (desc == null)
            desc = "";

        if (date == null)
            date = "";

        if (img == null)
            img = "";

        if (reportedBy == null)
            reportedBy = "";
    }

    public static Creator<NewsObj> CREATOR = new Creator<NewsObj>() {

        @Override
        public NewsObj createFromParcel(Parcel source) {
            return new NewsObj(source);
        }

        @Override
        public NewsObj[] newArray(int size) {
            return new NewsObj[size];
        }
    };


}
