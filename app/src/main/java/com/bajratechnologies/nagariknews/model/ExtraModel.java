package com.bajratechnologies.nagariknews.model;

/**
 * Created by ronem on 2/21/16.
 */
public class ExtraModel {
    private String id;
    private int image;
    private String title;
    private String description;

    public ExtraModel(String id, int image, String title, String description) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
