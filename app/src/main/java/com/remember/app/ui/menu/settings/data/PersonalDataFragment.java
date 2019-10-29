package com.remember.app.ui.menu.settings.data;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.appcompat.widget.AppCompatRadioButton;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.RequestSettings;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.MvpAppCompatFragment;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class PersonalDataFragment extends MvpAppCompatFragment implements PersonalDataFragmentView {

    @InjectPresenter
    PersonalDataFragmentPresenter presenter;

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.login)
    AutoCompleteTextView login;
    @BindView(R.id.secName)
    AppCompatEditText secondName;
    @BindView(R.id.name)
    AutoCompleteTextView name;
    @BindView(R.id.surname)
    AutoCompleteTextView middleName;
    @BindView(R.id.livePlace)
    AutoCompleteTextView city;
    @BindView(R.id.email)
    AutoCompleteTextView email;
    @BindView(R.id.phone)
    AutoCompleteTextView phone;
    @BindView(R.id.cb_theme_svetl)
    AppCompatRadioButton but_svetl;
    @BindView(R.id.cb_theme_temn)
    AppCompatRadioButton but_temnee;
    @BindView(R.id.add_new_photo)
    Button add_photo;
    @BindView(R.id.save_button)
    Button save_photo;
     private static String TAG="PersonalDataFragment";
    private Unbinder unbinder;
    private File imageFile;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.getInfo();
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
            } else {
                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
        }



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setings_lk, container, false);
        unbinder = ButterKnife.bind(this, v);
        if (Prefs.getInt("IS_THEME",0)==0||Prefs.getInt("IS_THEME",0)==1){
            but_svetl.setChecked(true);
        }else if (Prefs.getInt("IS_THEME",0)==2){
            but_temnee.setChecked(true);
            but_svetl.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            but_temnee.setTextColor(getResources().getColor(R.color.colorWhiteDark));

        }
        but_svetl.setOnClickListener(l->{
            Objects.requireNonNull(getActivity()).setTheme(R.style.AppTheme);
            getActivity().recreate();

            if (but_temnee.isChecked()) {
                but_temnee.setChecked(false);
            }
            Prefs.putInt("IS_THEME",1);
        });

        but_temnee.setOnClickListener(o->{
            Objects.requireNonNull(getActivity()).setTheme(R.style.AppTheme_Dark);
            getActivity().recreate();
            if (but_svetl.isChecked()){
                but_svetl.setChecked(false);
            }
            Prefs.putInt("IS_THEME",2);
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Log.i(TAG,result.getUri().getPath());
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (getContext() != null) {
                    Glide.with(getContext())
                            .load(resultUri)
                            .apply(RequestOptions.circleCropTransform())
                            .into(avatar);
                    ColorMatrix colorMatrix = new ColorMatrix();
                    colorMatrix.setSaturation(0);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                    avatar.setColorFilter(filter);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getUri());
                        imageFile = saveBitmap(bitmap);
                        presenter.saveImageSetting(imageFile);
                        progressDialog = LoadingPopupUtils.showLoadingDialog(getContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
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

    @OnClick(R.id.add_new_photo)
    public void addNewPhoto() {
        CropImage.activity()
                .setCropShape(CropImageView.CropShape.OVAL)
                .setFixAspectRatio(true)
                .start(getContext(), this);
    }

    @OnClick(R.id.save_button)
    public void save() {
        RequestSettings requestSettings = new RequestSettings();
        requestSettings.setName(name.getText().toString());
        requestSettings.setLocation(city.getText().toString());
        requestSettings.setMiddleName(middleName.getText().toString());
        requestSettings.setSurName(secondName.getText().toString());
        requestSettings.setPhone(phone.getText().toString());
        presenter.saveSettings(requestSettings);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onReceivedInfo(ResponseSettings responseSettings) {
        if (responseSettings.getPicture() != null) {
            Prefs.putString("AVATAR", "http://помню.рус" + responseSettings.getPicture());
            Prefs.putString("NAME_USER", responseSettings.getName() + " " + responseSettings.getSurname());
            Glide.with(this)
                    .load("http://помню.рус" + responseSettings.getPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(avatar);
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_unknown)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(avatar);
        }
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        avatar.setColorFilter(filter);
        try {
            if (responseSettings.getSurname() != null) {
                secondName.setText(responseSettings.getSurname());
            }
            if (responseSettings.getName() != null) {
                name.setText(responseSettings.getName());
            }
            if (responseSettings.getThirdname() != null) {
                middleName.setText(responseSettings.getThirdname());
            }
            if (responseSettings.getPhone() != null) {
                phone.setText(responseSettings.getPhone());
            }
        } catch (Exception e){
            login.setText(responseSettings.getName());
        }
        getView().invalidate();
    }

    @Override
    public void error(Throwable throwable) {
        Log.i(TAG, throwable.toString());
        Snackbar.make(avatar, "Ошибка загрузки данных", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSaved(Object o) {
        presenter.getInfo();
    }

    @Override
    public void onSavedImage(Object o) {
        progressDialog.dismiss();
    }
}
