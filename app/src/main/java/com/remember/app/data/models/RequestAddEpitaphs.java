package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestAddEpitaphs {

    @SerializedName("page_id")
    @Expose
    private Integer pageId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("updated_at")
    @Expose
    private String updated;
    @SerializedName("created_at")
    @Expose
    private String created;

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

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
