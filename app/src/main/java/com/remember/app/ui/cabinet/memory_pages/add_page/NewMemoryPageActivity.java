package com.remember.app.ui.cabinet.memory_pages.add_page;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomAutoCompleteTextView;
import com.remember.app.customView.CustomButton;
import com.remember.app.customView.CustomEditText;
import com.remember.app.customView.CustomEditTextFrame;
import com.remember.app.customView.CustomRadioButton;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.cabinet.memory_pages.place.BurialPlaceActivity;
import com.remember.app.ui.cabinet.memory_pages.place.PopupReligion;
import com.remember.app.utils.DateUtils;
import com.remember.app.utils.DeletePageDialog;
import com.remember.app.utils.FileUtils;
import com.remember.app.utils.LoadingPopupUtils;
import com.remember.app.utils.Utils;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static android.provider.MediaStore.Images.Media.getBitmap;
import static com.remember.app.data.Constants.BASE_URL_FROM_PHOTO;
import static com.remember.app.data.Constants.BURIAL_PLACE_CEMETERY;
import static com.remember.app.data.Constants.BURIAL_PLACE_CITY;
import static com.remember.app.data.Constants.BURIAL_PLACE_COORDS;
import static com.remember.app.data.Constants.BURIAL_PLACE_GRAVE;
import static com.remember.app.data.Constants.BURIAL_PLACE_LINE;
import static com.remember.app.data.Constants.BURIAL_PLACE_SECTOR;
import static com.remember.app.data.Constants.INTENT_EXTRA_AFTER_SAVE;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;
import static com.remember.app.utils.DateUtils.dfLocal;
import static com.remember.app.utils.DateUtils.parseLocalFormat;
import static com.remember.app.utils.FileUtils.saveBitmap;
import static com.remember.app.utils.FileUtils.storagePermissionGranted;
import static com.remember.app.utils.FileUtils.verifyStoragePermissions;
import static com.remember.app.utils.ImageUtils.cropImage;
import static com.remember.app.utils.ImageUtils.glideLoadInto;
import static com.remember.app.utils.ImageUtils.glideLoadIntoAsBitmap;
import static com.remember.app.utils.ImageUtils.glideLoadIntoWithError;


public class NewMemoryPageActivity extends BaseActivity implements AddPageView, PopupReligion.Callback, DeletePageDialog.Callback {

    private static final int SELECT_PICTURE = 451;
    private static final int GRAVE_INFO_RESULT = 646;
    private Calendar dateAndTime = Calendar.getInstance();
    @InjectPresenter
    AddPagePresenter presenter;

