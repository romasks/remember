package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseEvents {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("religia_id")
    @Expose
    private Integer religiaId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("putdate")
    @Expose
    private String putdate;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("flag")
    @Expose
    private Object flag;
    @SerializedName("page_id")
    @Expose
    private Integer pageId;
    @SerializedName("user_id")
    @Expose
    private Object userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReligiaId() {
        return religiaId;
    }

    public void setReligiaId(Integer religiaId) {
        this.religiaId = religiaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPutdate() {
        return putdate;
    }

    public void setPutdate(String putdate) {
        this.putdate = putdate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Object getFlag() {
        return flag;
    }

    public void setFlag(Object flag) {
        this.flag = flag;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }
}
