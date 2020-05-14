package com.remember.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventVideos implements Serializable {

	@SerializedName("link")
	private String link;

	@SerializedName("name_link")
	private String nameLink;

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}

	public void setNameLink(String nameLink){
		this.nameLink = nameLink;
	}

	public String getNameLink(){
		return nameLink;
	}

	@Override
 	public String toString(){
		return 
			"EventVideos{" + 
			"link = '" + link + '\'' + 
			",name_link = '" + nameLink + '\'' + 
			"}";
		}
}