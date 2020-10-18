package com.remember.app.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.remember.app.R;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PopupEventScreenLocal extends PopupWindow {

    private Callback callback;
    private MaterialSpinner spinner;
    private AutoCompleteTextView date;
    private Context context;

    private DatePickerDialog.OnDateSetListener dateBeginPickerDialog;
    private Calendar dateAndTime = Calendar.getInstance();

    private FragmentManager supportFragmentManager;

    public PopupEventScreenLocal(View contentView, int width, int height, FragmentManager supportFragmentManager) {
        super(contentView, width, height);

        this.supportFragmentManager = supportFragmentManager;
    }

    public void setContext(Context context) {
        this.context = context;
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
        /*dateBeginPickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateBegin();
        };*/

        spinner = popupView.findViewById(R.id.spinner);
        spinner.setItems(responseHandBooks);
        spinner.setSelectedIndex(7);

        popupView.findViewById(R.id.submit).setOnClickListener(v -> {
//            if (date.getText().toString().isEmpty()) {
//                Toast.makeText(context, "Выберите дату", Toast.LENGTH_SHORT).showKeyboard();
//            } else {
                if (date.getText().toString().equals("") && spinner.getSelectedIndex() == 7) {
                    Toast.makeText(context, "Выберите параметры поиска", Toast.LENGTH_SHORT).show();
                } else {
                    callback.search(date.getText().toString(), spinner.getSelectedIndex());
                    dismiss();
                }
//            }
        });
        date.setOnClickListener(this::setDateBegin);
    }

    public void setDateBegin(View v) {
        System.out.println("DATA RRRRR = " + date.getText().toString());
        /*new DatePickerDialog(v.getContext(), dateBeginPickerDialog,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();*/
        DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
                setInitialDateBegin();
                /*pickedDateTime = dateAndTime.getTimeInMillis();
                if (pickedDateTime > calendar.getTimeInMillis()) {
                    date.setText(dfLocal.format(new Date(dateAndTime.getTimeInMillis())));
                } else {
                    Utils.showSnack(date, getResources().getString(R.string.error_event_date_before_date_birth));
                }*/
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        //dialog.setMaxDate(new Date().getTime());
        dialog.setYearRange(1905, Calendar.getInstance().get(Calendar.YEAR));
        dialog.show(this.supportFragmentManager, "tag");

    }

    private void setInitialDateBegin() {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd.MM");
        String requiredDate = df.format(new Date(dateAndTime.getTimeInMillis()));
        date.setText(requiredDate);
    }

    public interface Callback {

        void search(String date, int selectedIndex);

    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
