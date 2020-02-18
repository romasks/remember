package com.remember.app.ui.cabinet.memory_pages.add_page;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.memory_pages.place.BurialPlaceActivity;
import com.remember.app.ui.cabinet.memory_pages.place.PopupReligion;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.utils.DateUtils;
import com.remember.app.ui.utils.FileUtils;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.BURIAL_PLACE_CEMETERY;
import static com.remember.app.data.Constants.BURIAL_PLACE_CITY;
import static com.remember.app.data.Constants.BURIAL_PLACE_COORDS;
import static com.remember.app.data.Constants.BURIAL_PLACE_GRAVE;
import static com.remember.app.data.Constants.BURIAL_PLACE_LINE;
import static com.remember.app.data.Constants.BURIAL_PLACE_SECTOR;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;
import static com.remember.app.ui.utils.DateUtils.dfLocal;
import static com.remember.app.ui.utils.DateUtils.parseLocalFormat;
import static com.remember.app.ui.utils.FileUtils.saveBitmap;
import static com.remember.app.ui.utils.FileUtils.storagePermissionGranted;
import static com.remember.app.ui.utils.FileUtils.verifyStoragePermissions;
import static com.remember.app.ui.utils.ImageUtils.glideLoadInto;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoAsBitmap;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoWithError;

public class NewMemoryPageActivity extends BaseActivity implements AddPageView, PopupReligion.Callback {

    private static final String TAG = NewMemoryPageActivity.class.getSimpleName();

    @InjectPresenter
    AddPagePresenter presenter;

