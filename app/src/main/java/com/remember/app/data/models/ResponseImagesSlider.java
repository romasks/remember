package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseImagesSlider {

    @Expose
    @SerializedName("updated_at")
    private String updatedAt;
    @Expose
    @SerializedName("created_at")
    private String createdAt;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("picture_cut")
    private String pictureCut;
    @Expose
    @SerializedName("picture")
    private String picture;
    @Expose
    @SerializedName("body")
    private String body;
    @Expose
    @SerializedName("page_id")
    private int pageId;
    @Expose
    @SerializedName("user_id")
    private int userId;
    @Expose
    @SerializedName("id")
    private int id;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPictureCut() {
        return pictureCut;
    }

    public void setPictureCut(String pictureCut) {
        this.pictureCut = pictureCut;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
