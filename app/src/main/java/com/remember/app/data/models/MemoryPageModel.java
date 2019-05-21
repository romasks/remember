package com.remember.app.data.models;

public class MemoryPageModel {

    private String dateBegin;
    private String dateEnd;
    private String fullName;
    private int amountDayforEnd;
    private String avatarUrl;

    public MemoryPageModel() {
    }

    public MemoryPageModel(String dateBegin, String dateEnd, String fullName, int amountDayforEnd, String avatarUrl) {
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.fullName = fullName;
        this.amountDayforEnd = amountDayforEnd;
        this.avatarUrl = avatarUrl;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAmountDayforEnd() {
        return amountDayforEnd;
    }

    public void setAmountDayforEnd(int amountDayforEnd) {
        this.amountDayforEnd = amountDayforEnd;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
