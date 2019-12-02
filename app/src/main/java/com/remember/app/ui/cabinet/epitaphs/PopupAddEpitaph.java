package com.remember.app.ui.cabinet.epitaphs;

import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.remember.app.R;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.ui.utils.Utils;

public class PopupAddEpitaph extends PopupWindow {

    private Callback callback;
    private ResponseEpitaphs responseEpitaphs;

    PopupAddEpitaph(View contentView, int width, int height) {
        super(contentView, width, height);

        ConstraintLayout layout = contentView.findViewById(R.id.addepi);
        EditText editText = contentView.findViewById(R.id.text_epitaph);

        if (Utils.isThemeDark()) {
            editText.setBackground(contentView.getResources().getDrawable(R.drawable.edit_text_with_border_dark));
            layout.setBackgroundColor(contentView.getResources().getColor(R.color.colorBlackDark));
        }
    }

    public void setUp(View contentView, String body) {
        setFocusable(true);
        setOutsideTouchable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(5.0f);
        }
        showAtLocation(contentView, Gravity.CENTER, 0, 0);

        View popupView = getContentView();
        Button saveButton = popupView.findViewById(R.id.save_button);
        EditText text = popupView.findViewById(R.id.text_epitaph);

        if (!body.isEmpty()) {
            text.setText(body);
            saveButton.setOnClickListener(v -> {
                callback.editEpitaph(text.getText().toString(), responseEpitaphs.getId());
                dismiss();
            });
        } else {
            saveButton.setOnClickListener(v -> {
                callback.saveEpitaph(text.getText().toString());
                dismiss();
            });
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    void setModel(ResponseEpitaphs responseEpitaphs) {
        this.responseEpitaphs = responseEpitaphs;
    }

    public interface Callback {

        void saveEpitaph(String text);

        void editEpitaph(String toString, Integer id);
    }
}
