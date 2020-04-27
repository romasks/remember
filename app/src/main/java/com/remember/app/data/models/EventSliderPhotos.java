package com.remember.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventSliderPhotos implements Serializable {

	@SerializedName("page_id")
	private int pageId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("picture_cut")
	private String pictureCut;

	@SerializedName("body")
	private String body;

	@SerializedName("picture")
	private String picture;

	@SerializedName("status")
	private String status;

	public void setPageId(int pageId){
		this.pageId = pageId;
	}

	public int getPageId(){
		return pageId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPictureCut(String pictureCut){
		this.pictureCut = pictureCut;
	}

	public String getPictureCut(){
		return pictureCut;
	}

	public void setBody(String body){
		this.body = body;
	}

	public String getBody(){
		return body;
	}

	public void setPicture(String picture){
		this.picture = picture;
	}

	public String getPicture(){
		return picture;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"EventSliderPhotos{" + 
			"page_id = '" + pageId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",picture_cut = '" + pictureCut + '\'' + 
			",body = '" + body + '\'' + 
			",picture = '" + picture + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}