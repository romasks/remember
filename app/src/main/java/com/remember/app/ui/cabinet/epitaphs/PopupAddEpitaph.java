package com.remember.app.ui.cabinet.epitaphs;

import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.remember.app.R;

public class PopupAddEpitaph extends PopupWindow {

    private Callback callback;

    public PopupAddEpitaph(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setUp(View contentView) {
        setFocusable(true);
        setOutsideTouchable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(5.0f);
        }
        showAtLocation(contentView, Gravity.CENTER, 0, 0);
        View popupView = getContentView();
        Button saveButton = popupView.findViewById(R.id.save_button);
        EditText text = popupView.findViewById(R.id.text_epitaph);
        saveButton.setOnClickListener(v -> {
            callback.saveEpitaph(text.getText().toString());
            dismiss();
        });
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void saveEpitaph(String text);

    }
}
