package com.bidhee.nagariknews.model;

/**
 * Created by ronem on 2/19/16.
 */
public class BreakingAndLatestNewsListModel {
    private String newsTitle;
    private String newsDescription;

    public BreakingAndLatestNewsListModel(String newsTitle, String newsDescription) {
        this.newsTitle = newsTitle;
        this.newsDescription = newsDescription;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }
}
