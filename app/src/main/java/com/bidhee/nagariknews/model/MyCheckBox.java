package com.bidhee.nagariknews.model;

/**
 * Created by ronem on 4/19/16.
 */
public class MyCheckBox {
    private String id;
    private String name;
    private String alias;
    private Boolean isPreferred;


    public MyCheckBox(String id, String name, String alias, Boolean isPreferred) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.isPreferred = isPreferred;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Boolean getIsPreferred() {
        return isPreferred;
    }

    public void setIsPreferred(Boolean isPreferred) {
        this.isPreferred = isPreferred;
    }
}
