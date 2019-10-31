package com.remember.app.data.models;

import android.widget.EditText;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestSettings {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("thirdname")
    @Expose
    private String middleName;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("show_no")
    @Expose
    private int enableNotifications;
    @SerializedName("id_notice")
    @Expose
    private int commemorationDays;
    @SerializedName("amount_days")
    @Expose
    private Integer amountDays;
    @SerializedName("rel_id")
    @Expose
    private Integer relId;

    public RequestSettings() {
        this.relId = 1;
    }

    public RequestSettings name(EditText name) {
        this.name = name.getText().toString();
        return this;
    }

    public RequestSettings name(String name) {
        this.name = name;
        return this;
    }

    public RequestSettings surname(EditText surname) {
        this.surname = surname.getText().toString();
        return this;
    }

    public RequestSettings surname(String surname) {
        this.surname = surname;
        return this;
    }

    public RequestSettings middleName(EditText middleName) {
        this.middleName = middleName.getText().toString();
        return this;
    }

    public RequestSettings middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public RequestSettings nickname(EditText nickname) {
        this.nickname = nickname.getText().toString();
        return this;
    }

    public RequestSettings nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public RequestSettings email(String email) {
        this.email = email;
        return this;
    }

    public RequestSettings location(EditText location) {
        this.location = location.getText().toString();
        return this;
    }

    public RequestSettings location(String location) {
        this.location = location;
        return this;
    }

    public RequestSettings phone(EditText phone) {
        this.phone = phone.getText().toString();
        return this;
    }

    public RequestSettings phone(String phone) {
        this.phone = phone;
        return this;
    }

    public RequestSettings enableNotifications(boolean enable) {
        this.enableNotifications = enable ? 1 : 0;
        return this;
    }

    public RequestSettings enableNotifications(int enableValue) {
        this.enableNotifications = enableValue;
        return this;
    }

    public RequestSettings commemorationDays(int id_notice) {
        this.commemorationDays = id_notice;
        return this;
    }

    public RequestSettings amountDays(Integer days) {
        this.amountDays = days;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getEnableNotifications() {
        return enableNotifications;
    }

    public void setEnableNotifications(int enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public int getCommemorationDays() {
        return commemorationDays;
    }

    public void setCommemorationDays(int commemorationDays) {
        this.commemorationDays = commemorationDays;
    }

    public Integer getAmountDays() {
        return amountDays;
    }

    public void setAmountDays(Integer amountDays) {
        this.amountDays = amountDays;
    }
}
