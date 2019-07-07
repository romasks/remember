package com.remember.app.ui.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolderPagination extends RecyclerView.ViewHolder {

    private int mCurrentPosition;

    public BaseViewHolderPagination(View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(int position) {
        mCurrentPosition = position;
        clear();
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

}