package com.remember.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePages {

    @SerializedName("result")
    private List<MemoryPageModel> result;
    @SerializedName("count")
    private int count;
    @SerializedName("pages")
    private int pages;

    public List<MemoryPageModel> getResult() {
        return result;
    }

    public void setResult(List<MemoryPageModel> result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
