package com.remember.app.ui.cabinet.memory_pages.events.add_new_event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatRadioButton;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.remember.app.R;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.ui.cabinet.memory_pages.events.EventsActivity;
import com.remember.app.ui.utils.MvpAppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddNewEventActivity extends MvpAppCompatActivity implements AddNewEventView {

    @InjectPresenter
    AddNewEventPresenter presenter;

    @BindView(R.id.deceased_value)
    AutoCompleteTextView nameDeceased;
    @BindView(R.id.title_value)
    AutoCompleteTextView title;
    @BindView(R.id.date_value)
    AutoCompleteTextView date;
    @BindView(R.id.for_one)
    AppCompatRadioButton forOne;
    @BindView(R.id.it_notification)
    AppCompatRadioButton isNeedNotification;
    @BindView(R.id.description)
    EditText description;

    private Unbinder unbinder;
    private String name;
    private DatePickerDialog.OnDateSetListener dateBeginPickerDialog;
    private Calendar dateAndTime = Calendar.getInstance();
    private int pageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        unbinder = ButterKnife.bind(this);

        try {
            name = getIntent().getExtras().getString("NAME", "");
            pageId = getIntent().getIntExtra("ID_PAGE", 1);
        } catch (NullPointerException ignored) {
        }
        nameDeceased.setText(name);

        dateBeginPickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateBegin();
        };

        date.setOnClickListener(this::setDate);
    }

    private void setDate(View view) {
        new DatePickerDialog(this, dateBeginPickerDialog,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDateBegin() {
        Calendar today = Calendar.getInstance();
        if (dateAndTime.after(today)) {
            Toast.makeText(this, "Дата не может быть больше текущей", Toast.LENGTH_LONG).show();
        } else {
            @SuppressLint("SimpleDateFormat")
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            String requiredDate = df.format(new Date(dateAndTime.getTimeInMillis()));
            date.setText(requiredDate);
        }
    }

    @OnClick(R.id.save_button)
    public void saveEvent() {
        if (nameDeceased.getText().toString().equals("")) {
            Snackbar.make(nameDeceased, "Выберете усопшего", Snackbar.LENGTH_LONG).show();
        } else if (title.getText().toString().equals("")) {
            Snackbar.make(nameDeceased, "Введите наименование", Snackbar.LENGTH_LONG).show();
        } else if (date.getText().toString().equals("")) {
            Snackbar.make(nameDeceased, "Выберете дату", Snackbar.LENGTH_LONG).show();
        } else {
            RequestAddEvent requestAddEvent = new RequestAddEvent();
            requestAddEvent.setName(title.getText().toString());
            requestAddEvent.setPageId(pageId);
            requestAddEvent.setUvShow("");
            requestAddEvent.setName(title.getText().toString());
            requestAddEvent.setDescription(description.getText().toString());
            if (forOne.isChecked()) {
                requestAddEvent.setFlag(1);
            } else {
                requestAddEvent.setFlag(0);
            }
            if (isNeedNotification.isChecked()) {
                requestAddEvent.setUvShow("1");
            } else {
                requestAddEvent.setUvShow("0");
            }
            requestAddEvent.setDate(date.getText().toString());
            presenter.saveEvent(requestAddEvent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onSavedEvent(RequestAddEvent requestAddEvent) {
        Intent intent = new Intent(this, EventsActivity.class);
        intent.putExtra("NAME", name);
        intent.putExtra("ID_PAGE", pageId);
        startActivity(intent);
        finish();
    }
}
