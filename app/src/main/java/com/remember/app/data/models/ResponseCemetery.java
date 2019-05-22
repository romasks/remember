package com.remember.app.data.models;

import com.google.gson.annotations.SerializedName;

public class ResponseCemetery {

    @SerializedName("id")
    private int id;
    @SerializedName("city_id")
    private int cityId;
    @SerializedName("body")
    private String address;
    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
