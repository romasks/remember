package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddPhoto {
    @SerializedName("photo_description")
    @Expose
    private String descr;

    public String getLink() {
        return descr;
    }

    public void setLink(String link) {
        this.descr = link;
    }
}
