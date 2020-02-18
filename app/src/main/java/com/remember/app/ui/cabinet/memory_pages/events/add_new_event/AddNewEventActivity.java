package com.remember.app.ui.cabinet.memory_pages.events.add_new_event;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.CreateEventRequest;
import com.remember.app.data.models.EditEventRequest;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.utils.DateUtils;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.OnClick;

import static android.provider.MediaStore.Images.Media.getBitmap;
import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ACCESS;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_DATE;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_DESCRIPTION;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_IMAGE_URL;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_IS_FOR_ONE;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_NAME;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_PERSON;
import static com.remember.app.data.Constants.INTENT_EXTRA_IS_EVENT_EDITING;
import static com.remember.app.data.Constants.INTENT_EXTRA_PAGE_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON_NAME;
import static com.remember.app.ui.utils.DateUtils.dfLocal;
import static com.remember.app.ui.utils.FileUtils.saveBitmap;
import static com.remember.app.ui.utils.FileUtils.storagePermissionGranted;
import static com.remember.app.ui.utils.FileUtils.verifyStoragePermissions;
import static com.remember.app.ui.utils.ImageUtils.cropImage;
import static com.remember.app.ui.utils.ImageUtils.glideLoadInto;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoAsBitmap;

public class AddNewEventActivity extends BaseActivity implements AddNewEventView {

    private final String TAG = AddNewEventActivity.class.getSimpleName();

    private static final int SELECT_PICTURE = 451;
    private Calendar dateAndTime = Calendar.getInstance();

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

    private DatePickerDialog.OnDateSetListener dateBeginPickerDialog;
    private ProgressDialog progressDialog;
    private File imageFile;
    private int pageId;
    private int eventId = 0;
    private String name;
    private String personName = "";

    @Override
    protected int getContentView() {
        return R.layout.activity_add_event;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);

        if (storagePermissionGranted(this) || Build.VERSION.SDK_INT < 23) {
            setUp();
        } else {
            verifyStoragePermissions(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        setUp();
    }

    @Override
    public void onSavedEvent(RequestAddEvent requestAddEvent) {
        onBackPressed();
    }

    @Override
    public void onEditEvent(EditEventRequest editEventRequest) {
        onBackPressed();
    }

    @Override
    public void onError(Throwable throwable) {
        Utils.showSnack(description, "Ошибка загрузки данных");
    }

    @Override
    protected void setViewsInDarkTheme() {
        backButton.setImageResource(R.drawable.ic_back_dark_theme);
        description.setBackground(getResources().getDrawable(R.drawable.edit_text_with_border_dark));
        int textColorDark = getResources().getColor(R.color.colorWhiteDark);
        forOne.setTextColor(textColorDark);
        notForOne.setTextColor(textColorDark);
        isNeedNotification.setTextColor(textColorDark);
        notNeedNotification.setTextColor(textColorDark);
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
                glideLoadIntoAsBitmap(hah, image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    imageFile = saveBitmap(getBitmap(this.getContentResolver(), result.getUri()));
                    glideLoadInto(getApplicationContext(), result.getUri(), image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            progressDialog.dismiss();
        }
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.image_layout)
    public void pickImage() {
        progressDialog = LoadingPopupUtils.showLoadingDialog(this);
        cropImage(this);
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
            if (eventId == 0) {
                CreateEventRequest createEventRequest = new CreateEventRequest();
                createEventRequest.setPageId(String.valueOf(pageId));
                createEventRequest.setName(title.getText().toString());
                createEventRequest.setDate(DateUtils.convertLocalToRemoteFormat(date.getText().toString()));
                createEventRequest.setFlag(forOne.isChecked() ? "1" : "0");
                createEventRequest.setUvShow(isNeedNotification.isChecked() ? "1" : "0");
                createEventRequest.setDescription(description.getText().toString());
                presenter.saveEvent(createEventRequest, imageFile);
            } else {
                EditEventRequest editEventRequest = new EditEventRequest();
                editEventRequest.setEventId(eventId);
                editEventRequest.setPageId(String.valueOf(pageId));
                editEventRequest.setName(title.getText().toString());
                editEventRequest.setDate(DateUtils.convertLocalToRemoteFormat(date.getText().toString()));
                editEventRequest.setFlag(forOne.isChecked() ? "0" : "1");
                editEventRequest.setUvShow(isNeedNotification.isChecked() ? "1" : "0");
                editEventRequest.setDescription(description.getText().toString());
                if (imageFile != null) {
                    presenter.editEvent(editEventRequest, imageFile);
                } else {
                    if (image.getDrawable() != null) {
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
                    } else {
                        presenter.editEvent(editEventRequest, null);
                    }
                }
            }
        }
    }

    @OnClick(R.id.date_value)
    public void onDateSelectedClick() {
        setDate();
    }

    private void setUp() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;

        eventId = bundle.getInt(INTENT_EXTRA_EVENT_ID);
        String eventDesc = bundle.getString(INTENT_EXTRA_EVENT_DESCRIPTION, "");
        String eventName = bundle.getString(INTENT_EXTRA_EVENT_NAME, "");
        String dateString = bundle.getString(INTENT_EXTRA_EVENT_DATE, "");
        String imageUrl = bundle.getString(INTENT_EXTRA_EVENT_IMAGE_URL, "");
        String access = bundle.getString(INTENT_EXTRA_EVENT_ACCESS, "0");
        String oneFlag = bundle.getString(INTENT_EXTRA_EVENT_IS_FOR_ONE, "0");

        glideLoadInto(this, BASE_SERVICE_URL + imageUrl, image);

        if (getIntent().getBooleanExtra(INTENT_EXTRA_IS_EVENT_EDITING, false)) {
            saveButton.setText(getString(R.string.change_event));
            eventHeaderName.setText(eventName);
        } else {
            saveButton.setText(getString(R.string.create_event));
        }

        try {
            name = bundle.getString(INTENT_EXTRA_PERSON_NAME, "");
            pageId = getIntent().getIntExtra(INTENT_EXTRA_PAGE_ID, 1);
            if (pageId == 1) {
                pageId = bundle.getInt(INTENT_EXTRA_PAGE_ID, 1);
            }
            personName = bundle.getString(INTENT_EXTRA_EVENT_PERSON, "");
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
        if (!eventDesc.isEmpty()) {
            description.setText(eventDesc);
        }
        if (!dateString.isEmpty()) {
            date.setText(dateString);
        }

        if (oneFlag.equals("1")) {
            forOne.setChecked(false);
            notForOne.setChecked(true);
        } else {
            forOne.setChecked(true);
            notForOne.setChecked(false);
        }

        if (access.equals("1")) {
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
            date.setText(dfLocal.format(new Date(dateAndTime.getTimeInMillis())));
        };
    }

    private void setDate() {
        new DatePickerDialog(this, dateBeginPickerDialog,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }
}
