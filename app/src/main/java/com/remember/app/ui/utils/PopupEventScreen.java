package com.remember.app.ui.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.PopupWindow;

import com.remember.app.R;
import com.remember.app.customView.CustomAutoCompleteTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PopupEventScreen extends PopupWindow {

    private Callback callback;
    private CustomAutoCompleteTextView lastName;
    private CustomAutoCompleteTextView date;

    private DatePickerDialog.OnDateSetListener dateBeginPickerDialog;
    private Calendar dateAndTime = Calendar.getInstance();

    public PopupEventScreen(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setUp(View contentView) {
        setFocusable(false);
        setOutsideTouchable(false);
        //showAtLocation(contentView, Gravity.TOP, 0, 0);
        View popupView = getContentView();
        popupView.findViewById(R.id.back).setOnClickListener(v -> {
            dismiss();
        });

        date = popupView.findViewById(R.id.date_value);
        dateBeginPickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateBegin();
        };

        lastName = popupView.findViewById(R.id.last_name);

        popupView.findViewById(R.id.submit).setOnClickListener(v -> {
            dismiss();
        });

        date.setOnClickListener(this::setDateBegin);
    }

    public void setDateBegin(View v) {
        new DatePickerDialog(v.getContext(), dateBeginPickerDialog,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDateBegin() {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String requiredDate = df.format(new Date(dateAndTime.getTimeInMillis()));
        date.setText(requiredDate);
    }

    public interface Callback {


    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}