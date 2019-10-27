package com.remember.app.data.models;

import android.text.SpannableString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventNotificationModel extends NotificationModelNew {

    @SerializedName("origin_date")
    @Expose
    String originDate;

    @SerializedName("next_date")
    @Expose
    String nextDate;

    @SerializedName("remain_days")
    @Expose
    int remainDays;

    @SerializedName("type")
    @Expose
    String type;

    @SerializedName("page_id")
    @Expose
    int pageId;

    @SerializedName("page_name")
    @Expose
    String pageName;

    @SerializedName("event_name")
    @Expose
    String eventName;

    @SerializedName("event_id")
    @Expose
    int eventId;

    public String getOriginDate() {
        return originDate;
    }

    public String getNextDate() {
        return nextDate;
    }

    public int getRemainDays() {
        return remainDays;
    }

    public String getType() {
        return type;
    }

    public int getPageId() {
        return pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public String getEventName() {
        return eventName;
    }


    public int getEventId() {
        return eventId;
    }
}
