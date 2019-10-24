package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EpitNotificationModel extends NotificationModelNew{

    @SerializedName("page_id")
    @Expose
    int pageId;

    @SerializedName("text")
    @Expose
    String text;

    @SerializedName("created_at")
    @Expose
    String createdAt;

    @SerializedName("user_id")
    @Expose
    int userId;

    @SerializedName("user_name")
    @Expose
    String userName;

    @SerializedName("page_name")
    @Expose
    String pageName;

    public int getPageId() {
        return pageId;
    }

    public String getText() {
        return text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPageName() {
        return pageName;
    }
}
