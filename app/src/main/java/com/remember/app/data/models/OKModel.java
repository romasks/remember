package com.remember.app.data.models;


import com.google.gson.annotations.SerializedName;


public class OKModel {

    @SerializedName("email")
    private String email;
    @SerializedName("uid")
    private String uid;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return this.uid;
    }
}