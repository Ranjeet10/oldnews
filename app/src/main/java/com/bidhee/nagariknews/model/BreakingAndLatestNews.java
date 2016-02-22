package com.bidhee.nagariknews.model;

import java.util.ArrayList;

/**
 * Created by ronem on 2/19/16.
 */
public class BreakingAndLatestNews {
    private String topic;
    private ArrayList<NewsObj> breakingAndLatestNewsListModels;

    public BreakingAndLatestNews(String topic, ArrayList<NewsObj> breakingAndLatestNewsListModels) {
        this.topic = topic;
        this.breakingAndLatestNewsListModels = breakingAndLatestNewsListModels;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<NewsObj> getBreakingAndLatestNewsListModels() {
        return breakingAndLatestNewsListModels;
    }

    public void setBreakingAndLatestNewsListModels(ArrayList<NewsObj> breakingAndLatestNewsListModels) {
        this.breakingAndLatestNewsListModels = breakingAndLatestNewsListModels;
    }
}
