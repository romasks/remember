package com.remember.app.data.models;

import com.google.gson.annotations.SerializedName;

public class PageEditedResponse {

    @SerializedName("result")
    private boolean isResultOk;

    public boolean isResultOk() {
        return isResultOk;
    }

    public void setResultOk(boolean resultOk) {
        isResultOk = resultOk;
    }
}
