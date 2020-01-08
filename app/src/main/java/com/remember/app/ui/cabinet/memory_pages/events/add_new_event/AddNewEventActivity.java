package com.remember.app.ui.cabinet.memory_pages.events.add_new_event;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.CreateEventRequest;
import com.remember.app.data.models.EditEventRequest;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.MvpAppCompatActivity;
import com.remember.app.ui.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_DATE;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_DESCRIPTION;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_IMAGE_URL;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_NAME;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_PERSON;
import static com.remember.app.data.Constants.INTENT_EXTRA_IS_EVENT_EDITING;
import static com.remember.app.data.Constants.INTENT_EXTRA_PAGE_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON_NAME;
import static com.remember.app.ui.utils.FileUtils.saveBitmap;
import static com.remember.app.ui.utils.FileUtils.storagePermissionGranted;
import static com.remember.app.ui.utils.FileUtils.verifyStoragePermissions;
import static com.remember.app.ui.utils.ImageUtils.glideLoadInto;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoAsBitmap;

public class AddNewEventActivity extends MvpAppCompatActivity implements AddNewEventView {

    private final String TAG = AddNewEventActivity.class.getSimpleName();

    private static final int SELECT_PICTURE = 451;

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
    @BindView(R.id.not_for_one)
    AppCompatRadioButton notForOne;
    @BindView(R.id.it_notification)
    AppCompatRadioButton isNeedNotification;
    @BindView(R.id.not_notification)
    AppCompatRadioButton notNeedNotification;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.image_layout)
    ConstraintLayout imageLayout;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.save_button)
    Button saveButton;
    @BindView(R.id.eventHeaderName)
    TextView eventHeaderName;
    @BindView(R.id.back)
    ImageView backButton;

    private Unbinder unbinder;
    private String name;
    private DatePickerDialog.OnDateSetListener dateBeginPickerDialog;
    private Calendar dateAndTime = Calendar.getInstance();
    private int pageId;
    private ProgressDialog progressDialog;
    private String imageUri;
    private Bitmap bitmap;
    private File imageFile;
    private int eventId = 0;
    private String personName = "";
    private String eventName = "";
    private String eventDescription = "";
    private String imageUrl = "";
    private String dateString = "";
    private String access = "";
    private String flag = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        unbinder = ButterKnife.bind(this);

        if (Utils.isThemeDark()) {
            backButton.setImageResource(R.drawable.ic_back_dark_theme);
            description.setBackground(getResources().getDrawable(R.drawable.edit_text_with_border_dark));
            int textColorDark = getResources().getColor(R.color.colorWhiteDark);
            forOne.setTextColor(textColorDark);
            notForOne.setTextColor(textColorDark);
            isNeedNotification.setTextColor(textColorDark);
            notNeedNotification.setTextColor(textColorDark);
        }

        if (storagePermissionGranted(this) || Build.VERSION.SDK_INT < 23) {
            setUp();
        } else {
            verifyStoragePermissions(this);
        }
    }

    private void setUp() {
        eventId = getIntent().getExtras().getInt(INTENT_EXTRA_EVENT_ID);
        eventName = getIntent().getExtras().getString(INTENT_EXTRA_EVENT_NAME, "");
        eventDescription = getIntent().getExtras().getString(INTENT_EXTRA_EVENT_DESCRIPTION, "");
        imageUrl = getIntent().getExtras().getString(INTENT_EXTRA_EVENT_IMAGE_URL, "");
        dateString = getIntent().getExtras().getString(INTENT_EXTRA_EVENT_DATE, "");
        flag = getIntent().getExtras().getString("IS_FOR_ONE", "0");
        access = getIntent().getExtras().getString("ACCESS", "0");

        glideLoadInto(this, BASE_SERVICE_URL + imageUrl, image);

        if (getIntent().getBooleanExtra(INTENT_EXTRA_IS_EVENT_EDITING, false)) {
            saveButton.setText(getString(R.string.change_event));
            eventHeaderName.setText(eventName);
        } else {
            saveButton.setText(getString(R.string.create_event));
        }

        try {
            name = getIntent().getExtras().getString(INTENT_EXTRA_PERSON_NAME, "");
            pageId = getIntent().getIntExtra(INTENT_EXTRA_PAGE_ID, 1);
            if (pageId == 1) {
                pageId = getIntent().getExtras().getInt(INTENT_EXTRA_PAGE_ID, 1);
            }
            personName = getIntent().getExtras().getString(INTENT_EXTRA_EVENT_PERSON, "");
        } catch (NullPointerException ignored) {
        }
        if (!personName.isEmpty()) {
            nameDeceased.setText(personName);
        } else if (!name.isEmpty()) {
            nameDeceased.setText(name);
        }
        if (!eventName.isEmpty()) {
            title.setText(eventName);
        }
        if (!eventDescription.isEmpty()) {
            description.setText(eventDescription);
        }
        if (!dateString.isEmpty()) {
            date.setText(dateString);
        }

        if (flag.equals("1")){
            forOne.setChecked(false);
            notForOne.setChecked(true);
        } else {
            forOne.setChecked(true);
            notForOne.setChecked(false);
        }

        if (access.equals("1")){
            isNeedNotification.setChecked(true);
            notNeedNotification.setChecked(false);
        } else {
            isNeedNotification.setChecked(false);
            notNeedNotification.setChecked(true);
        }

        dateBeginPickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateBegin();
        };

        date.setOnClickListener(this::setDate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                glideLoadInto(this, data.getData(), image);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        setUp();
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
//        if (dateAndTime.after(today)) {
//            Toast.makeText(this, "Дата не может быть больше текущей", Toast.LENGTH_LONG).show();
//        } else {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String requiredDate = df.format(new Date(dateAndTime.getTimeInMillis()));
        date.setText(requiredDate);
//        }
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
        finish();
    }

    @OnClick(R.id.image_layout)
    public void pickImage() {
        progressDialog = LoadingPopupUtils.showLoadingDialog(this);
        CropImage.activity()
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    @OnClick(R.id.save_button)
    public void saveEvent() {
        if (nameDeceased.getText().toString().isEmpty()) {
            Utils.showSnack(nameDeceased, "Выберете усопшего");
        } else if (title.getText().toString().isEmpty()) {
            Utils.showSnack(nameDeceased, "Введите наименование");
        } else if (date.getText().toString().isEmpty()) {
            Utils.showSnack(nameDeceased, "Выберете дату");
        } else {
//            RequestAddEvent requestAddEvent = new RequestAddEvent();
            if (eventId == 0) {
                CreateEventRequest createEventRequest = new CreateEventRequest();
                createEventRequest.setName(title.getText().toString());
                createEventRequest.setPageId(String.valueOf(pageId));
                createEventRequest.setName(title.getText().toString());
                createEventRequest.setDescription(description.getText().toString());
                createEventRequest.setFlag(forOne.isChecked() ? "1" : "0");
                createEventRequest.setUvShow(isNeedNotification.isChecked() ? "1" : "0");
                createEventRequest.setDate(formatToServerDate(date.getText().toString()));
                if (imageFile != null) {
                    presenter.saveEvent(createEventRequest, imageFile);
                } else {
                    Toast.makeText(this, "Необходимо добавить фото", Toast.LENGTH_SHORT).show();
                }
            } else {
                EditEventRequest editEventRequest = new EditEventRequest();
                editEventRequest.setEventId(eventId);
                editEventRequest.setName(title.getText().toString());
                editEventRequest.setPageId(String.valueOf(pageId));
                editEventRequest.setName(title.getText().toString());
                editEventRequest.setDescription(description.getText().toString());
                editEventRequest.setDate(formatToServerDate(date.getText().toString()));
                editEventRequest.setFlag(forOne.isChecked() ? "1" : "0");
                editEventRequest.setUvShow(isNeedNotification.isChecked() ? "1" : "0");
                if (imageFile != null) {
                    presenter.editEvent(editEventRequest, imageFile);
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
                    presenter.editEvent(editEventRequest, file);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onSavedEvent(RequestAddEvent requestAddEvent) {
//        Intent intent = new Intent(this, EventsActivity.class);
//        intent.putExtra("NAME", name);
//        intent.putExtra("ID_PAGE", pageId);
//        startActivity(intent);
//        finish();
        onBackPressed();
    }

    @Override
    public void onEditEvent(EditEventRequest editEventRequest) {
//        Intent intent = new Intent(this, EventsActivity.class);
//        intent.putExtra("NAME", name);
//        intent.putExtra("ID_PAGE", pageId);
//        startActivity(intent);
//        finish();
        onBackPressed();
    }

    @SuppressLint("SimpleDateFormat")
    private String formatToServerDate(String date) {
        String result = "";
        try {
            Date dateResult = new SimpleDateFormat("dd.MM.yyyy").parse(date);
            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            result = formatter.format(dateResult);
        } catch (ParseException e) {
            result = date;
        }
        return result;
    }

    @Override
    public void onError(Throwable throwable) {
        Log.e(TAG, throwable.getMessage());
        Utils.showSnack(description, "Ошибка загрузки данных");
    }
}
