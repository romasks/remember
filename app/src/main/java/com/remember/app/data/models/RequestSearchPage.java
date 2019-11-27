package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestSearchPage {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("")
    @Expose
    private String secondName;
    @SerializedName("thirtname")
    @Expose
    private String thirdName;
    @SerializedName("datarod")
    @Expose
    private String dateBegin;
    @SerializedName("datasmert")
    @Expose
    private String dateEnd;
    @SerializedName("gorod")
    @Expose
    private String city;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("flag")
    @Expose
    private boolean flag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
