package com.bidhee.nagariknews.model;

/**
 * Created by ronem on 2/21/16.
 */
public class ExtraModel {
    private String id;
    private String image;
    private String title;

    public ExtraModel(String id, String image, String title) {
        this.id = id;
        this.image = image;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
