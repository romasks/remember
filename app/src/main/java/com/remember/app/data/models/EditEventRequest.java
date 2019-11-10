package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditEventRequest {

    @SerializedName("event_id")
    @Expose
    private int eventId;
    @SerializedName("page_id")
    @Expose
    private String pageId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("uv_show")
    @Expose
    private String uvShow;
    @SerializedName("description")
    @Expose
    private String description;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUvShow() {
        return uvShow;
    }

    public void setUvShow(String uvShow) {
        this.uvShow = uvShow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
