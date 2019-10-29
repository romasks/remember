package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseVk {

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("screen_name")
    @Expose
    private String screenName;
    @SerializedName("sex")
    @Expose
    private Integer sex;
    @SerializedName("relation")
    @Expose
    private Integer relation;
    @SerializedName("bdate")
    @Expose
    private String bdate;
    @SerializedName("bdate_visibility")
    @Expose
    private Integer bdateVisibility;
    @SerializedName("home_town")
    @Expose
    private String homeTown;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("phone")
    @Expose
    private String phone;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public Integer getBdateVisibility() {
        return bdateVisibility;
    }

    public void setBdateVisibility(Integer bdateVisibility) {
        this.bdateVisibility = bdateVisibility;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
