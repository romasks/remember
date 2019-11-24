package com.remember.app.data.models;

import com.google.gson.annotations.SerializedName;

public class RequestSocialAuth {

    @SerializedName("email")
    private String email;
//    @SerializedName("avatarImageUrl")
//    private String avatarImageUrl;
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("provider")
    private String provider;

    public RequestSocialAuth(String email, String accessToken, String provider) {
        this.email = email;
        this.accessToken = accessToken;
        this.provider = provider;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getAvatarImage() {
//        return avatarImageUrl;
//    }

//    public void setAvatarImage(String avatarImageUrl) {
//        this.avatarImageUrl = avatarImageUrl;
//    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
