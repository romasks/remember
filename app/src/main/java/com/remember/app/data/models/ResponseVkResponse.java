package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseVkResponse {

    @SerializedName("response")
    @Expose
    private ResponseVk response;

    public ResponseVk getResponse() {
        return response;
    }

    public void setResponse(ResponseVk response) {
        this.response = response;
    }


}
