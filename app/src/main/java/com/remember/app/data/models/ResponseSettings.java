package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSettings {

    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thirdname")
    @Expose
    private String thirdname;
    @SerializedName("rel_id")
    @Expose
    private Integer relId;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("id_notice")
    @Expose
    private Integer idNotice;
    @SerializedName("amount_days")
    @Expose
    private Integer amountDays;
    @SerializedName("show_no")
    @Expose
    private Integer notificationsEnabled;


    public String getSurname() {
        return surname == null ? "" : surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThirdname() {
        return thirdname == null ? "" : thirdname;
    }

    public void setThirdname(String thirdname) {
        this.thirdname = thirdname;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRelId() {
        return relId;
    }

    public void setRelId(Integer relId) {
        this.relId = relId;
    }

    public String getPicture() {
        return picture == null ? "" : picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location == null || "null".equals(location) ? "" : location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getIdNotice() {
        return idNotice;
    }

    public void setIdNotice(Integer idNotice) {
        this.idNotice = idNotice;
    }

    public Integer getAmountDays() {
        return amountDays == null ? 1 : amountDays;
    }

    public void setAmountDays(Integer amountDays) {
        this.amountDays = amountDays;
    }

    public Integer getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(Integer notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}
