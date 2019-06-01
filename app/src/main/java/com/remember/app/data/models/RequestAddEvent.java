package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestAddEvent {

    @SerializedName("page_id")
    @Expose
    private Integer pageId;
    @SerializedName("flag")
    @Expose
    private Integer flag;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("uv_show")
    @Expose
    private String uvShow;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUvShow() {
        return uvShow;
    }

    public void setUvShow(String uvShow) {
        this.uvShow = uvShow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
