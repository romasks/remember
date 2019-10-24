package com.remember.app.ui.cabinet.memory_pages.add_page;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.app.ActivityCompat;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.PageEditedResponse;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.ui.cabinet.main.MainActivity;
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
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.image_layout)
    ConstraintLayout imageLayout;
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
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memory_page);
        ButterKnife.bind(this);
        initiate();
        Intent i = getIntent();
        person = new AddPageModel();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
            } else {
                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
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

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
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
        dateBegin.setText(memoryPageModel.getDatarod());
        dateEnd.setText(memoryPageModel.getDatasmert());
        religion.setText(memoryPageModel.getReligiya());
        Glide.with(this)
                .load("http://помню.рус" + memoryPageModel.getPicture())
                .error(R.drawable.darth_vader)
                .into(image);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        image.setColorFilter(filter);
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
        person.setBirthDate(dateBegin.getText().toString());
        person.setDeathDate(dateEnd.getText().toString());
        person.setUserId(Prefs.getString("USER_ID", "0"));
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
//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String requiredDate = df.format(new Date(dateAndTime.getTimeInMillis()));
        dateBegin.setText(requiredDate);
    }

    private void setInitialDateEnd() {
        @SuppressLint("SimpleDateFormat")
//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String requiredDate = df.format(new Date(dateAndTime.getTimeInMillis()));
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
                Glide.with(this)
                        .load(data.getData())
                        .into(image);
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                image.setColorFilter(filter);
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
                Glide.with(this)
                        .asBitmap()
                        .load(hah)
                        .apply(RequestOptions.circleCropTransform())
                        .into(image);
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                image.setColorFilter(filter);
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
                    Glide.with(getApplicationContext())
                            .load(result.getUri())
                            .into(image);
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

    public static File saveBitmap(Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "image.jpg");

        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
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
    public void onEdited(PageEditedResponse responsePages) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("PERSON", person);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra("PERSON", person);
        intent.putExtra("IMAGE", imageFile);
        intent.putExtra("ID", person.getId());
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
