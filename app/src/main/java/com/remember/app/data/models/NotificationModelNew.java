package com.remember.app.data.models;

import android.text.SpannableString;

public class NotificationModelNew {

    private SpannableString displayedText;
    private String displayedTime;

    public SpannableString getDisplayedText() {
        return displayedText;
    }

    public void setDisplayedText(SpannableString displayedText) {
        this.displayedText = displayedText;
    }

    public String getDisplayedDate() {
        return displayedTime;
    }

    public void setDisplayedDate(String displayedTime) {
        this.displayedTime = displayedTime;
    }
}
