package com.remember.app.data.models;

import androidx.paging.PagedList;

import com.google.gson.annotations.SerializedName;

public class ResponsePages {

    @SerializedName("result")
    private PagedList<MemoryPageModel> result;
    @SerializedName("count")
    private int count;
    @SerializedName("pages")
    private int pages;

    public PagedList<MemoryPageModel> getResult() {
        return result;
    }

    public void setResult(PagedList<MemoryPageModel> result) {
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
