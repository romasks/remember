package com.remember.app.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class EventModel implements Parcelable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("page_id")
    private String page_id;
    @SerializedName("date")
    private String date;
    @SerializedName("name")
    private String name;
    @SerializedName("flag")
    private String flag;
    @SerializedName("description")
    private String description;
    @SerializedName("uv_show")
    private String uv_show;
    @SerializedName("picture")
    private String picture;


    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUv_show() {
        return uv_show;
    }

    public void setUv_show(String uv_show) {
        this.uv_show = uv_show;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EventModel(){}

    public EventModel(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        page_id = in.readString();
        date = in.readString();
        name = in.readString();
        flag = in.readString();
        description = in.readString();
        uv_show = in.readString();
        picture = in.readString();
    }

    public static final Creator<EventModel> CREATOR = new Creator<EventModel>() {
        @Override
        public EventModel createFromParcel(Parcel in) {return new EventModel(in);}
        @Override
        public EventModel[] newArray(int size) {return new EventModel[size];}
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(page_id);
        dest.writeString(date);
        dest.writeString(name);
        dest.writeString(flag);
        dest.writeString(description);
        dest.writeString(uv_show);
        dest.writeString(picture);
    }
}
