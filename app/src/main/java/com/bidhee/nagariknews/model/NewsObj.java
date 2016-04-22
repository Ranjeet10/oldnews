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
    private String introText;
    private String description;
    private String newsUrl;
    private String date;
    private String img;
    private String reportedBy;

//    public NewsObj() {
//    }


    public NewsObj(String newsType, String newsCategoryId, String newsId, String newsCategoryName, String img, String title,
                   String reportedBy, String date, String introText, String description, String newsUrl) {
        this.newsType = newsType;
        this.newsCategoryId = newsCategoryId;
        this.newsId = newsId;
        this.newsCategoryName = newsCategoryName;
        this.title = title;
        this.introText = introText;
        this.description = description;
        this.newsUrl = newsUrl;
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
        introText = source.readString();
        description = source.readString();
        newsUrl = source.readString();
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


    public String getIntroText() {
        return introText;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setIntroText(String introText) {
        this.introText = introText;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
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
        dest.writeString(introText);
        dest.writeString(description);
        dest.writeString(newsUrl);
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

        if (introText == null)
            introText = "";

        if (description == null)
            description = "";

        if (newsUrl == null)
            newsUrl = "";

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
