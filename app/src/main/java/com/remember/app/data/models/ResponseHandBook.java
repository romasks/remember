package com.remember.app.data.models;

import com.google.gson.annotations.SerializedName;

public class ResponseHandBook {

    @SerializedName("id")
    private int id;
    @SerializedName("region_id")
    private int regionId;
    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
