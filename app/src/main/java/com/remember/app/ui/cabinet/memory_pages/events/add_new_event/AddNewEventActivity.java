package com.remember.app.ui.cabinet.memory_pages.events.add_new_event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.customView.CustomAutoCompleteTextView;
import com.remember.app.customView.CustomButton;
import com.remember.app.customView.CustomEditTextFrame;
import com.remember.app.customView.CustomRadioButton;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.CreateEventRequest;
import com.remember.app.data.models.EditEventRequest;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.memory_pages.events.EventsActivity;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.CurrentEvent;
import com.remember.app.ui.utils.DateUtils;
import com.remember.app.ui.utils.DeleteEvent;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.Utils;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static android.provider.MediaStore.Images.Media.getBitmap;
import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.BIRTH_DATE;
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
import static com.remember.app.ui.utils.DateUtils.parseLocalFormat;
import static com.remember.app.ui.utils.FileUtils.saveBitmap;
import static com.remember.app.ui.utils.FileUtils.storagePermissionGranted;
import static com.remember.app.ui.utils.FileUtils.verifyStoragePermissions;
import static com.remember.app.ui.utils.ImageUtils.cropImage;
import static com.remember.app.ui.utils.ImageUtils.glideLoadInto;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoAsBitmap;

public class AddNewEventActivity extends BaseActivity implements AddNewEventView, DeleteEvent.Callback {

    private final String TAG = AddNewEventActivity.class.getSimpleName();

    private static final int SELECT_PICTURE = 451;
    private Calendar dateAndTime = Calendar.getInstance();
    @InjectPresenter
    AddNewEventPresenter presenter;

