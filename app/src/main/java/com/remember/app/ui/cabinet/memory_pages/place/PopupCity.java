package com.remember.app.ui.cabinet.memory_pages.place;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.remember.app.R;

public class PopupCity extends PopupWindow {

    private Button dismiss;

    PopupCity(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setUp(View contentView) {
        setFocusable(true);
        setOutsideTouchable(true);
        showAtLocation(contentView, Gravity.TOP, 0, 0);
        View popupView = getContentView();
        dismiss = popupView.findViewById(R.id.dismiss);


        dismiss.setOnClickListener(v -> {

        });

    }
}
