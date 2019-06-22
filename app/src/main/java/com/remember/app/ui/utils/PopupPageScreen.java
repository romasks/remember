package com.remember.app.ui.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.PopupWindow;

import com.google.android.material.textfield.TextInputLayout;
import com.remember.app.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PopupPageScreen extends PopupWindow {

    private Callback callback;
    private AutoCompleteTextView dateBeginVal;
    private AutoCompleteTextView dateEndVal;

    private DatePickerDialog.OnDateSetListener datePickerDialog;
    private Calendar dateAndTime = Calendar.getInstance();

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



        dateBeginVal = popupView.findViewById(R.id.date_begin_value);
        dateEndVal = popupView.findViewById(R.id.date_end_value);
        dateBeginVal.setOnClickListener(v -> setDate(v,1));
        dateEndVal.setOnClickListener(v -> setDate(v,2));
    }

    public void setDate(View v, int type) {
        datePickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate(type);
        };
        new DatePickerDialog(v.getContext(), datePickerDialog,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDate(int type) {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String requiredDate = df.format(new Date(dateAndTime.getTimeInMillis()));
        if(type==1)
        dateBeginVal.setText(requiredDate);
        else if(type==2)
            dateEndVal.setText(requiredDate);
    }

    public interface Callback {

        void search(String lastName);

    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