    @BindView(R.id.last_name)
    EditText lastName;
    @BindView(R.id.back_button)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.settings)
    ImageView settings;
    @BindView(R.id.image_layout)
    ConstraintLayout imageLayout;
    @BindView(R.id.middle_name)
    EditText middleName;
    @BindView(R.id.name)
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

    private static final int SELECT_PICTURE = 451;
    private static final int GRAVE_INFO_RESULT = 646;

    private Calendar dateAndTime = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateBeginPickerDialog;
    private DatePickerDialog.OnDateSetListener dateEndPickerDialog;
    private AddPageModel person;
    private boolean isEdit;
    private MemoryPageModel memoryPageModel;
    private ProgressDialog progressDialog;
    private File imageFile;

    @Override
    protected int getContentView() {
        return R.layout.activity_new_memory_page;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);

        title.setText(R.string.memory_page_new_header_text);
        settings.setVisibility(View.GONE);

        if (storagePermissionGranted(this) || Build.VERSION.SDK_INT < 23) {
            setUp();
        } else {
            verifyStoragePermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        setUp();
    }

    @Override
    public void onSavedPage(ResponseCemetery response) {
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra("PERSON", person);
        intent.putExtra("IMAGE", imageFile);
        intent.putExtra("ID", response.getId());
        intent.putExtra("AFTER_SAVE", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Prefs.putString(BURIAL_PLACE_COORDS, "");
        Prefs.putString(BURIAL_PLACE_CITY, "");
        Prefs.putString(BURIAL_PLACE_CEMETERY, "");
        Prefs.putString(BURIAL_PLACE_SECTOR, "");
        Prefs.putString(BURIAL_PLACE_LINE, "");
        Prefs.putString(BURIAL_PLACE_GRAVE, "");
        startActivity(intent);
        finish();
    }

    @Override
    public void onGetInfo(List<ResponseHandBook> responseHandBooks) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_city, null);

        LinearLayout layout = popupView.findViewById(R.id.lay);
        if (Utils.isThemeDark()) {
            layout.setBackgroundColor(getResources().getColor(R.color.colorBlackDark));
        }

        PopupReligion popupWindow = new PopupReligion(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setCallback(this);
        popupWindow.setUp(lastName, responseHandBooks);
    }

    @Override
    public void onEdited(MemoryPageModel memoryPageModel) {
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra("PERSON", person);
        intent.putExtra("IMAGE", imageFile);
        intent.putExtra("ID", memoryPageModel.getId());
        intent.putExtra("AFTER_SAVE", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Prefs.putString(BURIAL_PLACE_COORDS, "");
        Prefs.putString(BURIAL_PLACE_CITY, "");
        Prefs.putString(BURIAL_PLACE_CEMETERY, "");
        Prefs.putString(BURIAL_PLACE_SECTOR, "");
        Prefs.putString(BURIAL_PLACE_LINE, "");
        Prefs.putString(BURIAL_PLACE_GRAVE, "");
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(Throwable throwable) {
        Utils.showSnack(image, "Ошибка получения данных");
    }

    @Override
    public void onErrorSave(Throwable throwable) {
        Utils.showSnack(image, "Ошибка сохранения");
    }

    @Override
    public void saveItem(ResponseHandBook responseHandBook) {
        religion.setText(responseHandBook.getName());
    }

    @OnClick(R.id.image_layout)
    public void pickImage() {
        Log.d("MYLOG", "adsdasdasdadsdas");
        progressDialog = LoadingPopupUtils.showLoadingDialog(this);
        CropImage.activity()
                .setMinCropResultSize(400,400)
                .setMaxCropResultSize(7000, 7000)//TODO
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    @OnClick(R.id.place_button)
    public void toPlace() {
        Intent intent = new Intent(this, BurialPlaceActivity.class);
        intent.putExtra(BURIAL_PLACE_COORDS, Prefs.getString(BURIAL_PLACE_COORDS, ""));
        intent.putExtra(BURIAL_PLACE_CITY, Prefs.getString(BURIAL_PLACE_CITY, ""));
        intent.putExtra(BURIAL_PLACE_CEMETERY, Prefs.getString(BURIAL_PLACE_CEMETERY, ""));
        intent.putExtra(BURIAL_PLACE_SECTOR, Prefs.getString(BURIAL_PLACE_SECTOR, ""));
        intent.putExtra(BURIAL_PLACE_LINE, Prefs.getString(BURIAL_PLACE_LINE, ""));
        intent.putExtra(BURIAL_PLACE_GRAVE, Prefs.getString(BURIAL_PLACE_GRAVE, ""));
        if (isEdit) {
            intent.putExtra("MODEL", memoryPageModel);
            intent.putExtra("EDIT", true);
        }
        startActivityForResult(intent, GRAVE_INFO_RESULT);
    }

    @OnClick(R.id.save_button)
    public void savePage() {
        person.setSecondName(lastName.getText().toString());
        person.setThirdName(middleName.getText().toString());
        person.setComment(description.getText().toString());

        person.setUserId(Prefs.getString(PREFS_KEY_USER_ID, "0"));

        person.setStar(String.valueOf(isFamous.isChecked()));
        person.setFlag(String.valueOf(isPublic.isChecked()));
        person.setReligion(religion.getText().toString());
        person.setPictureData("");

        if (name.getText().toString().isEmpty()) {
            Utils.showSnack(image, "Введите имя усопшего");
            return;
        }
        person.setName(name.getText().toString());

        if (dateBegin.getText().toString().isEmpty() || dateEnd.getText().toString().isEmpty()) {
            Utils.showSnack(image, "Заполните дату рождения и смерти");
            return;
        }
        person.setBirthDate(DateUtils.convertLocalToRemoteFormat(dateBegin.getText().toString()));
        person.setDeathDate(DateUtils.convertLocalToRemoteFormat(dateEnd.getText().toString()));

        if (!isEdit) {
            if (isFamous.isChecked() && imageFile == null) {
                Utils.showSnack(image, "Необходимо загрузить фото");
            } else {
                presenter.addPage(person, imageFile);
            }
        } else {
            if (imageFile != null) {
                presenter.editPage(person, memoryPageModel.getId(), imageFile);
            } else {
                File file = new File(this.getCacheDir(), "image");
                FileUtils.saveImageFromDrawableToFile(file, image);
                presenter.editPage(person, memoryPageModel.getId(), file);
            }
        }
    }

    @OnClick(R.id.religion_value)
    public void onSelectReligiousClick() {
        presenter.getReligion();
    }

    @OnClick(R.id.back_button)
    public void onBackClick() {
        onBackPressed();
        finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GRAVE_INFO_RESULT) {
            if (resultCode == RESULT_OK) {
                person.setCoords(data.getStringExtra(BURIAL_PLACE_COORDS));
                person.setCity(data.getStringExtra(BURIAL_PLACE_CITY));
                person.setCemeteryName(data.getStringExtra(BURIAL_PLACE_CEMETERY));
                person.setSector(data.getStringExtra(BURIAL_PLACE_SECTOR));
                person.setSpotId(data.getStringExtra(BURIAL_PLACE_LINE));
                person.setGraveId(data.getStringExtra(BURIAL_PLACE_GRAVE));

                Prefs.putString(BURIAL_PLACE_COORDS, data.getStringExtra(BURIAL_PLACE_COORDS));
                Prefs.putString(BURIAL_PLACE_CITY, data.getStringExtra(BURIAL_PLACE_CITY));
                Prefs.putString(BURIAL_PLACE_CEMETERY, data.getStringExtra(BURIAL_PLACE_CEMETERY));
                Prefs.putString(BURIAL_PLACE_SECTOR, data.getStringExtra(BURIAL_PLACE_SECTOR));
                Prefs.putString(BURIAL_PLACE_LINE, data.getStringExtra(BURIAL_PLACE_LINE));
                Prefs.putString(BURIAL_PLACE_GRAVE, data.getStringExtra(BURIAL_PLACE_GRAVE));
            }
        } else if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                glideLoadInto(this, data.getData(), image);
            }
        } else if (requestCode == 1) {
            DisplayMetrics dsMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dsMetrics);
            Bitmap hah = null;
            if (data != null) {
                hah = Bitmap.createScaledBitmap(data.getParcelableExtra("bitmap"),//TODO
                        dsMetrics.widthPixels, dsMetrics.heightPixels, false);
            }
            findViewById(R.id.text_image).setVisibility(View.GONE);
            findViewById(R.id.add_white).setVisibility(View.GONE);
            imageLayout.setBackgroundColor(Color.TRANSPARENT);
            try {
                glideLoadIntoAsBitmap(this, hah, image);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Log.d("MYLOG", "YES1");
            if (resultCode == RESULT_OK) {
                progressDialog.dismiss();
                Log.d("MYLOG", "YES2");
                try {
                    Log.d("MYLOG", "YES3");
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                    imageFile = saveBitmap(bitmap);
                    progressDialog.dismiss();
                    glideLoadInto(getApplicationContext(), result.getUri(), image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.d("MYLOG", "YES4");
                Log.e(TAG, result.getError().getLocalizedMessage());
                progressDialog.dismiss();
            } else {
                Log.d("MYLOG", "YES5");
                progressDialog.dismiss();
            }
        }
    }

    @Override
    protected void setViewsInDarkTheme() {
        back.setImageResource(R.drawable.ic_back_dark_theme);
        int textColorDark = getResources().getColor(R.color.colorWhiteDark);
        name.setTextColor(textColorDark);
        lastName.setTextColor(textColorDark);
        middleName.setTextColor(textColorDark);
        dateBegin.setTextColor(textColorDark);
        dateEnd.setTextColor(textColorDark);
        religion.setTextColor(textColorDark);
        isFamous.setTextColor(textColorDark);
        isPublic.setTextColor(textColorDark);
        noPublic.setTextColor(textColorDark);
        notFamous.setTextColor(textColorDark);
        description.setBackground(getResources().getDrawable(R.drawable.edit_text_with_border_dark));
    }

    private void setUp() {
        initiate();
        person = new AddPageModel();

        isEdit = getIntent().getBooleanExtra("EDIT", false);

        if (isEdit) {
            title.setText("Редактирование");
            saveButton.setText("Сохранить изменения");
            textViewImage.setText("Изменить фотографию");
            memoryPageModel = getIntent().getParcelableExtra("PERSON");
            initEdit();
            person.setCoords(getIntent().getStringExtra(BURIAL_PLACE_COORDS));
            person.setCity(getIntent().getStringExtra(BURIAL_PLACE_CITY));
            person.setCemeteryName(getIntent().getStringExtra(BURIAL_PLACE_CEMETERY));
            person.setSector(getIntent().getStringExtra(BURIAL_PLACE_SECTOR));
            person.setSpotId(getIntent().getStringExtra(BURIAL_PLACE_LINE));
            person.setGraveId(getIntent().getStringExtra(BURIAL_PLACE_GRAVE));
//            if (person != null) {
//                if (person.getStar() != null) {
//                    if (person.getStar().equals("true")) {
//                        isFamous.setChecked(true);
//                    } else {
//                        notFamous.setChecked(false);
//                    }
//                }
//                if (person.getFlag() != null) {
//                    if (person.getFlag().equals("true")) {
//                        isPublic.setChecked(true);
//                    } else {
//                        noPublic.setChecked(false);
//                    }
//                }
//            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void initEdit() {
        lastName.setText(memoryPageModel.getSecondName());
        name.setText(memoryPageModel.getName());
        middleName.setText(memoryPageModel.getThirdName());
        description.setText(memoryPageModel.getComment());
        religion.setText(memoryPageModel.getReligiya());
//        isFamous.setChecked(memoryPageModel.getStar().equals("true"));
//        notFamous.setChecked(!memoryPageModel.getStar().equals("true"));
//        isPublic.setChecked(memoryPageModel.getFlag().equals("true"));
//        noPublic.setChecked(!memoryPageModel.getFlag().equals("true"));

        if (memoryPageModel.getStar().equals("true")) {
            isFamous.setChecked(true);
            notFamous.setChecked(false);
        } else {
            isFamous.setChecked(false);
            notFamous.setChecked(true);
        }
        if (memoryPageModel.getFlag().equals("true")) {
            isPublic.setChecked(true);
            noPublic.setChecked(false);
        } else {
            isPublic.setChecked(false);
            noPublic.setChecked(true);
        }

        dateBegin.setText(DateUtils.convertRemoteToLocalFormat(memoryPageModel.getDateBirth()));
        dateEnd.setText(DateUtils.convertRemoteToLocalFormat(memoryPageModel.getDateDeath()));

        glideLoadIntoWithError(this, BASE_SERVICE_URL + memoryPageModel.getPicture(), image);
    }

    private void initiate() {
        dateBeginPickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Date endDate = parseLocalFormat(dateEnd.getText().toString());
            if (endDate != null && dateAndTime.getTime().after(endDate)) {
                Utils.showSnack(dateBegin, getResources().getString(R.string.events_error_date_death_before_date_birth));
            } else {
                setInitialDateBegin();
            }
        };

        dateEndPickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Date startDate = parseLocalFormat(dateBegin.getText().toString());
            if (startDate != null && startDate.after(dateAndTime.getTime())) {
                Utils.showSnack(dateBegin, getResources().getString(R.string.events_error_date_death_before_date_birth));
            } else {
                setInitialDateEnd();
            }
        };

        dateBegin.setOnClickListener(this::setDateBegin);
        dateEnd.setOnClickListener(this::setDateEnd);
    }

    private void setInitialDateBegin() {
        dateBegin.setText(dfLocal.format(new Date(dateAndTime.getTimeInMillis())));
    }

    private void setInitialDateEnd() {
        dateEnd.setText(dfLocal.format(new Date(dateAndTime.getTimeInMillis())));
    }
}
