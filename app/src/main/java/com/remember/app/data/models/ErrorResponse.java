package com.remember.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorResponse  {
    @SerializedName("ok")
    private boolean ok;
    @SerializedName("error")
    private String error = "";

    public boolean getOk() { return ok; }
    public String getError() { return error; }

}