package com.bidhee.nagariknews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ronem on 2/9/16.
 */
public class NewsObj implements Parcelable {
    private Boolean isTOShow;
    private String newsType;
    private String newsCategoryId;
    private String newsId;
    private String newsCategoryName;
    private String title;
    private String desc;
    private String date;
    private String img;
    private String reportedBy;

    public NewsObj() {
    }


    public NewsObj(String newsType, String newsCategoryId, String newsId, String newsCategoryName, String img, String title, String reportedBy, String date, String desc) {
        this.newsType = newsType;
        this.newsCategoryId = newsCategoryId;
        this.newsId = newsId;
        this.newsCategoryName = newsCategoryName;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.img = img;
        this.reportedBy = reportedBy;
    }

    public NewsObj(Parcel source) {
        newsType = source.readString();
        newsCategoryId = source.readString();
        newsId = source.readString();
        newsCategoryName = source.readString();
        title = source.readString();
        desc = source.readString();
        date = source.readString();
        img = source.readString();
        reportedBy = source.readString();
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

    public Boolean getIsTOShow() {
        return isTOShow;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getNewsCategoryId() {
        return newsCategoryId;
    }

    public void setNewsCategoryId(String newsCategoryId) {
        this.newsCategoryId = newsCategoryId;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsCategoryName() {
        return newsCategoryName;
    }

    public void setNewsCategoryName(String newsCategoryName) {
        this.newsCategoryName = newsCategoryName;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getNewsType() {
        return newsType;
    }

    public Boolean isToShow() {
        return isTOShow;
    }

    public void setIsTOShow(Boolean isTOShow) {
        this.isTOShow = isTOShow;
    }


    @Override

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        applyDefault();

        //writing the variables to destination of parcel

        dest.writeString(newsType);
        dest.writeString(newsCategoryId);
        dest.writeString(newsId);
        dest.writeString(newsCategoryName);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(date);
        dest.writeString(img);
        dest.writeString(reportedBy);
    }

    private void applyDefault() {
        if (newsType == null) {
            newsType = "1";
        }

        if (newsCategoryId == null) {
            newsCategoryId = "1";
        }
        if (newsId == null)
            newsId = "";

        if (newsCategoryName == null)
            newsCategoryName = "";

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