    @BindView(R.id.deceased_value)
    CustomAutoCompleteTextView nameDeceased;
    @BindView(R.id.title_value)
    CustomAutoCompleteTextView title;
    @BindView(R.id.date_value)
    CustomAutoCompleteTextView date;
    @BindView(R.id.for_one)
    CustomRadioButton forOne;
    @BindView(R.id.not_for_one)
    CustomRadioButton notForOne;
    @BindView(R.id.it_notification)
    CustomRadioButton isNeedNotification;
    @BindView(R.id.not_notification)
    CustomRadioButton notNeedNotification;
    @BindView(R.id.description)
    CustomEditTextFrame description;
    @BindView(R.id.image_layout)
    ConstraintLayout imageLayout;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.save_button)
    CustomButton saveButton;
    @BindView(R.id.eventHeaderName)
    CustomTextView eventHeaderName;
    @BindView(R.id.back)
    ImageView backButton;
    @BindView(R.id.settings)
    ImageView settings;

    private DatePickerDialog.OnDateSetListener dateBeginPickerDialog;
    private ProgressDialog progressDialog;
    private File imageFile;
    private int pageId;
    private int eventId = 0;
    private String name;
    private String personName = "";
    private String birthDate = "";
    private long pickedDateTime = 0;
    private Calendar calendar = Calendar.getInstance();

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
    public void onDeleteEvent(Object response) {
        openStartActivity();


    }

    @Override
    public void onDeleteEventError(Throwable throwable) {
        Utils.showSnack(image, "Ошибка удаления, попробуйте позже");
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
            Utils.showSnack(nameDeceased, "Выберите усопшего");
        } else if (title.getText().toString().isEmpty()) {
            Utils.showSnack(nameDeceased, "Введите наименование");
        } else {
            if (eventId == 0) {
                CreateEventRequest createEventRequest = new CreateEventRequest();
                createEventRequest.setPageId(String.valueOf(pageId));
                createEventRequest.setName(title.getText().toString());
                if (date.getText().toString().isEmpty()) {
                    createEventRequest.setDate(DateUtils.convertLocalToRemoteFormat(date.getText().toString()));
                }
                createEventRequest.setFlag(forOne.isChecked() ? "1" : "0");
                createEventRequest.setUvShow(isNeedNotification.isChecked() ? "1" : "0");
                createEventRequest.setDescription(description.getInputText().getText().toString());
                presenter.saveEvent(createEventRequest, imageFile);
            } else {
                EditEventRequest editEventRequest = new EditEventRequest();
                editEventRequest.setEventId(eventId);
                editEventRequest.setPageId(String.valueOf(pageId));
                editEventRequest.setName(title.getText().toString());
                if (date.getText().toString().isEmpty()) {
                    editEventRequest.setDate(DateUtils.convertLocalToRemoteFormat(date.getText().toString()));
                }
                editEventRequest.setFlag(forOne.isChecked() ? "0" : "1");
                editEventRequest.setUvShow(isNeedNotification.isChecked() ? "1" : "0");
                editEventRequest.setDescription(description.getInputText().getText().toString());
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
            eventHeaderName.setText("Редактирование");
        } else {
            settings.setVisibility(View.GONE);
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
        birthDate = getIntent().getStringExtra(BIRTH_DATE);
        if (!personName.isEmpty()) {
            nameDeceased.setText(personName);
        } else if (!name.isEmpty()) {
            nameDeceased.setText(name);
        }
        if (!eventName.isEmpty()) {
            title.setText(eventName);
        }
        if (!eventDesc.isEmpty()) {
            description.setInputText(eventDesc);
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
        setTheme();
    }

    private void setDate() {
        /*new DatePickerDialog(this, dateBeginPickerDialog,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();*/
        Log.d("myLog", "here" + birthDate);
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            calendar.setTime(Objects.requireNonNull(df.parse(birthDate)));
            Log.d("myLog", "birthDate = " + df.parse(birthDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String localDate = date.getText().toString();
        if(localDate.length() == 0) {
            DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                    dateAndTime.set(Calendar.YEAR, year);
                    dateAndTime.set(Calendar.MONTH, monthOfYear);
                    dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    pickedDateTime = dateAndTime.getTimeInMillis();
                    Log.d("myLog", "pickedtime = " + dateAndTime.getTimeInMillis());
                    Log.d("myLog", "calendar time = " + calendar.getTime());
                    if (pickedDateTime > calendar.getTimeInMillis()) {
                        date.setText(dfLocal.format(new Date(dateAndTime.getTimeInMillis())));
                    } else {
                        Utils.showSnack(date, getResources().getString(R.string.error_event_date_before_date_birth));
                    }
                }
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            dialog.setMaxDate(new Date().getTime());
            dialog.setYearRange(1800, Calendar.getInstance().get(Calendar.YEAR));
            dialog.show(getSupportFragmentManager(), "tag");
        } else {
            int day3 = Integer.parseInt(localDate.substring(0,2));
            int month3 = Integer.parseInt(localDate.substring(3,5));
            int year3 = Integer.parseInt(localDate.substring(6,10));
            DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                    dateAndTime.set(Calendar.YEAR, year);
                    dateAndTime.set(Calendar.MONTH, monthOfYear);
                    dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    pickedDateTime = dateAndTime.getTimeInMillis();
                    Log.d("myLog", "pickedtime = " + dateAndTime.getTimeInMillis());
                    Log.d("myLog", "calendar time = " + calendar.getTime());
                    if (pickedDateTime > calendar.getTimeInMillis()) {
                        date.setText(dfLocal.format(new Date(dateAndTime.getTimeInMillis())));
                    } else {
                        Utils.showSnack(date, getResources().getString(R.string.error_event_date_before_date_birth));
                    }
                }
            }, year3, month3-1, day3);
            dialog.setMaxDate(new Date().getTime());
            dialog.setYearRange(1800, Calendar.getInstance().get(Calendar.YEAR));
            dialog.show(getSupportFragmentManager(), "tag");
        }
    }
        /*DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                pickedDateTime = dateAndTime.getTimeInMillis();
                Log.d("myLog", "pickedtime = " + dateAndTime.getTimeInMillis());
                Log.d("myLog", "calendar time = " + calendar.getTime());
                if (pickedDateTime > calendar.getTimeInMillis()) {
                    date.setText(dfLocal.format(new Date(dateAndTime.getTimeInMillis())));
                } else {
                    Utils.showSnack(date, getResources().getString(R.string.error_event_date_before_date_birth));
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dialog.setMaxDate(new Date().getTime());
        dialog.show(getSupportFragmentManager(), "tag");
    }*/

    @OnClick(R.id.settings)
    public void deleteEvent() {
        showDeleteDialog();
    }

    public void showDeleteDialog() {
        DeleteEvent myDialogFragment = new DeleteEvent();
        myDialogFragment.setCallback(this);
        myDialogFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }

    @Override
    public void onDeleteEvent() {
        presenter.deleteEvent(eventId);

    }

    private void openStartActivity() {
        Toast.makeText(getApplicationContext(), "Событие удалено", Toast.LENGTH_SHORT).show();
        CurrentEvent.getInstance().finish();
        EventsActivity.getInstance().finish();
        Intent intent = new Intent(this, EventsActivity.class);
        intent.putExtra(INTENT_EXTRA_PAGE_ID, pageId);
        startActivity(intent);
        finish();
    }

    private void setTheme() {
        ColorStateList textColor = Utils.isThemeDark()
                ? getResources().getColorStateList(R.color.abc_dark)
                : getResources().getColorStateList(R.color.abc_light);
        nameDeceased.setTextColor(textColor);
        title.setTextColor(textColor);
        date.setTextColor(textColor);
    }
}
