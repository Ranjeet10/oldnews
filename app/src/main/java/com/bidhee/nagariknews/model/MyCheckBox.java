package com.bidhee.nagariknews.model;

/**
 * Created by ronem on 4/19/16.
 */
public class MyCheckBox {
    private String id;
    private Boolean isChecked;
    private String text;

    public MyCheckBox(String id,Boolean isChecked, String text) {
        this.id = id;
        this.isChecked = isChecked;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
