package com.remember.app.ui.cabinet.memory_pages.add_page;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.cabinet.memory_pages.place.BurialPlaceActivity;
import com.remember.app.ui.cabinet.memory_pages.place.PopupReligion;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.MvpAppCompatActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;
import static com.remember.app.ui.utils.FileUtils.saveBitmap;
import static com.remember.app.ui.utils.FileUtils.verifyStoragePermissions;
import static com.remember.app.ui.utils.ImageUtils.glideLoadInto;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoAsBitmap;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoWithError;
import static com.remember.app.ui.utils.ImageUtils.setBlackWhite;

public class NewMemoryPageActivity extends MvpAppCompatActivity implements AddPageView, PopupReligion.Callback {

    @InjectPresenter
    AddPagePresenter presenter;

    @BindView(R.id.last_name)
//    AutoCompleteTextView lastName;
            EditText lastName;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.image_layout)
    ConstraintLayout imageLayout;
    @BindView(R.id.middle_name)
//    AutoCompleteTextView middleName;
            EditText middleName;
    @BindView(R.id.name)
//    AutoCompleteTextView name;
            EditText name;
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
    @BindView(R.id.save_button)
    Button saveButton;
    @BindView(R.id.text_image)
    TextView textViewImage;

    private Calendar dateAndTime = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateBeginPickerDialog;
    private DatePickerDialog.OnDateSetListener dateEndPickerDialog;
    private AddPageModel person;
    private String dataString = "";
    private static final int SELECT_PICTURE = 451;
    private static final int GRAVE_INFO_RESULT = 646;
    private String imageUri;
    private boolean isEdit;
    private MemoryPageModel memoryPageModel;
    private ProgressDialog progressDialog;
    private File imageFile;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memory_page);
        ButterKnife.bind(this);
        initiate();
        Intent i = getIntent();
        person = new AddPageModel();

        if (Build.VERSION.SDK_INT >= 23) {
            verifyStoragePermissions(this);
        }

        isEdit = i.getBooleanExtra("EDIT", false);

        if (isEdit) {
            saveButton.setText("Сохранить изменения");
            textViewImage.setText("Изменить фотографию");
            memoryPageModel = i.getParcelableExtra("PERSON");
            initEdit();
            person.setCity(getIntent().getExtras().getString("CITY", ""));
            person.setSpotId(getIntent().getExtras().getString("SECTOR", ""));
            person.setGraveId(getIntent().getExtras().getString("GRAVE", ""));
            person.setCemeteryName(getIntent().getExtras().getString("CRYPT", ""));
            person.setCoords(getIntent().getExtras().getString("COORD", ""));
        }

        religion.setOnClickListener(v -> {
            presenter.getReligion();
        });
        back.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        if (Build.VERSION.SDK_INT >= 23) {
            verifyStoragePermissions(this);
        }
    }

    private void initEdit() {
        try {
            lastName.setText(memoryPageModel.getSecondname());
        } catch (Exception e) {
            lastName.setText("");
        }
        name.setText(memoryPageModel.getName());
        middleName.setText(memoryPageModel.getThirtname());
        description.setText(memoryPageModel.getComment());

        DateFormat dfLocal = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat dfRemote = new SimpleDateFormat("yyyy-MM-dd");

        Date beginDate = null;
        try {
            beginDate = dfRemote.parse(memoryPageModel.getDatarod());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (beginDate != null) {
            dateBegin.setText(dfLocal.format(beginDate));
        }

        Date endDate = null;
        try {
            endDate = dfRemote.parse(memoryPageModel.getDatasmert());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (endDate != null) {
            dateEnd.setText(dfLocal.format(endDate));
        }

//        dateBegin.setText(memoryPageModel.getDatarod());
//        dateEnd.setText(memoryPageModel.getDatasmert());
        religion.setText(memoryPageModel.getReligiya());

        glideLoadIntoWithError(this, BASE_SERVICE_URL + memoryPageModel.getPicture(), image);

        setBlackWhite(image);

        if (memoryPageModel.getStar().equals("true")) {
            isFamous.setChecked(true);
        } else {
            isFamous.setChecked(false);
        }
//        if (memoryPageModel.getStatus().equals("true")){
//            isPublic.setChecked(true);
//        } else {
//            isPublic.setChecked(false);
//        }
    }

    @OnClick(R.id.image_layout)
    public void pickImage() {
        progressDialog = LoadingPopupUtils.showLoadingDialog(this);
        CropImage.activity()
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    @OnClick(R.id.place_button)
    public void toPlace() {
        Intent intent = new Intent(this, BurialPlaceActivity.class);
        if (isEdit) {
            intent.putExtra("MODEL", memoryPageModel);
            intent.putExtra("EDIT", true);
        }
        startActivityForResult(intent, GRAVE_INFO_RESULT);
    }

    @OnClick(R.id.save_button)
    public void savePage() {
        person.setSecondName(lastName.getText().toString());
        person.setName(name.getText().toString());
        person.setThirdName(middleName.getText().toString());
        person.setComment(description.getText().toString());

        DateFormat dfLocal = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date beginDate = null;
        try {
            beginDate = dfLocal.parse(dateBegin.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (beginDate != null) {
            person.setBirthDate(df.format(beginDate));
        }

        Date endDate = null;
        try {
            endDate = dfLocal.parse(dateEnd.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (endDate != null) {
            person.setDeathDate(df.format(endDate));
        }
        person.setUserId(Prefs.getString(PREFS_KEY_USER_ID, "0"));
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
        person.setPictureData(dataString);
        if (!isEdit) {
            if (imageFile != null) {
                presenter.addPage(person, imageFile);
            } else {
                Toast.makeText(this, "Необходимо загрузить фото", Toast.LENGTH_LONG).show();
            }
        } else {

            if (imageFile != null) {
                presenter.editPage(person, memoryPageModel.getId(), imageFile);
            } else {
                File file = new File(this.getCacheDir(), "image");
                try {
                    file.createNewFile();
                    image.invalidate();
                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                presenter.editPage(person, memoryPageModel.getId(), file);
            }
        }
    }

    private void initiate() {
        dateBeginPickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date endDate = null;
            try {
                if (!dateEnd.getText().toString().isEmpty()) {
                    endDate = simpleDateFormat.parse(dateEnd.getText().toString());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (endDate != null) {
                if (dateAndTime.getTime().after(endDate)) {
                    Snackbar.make(dateBegin, "Дата смерти не может быть перед датой рождения", Snackbar.LENGTH_LONG).show();
                } else {
                    setInitialDateBegin();
                }
            } else {
                setInitialDateBegin();
            }
        };

        dateEndPickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            try {
                if (!dateBegin.getText().toString().isEmpty()) {
                    startDate = simpleDateFormat.parse(dateBegin.getText().toString());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (startDate != null) {
                if (startDate.after(dateAndTime.getTime())) {
                    Snackbar.make(dateBegin, "Дата смерти не может быть перед датой рождения", Snackbar.LENGTH_LONG).show();
                } else {
                    setInitialDateEnd();
                }
            } else {
                setInitialDateEnd();
            }
        };

        dateBegin.setOnClickListener(this::setDateBegin);
        dateEnd.setOnClickListener(this::setDateEnd);
    }

    public void setDateBegin(View v) {
        DatePickerDialog dialog = new DatePickerDialog(this, dateBeginPickerDialog,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.show();
    }

    public void setDateEnd(View v) {
        DatePickerDialog dialog = new DatePickerDialog(this, dateEndPickerDialog,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.show();
    }

    private void setInitialDateBegin() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dfLocal = new SimpleDateFormat("dd.MM.yyyy");
        String requiredDate = dfLocal.format(new Date(dateAndTime.getTimeInMillis()));
        dateBegin.setText(requiredDate);
    }

    private void setInitialDateEnd() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dfLocal = new SimpleDateFormat("dd.MM.yyyy");
        String requiredDate = dfLocal.format(new Date(dateAndTime.getTimeInMillis()));
        dateEnd.setText(requiredDate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GRAVE_INFO_RESULT) {
            if (resultCode == RESULT_OK) {
                person.setCoords(data.getStringExtra("COORDS"));
                person.setCity(data.getStringExtra("CITY"));
                person.setCemeteryName(data.getStringExtra("CEMETERY"));
                person.setSpotId(data.getStringExtra("SPOT_ID"));
                person.setGraveId(data.getStringExtra("GRAVE_ID"));
                person.setSector(data.getStringExtra("SECTOR"));
            }
        } else if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                glideLoadInto(this, data.getData(), image);
                setBlackWhite(image);
            }
        } else if (requestCode == 1) {
            DisplayMetrics dsMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dsMetrics);
            Bitmap hah = null;
            if (data != null) {
                hah = Bitmap.createScaledBitmap(data.getParcelableExtra("bitmap"),
                        dsMetrics.widthPixels, dsMetrics.heightPixels, false);
            }
            findViewById(R.id.text_image).setVisibility(View.GONE);
            findViewById(R.id.add_white).setVisibility(View.GONE);
            imageLayout.setBackgroundColor(Color.TRANSPARENT);
            try {
                glideLoadIntoAsBitmap(this, hah, image);
                setBlackWhite(image);

            } catch (Exception e) {
                Log.e("dsgsd", e.getMessage());
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                progressDialog.dismiss();
                imageUri = String.valueOf(result.getUri());
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                    imageFile = saveBitmap(bitmap);
                    progressDialog.dismiss();
                    glideLoadInto(getApplicationContext(), result.getUri(), image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                progressDialog.dismiss();
            } else {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onSavedPage(ResponseCemetery response) {
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra("PERSON", person);
        intent.putExtra("IMAGE", imageFile);
        intent.putExtra("ID", response.getId());
        intent.putExtra("AFTER_SAVE", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
//    public void onEdited(ResponsePages responsePages) {
    public void onEdited(MemoryPageModel memoryPageModel) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("PERSON", person);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra("PERSON", person);
        intent.putExtra("IMAGE", imageFile);
        intent.putExtra("ID", memoryPageModel.getId());
        intent.putExtra("AFTER_SAVE", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void error(Throwable throwable) {
        Snackbar.make(image, "Ошибка сохранения", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void saveItem(ResponseHandBook responseHandBook) {
        religion.setText(responseHandBook.getName());
    }

}
