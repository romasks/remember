package com.remember.app.ui.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.PopupWindow;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.remember.app.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PopupEventScreenLocal extends PopupWindow {

    private Callback callback;
    private MaterialSpinner spinner;
    private AutoCompleteTextView date;

    private DatePickerDialog.OnDateSetListener dateBeginPickerDialog;
    private Calendar dateAndTime = Calendar.getInstance();

    public PopupEventScreenLocal(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setUp(View contentView, List<String> responseHandBooks) {
        setFocusable(false);
        setOutsideTouchable(false);
        showAtLocation(contentView, Gravity.TOP, 0, 0);
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

        spinner = popupView.findViewById(R.id.spinner);
        spinner.setItems(responseHandBooks);

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
