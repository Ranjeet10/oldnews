package com.bidhee.nagariknews.model;

/**
 * Created by ronem on 4/19/16.
 */
public class MyCheckBox {
    private Boolean isChecked;
    private String text;

    public MyCheckBox(Boolean isChecked, String text) {
        this.isChecked = isChecked;
        this.text = text;
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
