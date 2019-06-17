package com.remember.app.ui.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.PopupWindow;

import com.remember.app.R;

public class PopupPageScreen extends PopupWindow {

    private Callback callback;

    public PopupPageScreen(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setUp(View contentView) {
        setFocusable(true);
        setOutsideTouchable(false);
        showAtLocation(contentView, Gravity.TOP, 0, 0);
        View popupView = getContentView();
        AutoCompleteTextView lastName = popupView.findViewById(R.id.last_name_value);
        popupView.findViewById(R.id.back).setOnClickListener(v -> {
            dismiss();
        });
        popupView.findViewById(R.id.submit).setOnClickListener(v -> {
            callback.search(lastName.getText().toString());
            dismiss();
        });
    }

    public interface Callback {

        void search(String lastName);

    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
