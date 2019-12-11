package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventResponse {

    @SerializedName("event_id")
    @Expose
    private int eventId;

    @SerializedName("event_name")
    @Expose
    private String eventName;

    @SerializedName("picture")
    @Expose
    private String picture;

    @SerializedName("page_name")
    @Expose
    private String pageName;

    @SerializedName("page_id")
    @Expose
    private int pageId;

    @SerializedName("type")
    @Expose
    private String birth;

    @SerializedName("remain_days")
    @Expose
    private String remain_days;

    @SerializedName("next_date")
    @Expose
    private String nextDate;

    @SerializedName("origin_date")
    @Expose
    private String originDate;


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getRemain_days() {
        return remain_days;
    }

    public void setRemain_days(String remain_days) {
        this.remain_days = remain_days;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    public String getOriginDate() {
        return originDate;
    }

    public void setOriginDate(String originDate) {
        this.originDate = originDate;
    }

}
