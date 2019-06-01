package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseEpitaphs {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("page_id")
    @Expose
    private Integer pageId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("candle")
    @Expose
    private String candle;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCandle() {
        return candle;
    }

    public void setCandle(String candle) {
        this.candle = candle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
