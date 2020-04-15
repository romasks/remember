package com.remember.app.ui.utils;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.remember.app.R;
import com.remember.app.customView.CustomAutoCompleteTextView;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.RequestSearchPage;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.remember.app.data.Constants.IMAGES_STATUS_APPROVED;
import static com.remember.app.data.Constants.SEARCH_ON_GRID;

public class PopupPageScreen extends PopupWindow {

    private Callback callback;
    private CustomAutoCompleteTextView dateBeginVal;
    private CustomAutoCompleteTextView dateEndVal;
    private String status;
    private Boolean flag;
    CustomTextView textView;
    CustomAutoCompleteTextView lastName;
    CustomAutoCompleteTextView name;
    CustomAutoCompleteTextView middleName;
    CustomAutoCompleteTextView place;

    //private DatePickerDialog.OnDateSetListener datePickerDialog;
    private Calendar dateAndTime = Calendar.getInstance();

    private DatePickerFragmentDialog datePickerDialog;
    private FragmentManager fragmentManager;

    public PopupPageScreen(View contentView, int width, int height, FragmentManager fragmentManager) {
        super(contentView, width, height);

        this.fragmentManager = fragmentManager;
        ConstraintLayout layout = contentView.findViewById(R.id.cont);
        Toolbar toolbar = contentView.findViewById(R.id.toolbar);
        ImageView backImg = contentView.findViewById(R.id.back);
         textView = contentView.findViewById(R.id.textView2);
         lastName = contentView.findViewById(R.id.last_name_value);
         name = contentView.findViewById(R.id.first_name_value);
         middleName = contentView.findViewById(R.id.father_name_value);
         place = contentView.findViewById(R.id.live_place_value);
        setTheme();
        if (Utils.isThemeDark()) {
            toolbar.setBackgroundColor(contentView.getResources().getColor(R.color.colorPrimaryBlack));
            layout.setBackgroundColor(contentView.getResources().getColor(R.color.colorBlackDark));
            backImg.setImageResource(R.drawable.ic_back_dark_theme);
            int textColorDark = contentView.getResources().getColor(R.color.colorWhiteDark);
            textView.setTextColor(textColorDark);
            name.setTextColor(textColorDark);
            lastName.setTextColor(textColorDark);
            middleName.setTextColor(textColorDark);
            dateBeginVal.setTextColor(textColorDark);
            dateEndVal.setTextColor(textColorDark);
            place.setTextColor(textColorDark);
        }
    }

    public void setUp(View contentView) {
        setFocusable(true);
        setOutsideTouchable(false);
        showAtLocation(contentView, Gravity.TOP, 0, 0);

        View popupView = getContentView();
        CustomAutoCompleteTextView lastName = popupView.findViewById(R.id.last_name_value);
        CustomAutoCompleteTextView name = popupView.findViewById(R.id.first_name_value);
        CustomAutoCompleteTextView middleName = popupView.findViewById(R.id.father_name_value);
        CustomAutoCompleteTextView city = popupView.findViewById(R.id.live_place_value);
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
            request.setDateBegin(convertDate(dateBeginVal.getText().toString()));
            request.setDateEnd(convertDate(dateEndVal.getText().toString()));
            request.setCity(city.getText().toString());
            request.setStatus(status);
            request.setFlag(flag);
            callback.search(request);
            dismiss();
        });
    }

    public void setDate(View v, int type) {
        /*datePickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate(type);
        };*/
        datePickerDialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                /*pickedDateTime = dateAndTime.getTimeInMillis();
                if (pickedDateTime > calendar.getTimeInMillis()) {
                    date.setText(dfLocal.format(new Date(dateAndTime.getTimeInMillis())));
                } else {
                    Utils.showSnack(date, getResources().getString(R.string.error_event_date_before_date_birth));
                }*/
                setInitialDate(type);
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        //dialog.setMaxDate(new Date().getTime());
        datePickerDialog.setYearRange(1900, Calendar.getInstance().get(Calendar.YEAR));
        datePickerDialog.show(this.fragmentManager, "tag");
        /*new DatePickerDialog(v.getContext(), datePickerDialog,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();*/
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

    private String convertDate(String data){
        String newDate ="";
        if (!data.equals("")){
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy");
            DateTime jodatime = dtf.parseDateTime(data);
            DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy-MM-dd");
            newDate = dtfOut.print(jodatime);
        }
        return newDate;
    }

    private void setTheme() {
        ColorStateList textColor = Utils.isThemeDark()
                ? getContentView().getResources().getColorStateList(R.color.abc_dark)
                : getContentView().getResources().getColorStateList(R.color.abc_light);

        textView.setTextColor(textColor);
        name.setTextColor(textColor);
        middleName.setTextColor(textColor);
        lastName.setTextColor(textColor);
        place.setTextColor(textColor);
    }
}