    @BindView(R.id.last_name)
    CustomEditText lastName;
    @BindView(R.id.back_button)
    ImageView back;
    @BindView(R.id.tvTitle)
    CustomTextView title;
    @BindView(R.id.settings)
    ImageView settings;
    @BindView(R.id.image_layout)
    ConstraintLayout imageLayout;
    @BindView(R.id.middle_name)
    CustomEditText middleName;
    @BindView(R.id.name)
    CustomEditText name;
    @BindView(R.id.date_begin)
    CustomAutoCompleteTextView dateBegin;
    @BindView(R.id.date_end)
    CustomAutoCompleteTextView dateEnd;
    @BindView(R.id.religion_value)
    CustomAutoCompleteTextView religion;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.description)
    CustomEditTextFrame description;
    @BindView(R.id.is_famous)
    CustomRadioButton isFamous;
    @BindView(R.id.not_famous)
    CustomRadioButton notFamous;
    @BindView(R.id.it_public)
    CustomRadioButton isPublic;
    @BindView(R.id.not_public)
    CustomRadioButton noPublic;
    @BindView(R.id.save_button)
    CustomButton saveButton;
    @BindView(R.id.text_image)
    CustomTextView textViewImage;

    private ProgressDialog progressDialog;
    private MemoryPageModel memoryPageModel;
    private AddPageModel person;
    private File imageFile;
    private boolean isEdit;
    public long startDate = 0;
    public long endDate = 0;
    boolean afterSave = false;

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
        intent.putExtra(INTENT_EXTRA_AFTER_SAVE, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        Prefs.putString(BURIAL_PLACE_COORDS, "");
//        Prefs.putString(BURIAL_PLACE_CITY, "");
//        Prefs.putString(BURIAL_PLACE_CEMETERY, "");
//        Prefs.putString(BURIAL_PLACE_SECTOR, "");
//        Prefs.putString(BURIAL_PLACE_LINE, "");
//        Prefs.putString(BURIAL_PLACE_GRAVE, "");
        startActivity(intent);
        finish();
    }

    @Override
    public void onGetInfo(List<ResponseHandBook> responseHandBooks) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_city, null);

        ConstraintLayout layout = popupView.findViewById(R.id.lay);
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
        intent.putExtra(INTENT_EXTRA_AFTER_SAVE, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        Prefs.putString(BURIAL_PLACE_COORDS, "");
//        Prefs.putString(BURIAL_PLACE_CITY, "");
//        Prefs.putString(BURIAL_PLACE_CEMETERY, "");
//        Prefs.putString(BURIAL_PLACE_SECTOR, "");
//        Prefs.putString(BURIAL_PLACE_LINE, "");
//        Prefs.putString(BURIAL_PLACE_GRAVE, "");
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
    public void onDeletePage(Object response) {
        Toast.makeText(getApplicationContext(), "Страница удалена", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDeletePageError(Throwable throwable) {
        Utils.showSnack(image, "Ошибка удаления, попробуйте позже");
    }

    public void showDeleteDialog() {
        DeletePageDialog myDialogFragment = new DeletePageDialog();
        myDialogFragment.setCallback(this);
        myDialogFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }

    @Override
    public void saveItem(ResponseHandBook responseHandBook) {
        religion.setText(responseHandBook.getName());
    }

    @OnClick(R.id.image_layout)
    public void pickImage() {
        progressDialog = LoadingPopupUtils.showLoadingDialog(this);
        cropImage(this);
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
        afterSave = Prefs.getBoolean(INTENT_EXTRA_AFTER_SAVE, false);
        if (isEdit || Prefs.getBoolean(INTENT_EXTRA_AFTER_SAVE, false)) {
            intent.putExtra("MODEL", memoryPageModel);
            intent.putExtra("EDIT", true);
        }
        startActivityForResult(intent, GRAVE_INFO_RESULT);
    }

    @OnClick(R.id.save_button)
    public void savePage() {
        person.setSecondName(lastName.getText().toString());
        person.setThirdName(middleName.getText().toString());
        person.setComment(description.getInputText().getText().toString());
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
                afterSave = data.getBooleanExtra(INTENT_EXTRA_AFTER_SAVE, false);
                Prefs.putString(BURIAL_PLACE_COORDS, data.getStringExtra(BURIAL_PLACE_COORDS));
                Prefs.putString(BURIAL_PLACE_CITY, data.getStringExtra(BURIAL_PLACE_CITY));
                Prefs.putString(BURIAL_PLACE_CEMETERY, data.getStringExtra(BURIAL_PLACE_CEMETERY));
                Prefs.putString(BURIAL_PLACE_SECTOR, data.getStringExtra(BURIAL_PLACE_SECTOR));
                Prefs.putString(BURIAL_PLACE_LINE, data.getStringExtra(BURIAL_PLACE_LINE));
                Prefs.putString(BURIAL_PLACE_GRAVE, data.getStringExtra(BURIAL_PLACE_GRAVE));
                Prefs.putBoolean(INTENT_EXTRA_AFTER_SAVE, data.getBooleanExtra(INTENT_EXTRA_AFTER_SAVE, false));
            }
        } else if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                glideLoadInto(data.getData(), image);
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
                glideLoadIntoAsBitmap(hah, image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = getBitmap(getContentResolver(), result.getUri());
                    imageFile = saveBitmap(bitmap);
                    glideLoadInto(result.getUri(), image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(progressDialog != null)
            progressDialog.dismiss();
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
        initCalendar();
        setTheme();
        person = new AddPageModel();
        isEdit = getIntent().getBooleanExtra("EDIT", false);
        changeIUIfPageEdited();
    }

    private void changeIUIfPageEdited() {
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
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void initEdit() {
        initToolbar();
        lastName.setText(memoryPageModel.getSecondname());
        name.setText(memoryPageModel.getName());
        middleName.setText(memoryPageModel.getThirtname());
        description.setInputText(memoryPageModel.getComment());
        religion.setText(memoryPageModel.getReligiya());
        if (memoryPageModel.getStar().equals("true")) {
            isFamous.setChecked(true);
            notFamous.setChecked(false);
        } else {
            isFamous.setChecked(false);
            notFamous.setChecked(true);
        }
        if (memoryPageModel.getFlag().equals("true")) { //TODO FIX CRASH
            isPublic.setChecked(true);
            noPublic.setChecked(false);
        } else {
            isPublic.setChecked(false);
            noPublic.setChecked(true);
        }
        dateBegin.setText(DateUtils.convertRemoteToLocalFormat(memoryPageModel.getDatarod()));
        dateEnd.setText(DateUtils.convertRemoteToLocalFormat(memoryPageModel.getDatasmert()));
        glideLoadIntoWithError(BASE_URL_FROM_PHOTO + memoryPageModel.getPicture(), image);
    }

    @OnClick(R.id.settings)
    public void deletePage() {
        showDeleteDialog();
    }

    @Override
    public void onDeletePage() {
        presenter.deletePage(memoryPageModel.getId());
    }

    private void setTheme() {
        ColorStateList textColor = Utils.isThemeDark()
                ? getResources().getColorStateList(R.color.abc_dark)
                : getResources().getColorStateList(R.color.abc_light);
        dateBegin.setTextColor(textColor);
        name.setTextColor(textColor);
        middleName.setTextColor(textColor);
        dateEnd.setTextColor(textColor);
        religion.setTextColor(textColor);
        lastName.setTextColor(textColor);
    }

    private void initCalendar() {
        DatePickerDialog.OnDateSetListener dateBeginPickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
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

        DatePickerDialog.OnDateSetListener dateEndPickerDialog = (view, year, monthOfYear, dayOfMonth) -> {
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

    @OnLongClick(R.id.date_begin)
    public void longC() {
        System.out.println("GOOOOOOOL");
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(dateBegin.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        dateBegin.requestFocus();
    }

    private boolean setLong(View view) {
        System.out.println("GOOOOOOOL");
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(dateBegin.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        dateBegin.requestFocus();
        return true;
    }

    private void setInitialDateBegin() {
        dateBegin.setText(dfLocal.format(new Date(dateAndTime.getTimeInMillis())));
    }

    private void setInitialDateEnd() {
        dateEnd.setText(dfLocal.format(new Date(dateAndTime.getTimeInMillis())));
    }

    private void setDateBegin(View v) {
        String localDateBegin = dateBegin.getText().toString();
        if(localDateBegin.length() == 0) {
            DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDate = dateAndTime.getTimeInMillis();
                Date endDate = parseLocalFormat(dateEnd.getText().toString());
                if (endDate != null && dateAndTime.getTime().after(endDate)) {
                    Utils.showSnack(dateBegin, getResources().getString(R.string.events_error_date_death_before_date_birth));
                } else {
                    setInitialDateBegin();
                }
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            dialog.setMaxDate(new Date().getTime());
            dialog.setYearRange(1800, Calendar.getInstance().get(Calendar.YEAR));
            dialog.show(getSupportFragmentManager(), "tag");
        } else {
            int day1 = Integer.parseInt(localDateBegin.substring(0,2));
            int month1 = Integer.parseInt(localDateBegin.substring(3,5));
            int year1 = Integer.parseInt(localDateBegin.substring(6,10));
            DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //startDate = dateAndTime.getTimeInMillis();
                Date endDate = parseLocalFormat(dateEnd.getText().toString());
                if (endDate != null && dateAndTime.getTime().after(endDate)) {
                    Utils.showSnack(dateBegin, getResources().getString(R.string.events_error_date_death_before_date_birth));
                } else {
                    setInitialDateBegin();
                }
            }, year1, month1-1, day1);
            dialog.setMaxDate(new Date().getTime());
            dialog.setYearRange(1800, Calendar.getInstance().get(Calendar.YEAR));
            dialog.show(getSupportFragmentManager(), "tag");
        }
    }

    private void setDateEnd(View v) {
        String localDateEnd = dateEnd.getText().toString();
        if(localDateEnd.length() == 0) {
            DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                    dateAndTime.set(Calendar.YEAR, year);
                    dateAndTime.set(Calendar.MONTH, monthOfYear);
                    dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    endDate = dateAndTime.getTimeInMillis();
                    Date startDate = parseLocalFormat(dateBegin.getText().toString());
                    if (startDate != null && startDate.after(dateAndTime.getTime())) {
                        Utils.showSnack(dateBegin, getResources().getString(R.string.events_error_date_death_before_date_birth));
                    } else {
                        setInitialDateEnd();
                    }
                }
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            dialog.setMaxDate(new Date().getTime());
            dialog.setYearRange(1800, Calendar.getInstance().get(Calendar.YEAR));
            dialog.show(getSupportFragmentManager(), "tag");
        } else {
            int day2 = Integer.parseInt(localDateEnd.substring(0,2));
            int month2 = Integer.parseInt(localDateEnd.substring(3,5));
            int year2 = Integer.parseInt(localDateEnd.substring(6,10));
            DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                    dateAndTime.set(Calendar.YEAR, year);
                    dateAndTime.set(Calendar.MONTH, monthOfYear);
                    dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    endDate = dateAndTime.getTimeInMillis();
                    Date startDate = parseLocalFormat(dateBegin.getText().toString());
                    if (startDate != null && startDate.after(dateAndTime.getTime())) {
                        Utils.showSnack(dateBegin, getResources().getString(R.string.events_error_date_death_before_date_birth));
                    } else {
                        setInitialDateEnd();
                    }
                }
            }, year2, month2-1, day2);
            dialog.setMaxDate(new Date().getTime());
            dialog.setYearRange(1800, Calendar.getInstance().get(Calendar.YEAR));
            dialog.show(getSupportFragmentManager(), "tag");
        }
    }

    private void initToolbar() {
        settings.setVisibility(View.VISIBLE);
        settings.setImageResource(R.drawable.delete);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Prefs.putBoolean(INTENT_EXTRA_AFTER_SAVE, false);

    }
}
