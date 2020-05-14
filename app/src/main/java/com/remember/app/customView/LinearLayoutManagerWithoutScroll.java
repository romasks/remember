package com.remember.app.customView;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class LinearLayoutManagerWithoutScroll  extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public LinearLayoutManagerWithoutScroll(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}