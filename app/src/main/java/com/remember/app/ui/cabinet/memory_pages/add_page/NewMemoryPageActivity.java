package com.remember.app.ui.cabinet.memory_pages.add_page;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.remember.app.R;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.cabinet.memory_pages.place.BurialPlaceActivity;
import com.remember.app.ui.cabinet.memory_pages.place.PopupMap;
import com.remember.app.ui.cabinet.memory_pages.place.PopupReligion;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewMemoryPageActivity extends MvpAppCompatActivity implements AddPageView, PopupReligion.Callback {

    @InjectPresenter
    AddPagePresenter presenter;

    @BindView(R.id.last_name)
    AutoCompleteTextView lastName;
    @BindView(R.id.middle_name)
    AutoCompleteTextView middleName;
    @BindView(R.id.name)
    AutoCompleteTextView name;
    @BindView(R.id.date_begin)
    AutoCompleteTextView dateBegin;
    @BindView(R.id.date_end)
    AutoCompleteTextView dateEnd;
    @BindView(R.id.religion_value)
    AutoCompleteTextView religion;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.is_famous)
    AppCompatRadioButton isFamous;
    @BindView(R.id.not_famous)
    AppCompatRadioButton notFamous;
    @BindView(R.id.it_public)
    AppCompatRadioButton isPublic;
    @BindView(R.id.not_public)
    AppCompatRadioButton noPublic;

    private Calendar dateAndTime = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateBeginPickerDialog;
    private DatePickerDialog.OnDateSetListener dateEndPickerDialog;
    private AddPageModel person;
    private static final int SELECT_PICTURE = 451;
    private static final int GRAVE_INFO_RESULT = 646;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memory_page);
        ButterKnife.bind(this);
        initiate();
        person = new AddPageModel();
        religion.setOnClickListener(v -> {
            presenter.getReligion();
        });
    }

    @OnClick(R.id.image_layout)
    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @OnClick(R.id.place_button)
    public void toPlace() {
        Intent intent = new Intent(this, BurialPlaceActivity.class);
        startActivityForResult(intent, GRAVE_INFO_RESULT);
    }

    @OnClick(R.id.save_button)
    public void savePage() {
        person.setSecondName(lastName.getText().toString());
        person.setName(name.getText().toString());
        person.setThirdName(middleName.getText().toString());
        person.setComment(description.getText().toString());
        person.setBirthDate(dateBegin.getText().toString());
        person.setDeathDate(dateEnd.getText().toString());
        person.setOptradio("options1");
        person.setPictureData("String");
        if (isFamous.isChecked()) {
            person.setStar("true");
        } else if (notFamous.isChecked()) {
            person.setStar("false");
        }
        if (isPublic.isChecked()) {
            person.setFlag("true");
        } else if (noPublic.isChecked()) {
            person.setFlag("false");
        }
        person.setReligion(religion.getText().toString());
        presenter.addPage(person);//TODO разобраться с id
    }

    private void initiate() {
        dateBeginPickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateBegin();
        };

        dateEndPickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateEnd();
        };

        dateBegin.setOnClickListener(this::setDateBegin);
        dateEnd.setOnClickListener(this::setDateEnd);
    }

    public void setDateBegin(View v) {
        new DatePickerDialog(this, dateBeginPickerDialog,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setDateEnd(View v) {
        new DatePickerDialog(this, dateEndPickerDialog,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDateBegin() {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String requiredDate = df.format(new Date(dateAndTime.getTimeInMillis()));
        dateBegin.setText(requiredDate);
    }

    private void setInitialDateEnd() {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String requiredDate = df.format(new Date(dateAndTime.getTimeInMillis()));
        dateEnd.setText(requiredDate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GRAVE_INFO_RESULT){
            if (resultCode == RESULT_OK) {
                person.setCoords(data.getStringExtra("COORDS"));
                person.setCity(data.getStringExtra("CITY"));
                person.setCemeteryName(data.getStringExtra("CEMETERY"));
                person.setSpotId(data.getStringExtra("SPOT_ID"));
                person.setGraveId(data.getStringExtra("GRAVE_ID"));
            }
        } else if (requestCode == SELECT_PICTURE){
            if (resultCode == RESULT_OK) {
                Glide.with(this)
                        .load(data.getData())
                        .into(image);
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                image.setColorFilter(filter);
            }
        }
    }

    @Override
    public void onSavedPage(ResponseCemetery response) {
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra("PERSON",  (Parcelable) person);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onGetedInfo(List<ResponseHandBook> responseHandBooks) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_city, null);
        PopupReligion popupWindow = new PopupReligion(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setCallback(this);
        popupWindow.setUp(lastName, responseHandBooks);
    }

    @Override
    public void saveItem(ResponseHandBook responseHandBook) {
        religion.setText(responseHandBook.getName());
    }
}
