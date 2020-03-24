package com.remember.app.ui.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.remember.app.R;
import com.remember.app.data.models.RequestSearchPage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.remember.app.data.Constants.IMAGES_STATUS_APPROVED;
import static com.remember.app.data.Constants.SEARCH_ON_GRID;

public class PopupPageScreen extends PopupWindow {

    private Callback callback;
    private AutoCompleteTextView dateBeginVal;
    private AutoCompleteTextView dateEndVal;
    private TextInputLayout dateBegin;
    private TextInputLayout dateEnd;
    private String status;
    private Boolean flag;

    private DatePickerDialog.OnDateSetListener datePickerDialog;
    private Calendar dateAndTime = Calendar.getInstance();

    public PopupPageScreen(View contentView, int width, int height) {
        super(contentView, width, height);

        ConstraintLayout layout = contentView.findViewById(R.id.cont);
        Toolbar toolbar = contentView.findViewById(R.id.toolbar);
        ImageView backImg = contentView.findViewById(R.id.back);
        TextView textView = contentView.findViewById(R.id.textView2);
        AutoCompleteTextView lastName = contentView.findViewById(R.id.last_name_value);
        AutoCompleteTextView name = contentView.findViewById(R.id.first_name_value);
        AutoCompleteTextView middleName = contentView.findViewById(R.id.father_name_value);
        AutoCompleteTextView place = contentView.findViewById(R.id.live_place_value);
        AutoCompleteTextView dateBegin = contentView.findViewById(R.id.date_begin_value);
        AutoCompleteTextView dateEnd = contentView.findViewById(R.id.date_end_value);

        if (Utils.isThemeDark()) {
            toolbar.setBackgroundColor(contentView.getResources().getColor(R.color.colorPrimaryBlack));
            layout.setBackgroundColor(contentView.getResources().getColor(R.color.colorBlackDark));
            backImg.setImageResource(R.drawable.ic_back_dark_theme);

            int textColorDark = contentView.getResources().getColor(R.color.colorWhiteDark);
            textView.setTextColor(textColorDark);
            name.setTextColor(textColorDark);
            lastName.setTextColor(textColorDark);
            middleName.setTextColor(textColorDark);
            dateBegin.setTextColor(textColorDark);
            dateEnd.setTextColor(textColorDark);
            place.setTextColor(textColorDark);
        }
    }

    public void setUp(View contentView) {
        setFocusable(true);
        setOutsideTouchable(false);
        showAtLocation(contentView, Gravity.TOP, 0, 0);

        View popupView = getContentView();
        AutoCompleteTextView lastName = popupView.findViewById(R.id.last_name_value);
        AutoCompleteTextView name = popupView.findViewById(R.id.first_name_value);
        AutoCompleteTextView middleName = popupView.findViewById(R.id.father_name_value);
        AutoCompleteTextView city = popupView.findViewById(R.id.live_place_value);
        popupView.findViewById(R.id.back).setOnClickListener(v -> {
            dismiss();
        });

        dateBeginVal = popupView.findViewById(R.id.date_begin_value);
        dateEndVal = popupView.findViewById(R.id.date_end_value);

        dateBeginVal.setOnClickListener(v -> setDate(v, 1));
        dateEndVal.setOnClickListener(v -> setDate(v, 2));

        popupView.findViewById(R.id.submit).setOnClickListener(v -> {
            RequestSearchPage request = new RequestSearchPage();
            request.setName(name.getText().toString());
            request.setSecondName(lastName.getText().toString());
            request.setThirdName(middleName.getText().toString());
            request.setDateBegin(dateBeginVal.getText().toString());
            request.setDateEnd(dateEndVal.getText().toString());
            request.setCity(city.getText().toString());
            request.setStatus(status);
            request.setFlag(flag);
            callback.search(request);
            dismiss();
        });
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
        if (type == 1)
            dateBeginVal.setText(requiredDate);
        else if (type == 2)
            dateEndVal.setText(requiredDate);
    }

    public interface Callback {

        void search(RequestSearchPage requestSearchPage);

    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setSourceType(String type) {
        if (SEARCH_ON_GRID.equals(type)) {
            status = IMAGES_STATUS_APPROVED;
            flag = true;
        } /*else if (SEARCH_ON_MAIN.equals(type)) {
            status = "";
            flag = false;
        }*/
    }
}
