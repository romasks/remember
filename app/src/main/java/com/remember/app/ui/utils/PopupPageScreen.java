package com.remember.app.ui.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.remember.app.R;

public class PopupPageScreen extends PopupWindow {

    private Callback callback;

    public PopupPageScreen(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setUp(View contentView) {
        setFocusable(false);
        setOutsideTouchable(false);
        showAtLocation(contentView, Gravity.TOP, 0, 0);
        View popupView = getContentView();
        popupView.findViewById(R.id.back).setOnClickListener(v -> {
            dismiss();
        });
        popupView.findViewById(R.id.submit).setOnClickListener(v -> {
            dismiss();
        });
    }

    public interface Callback {


    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
