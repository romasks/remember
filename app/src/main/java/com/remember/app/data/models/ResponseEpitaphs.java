package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseEpitaphs {

    @SerializedName("body")
    private String body;
    @SerializedName("candle")
    private int candle;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("deleted_at")
    private Object deletedAt;
    @SerializedName("id")
    private int id;
    @SerializedName("page_id")
    private int pageId;
    @SerializedName("parent_id")
    private int parentId;
    @SerializedName("settings_name")
    private Object settingsName;
    @SerializedName("settings_picture")
    private Object settingsPicture;
    @SerializedName("status")
    private String status;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("user_name")
    private String userName;

    public void setBody(String body){
        this.body = body;
    }
    public String getBody(){
        return this.body;
    }
    public void setCandle(int candle){
        this.candle = candle;
    }
    public int getCandle(){
        return this.candle;
    }
    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }
    public String getCreatedAt(){
        return this.createdAt;
    }
    public void setDeletedAt(Object deletedAt){
        this.deletedAt = deletedAt;
    }
    public Object getDeletedAt(){
        return this.deletedAt;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setPageId(int pageId){
        this.pageId = pageId;
    }
    public int getPageId(){
        return this.pageId;
    }
    public void setParentId(int parentId){
        this.parentId = parentId;
    }
    public int getParentId(){
        return this.parentId;
    }
    public void setSettingsName(Object settingsName){
        this.settingsName = settingsName;
    }
    public Object getSettingsName(){
        return this.settingsName;
    }
    public void setSettingsPicture(Object settingsPicture){
        this.settingsPicture = settingsPicture;
    }
    public Object getSettingsPicture(){
        return this.settingsPicture;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setUpdatedAt(String updatedAt){
        this.updatedAt = updatedAt;
    }
    public String getUpdatedAt(){
        return this.updatedAt;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    public int getUserId(){
        return this.userId;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
}
