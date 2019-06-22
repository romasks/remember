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
import android.widget.ImageView;

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
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.MvpAppCompatFragment;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
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
        Prefs.putString("AVATAR", "http://86.57.172.88:8082" + responseSettings.getSetting().get(0).getPicture());
        Glide.with(getContext())
                .load("http://86.57.172.88:8082" + responseSettings.getSetting().get(0).getPicture())
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(avatar);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        avatar.setColorFilter(filter);
        if (responseSettings.getSetting().get(0).getSurname() != null) {
            secondName.setText(responseSettings.getSetting().get(0).getSurname());
        }
        if (responseSettings.getSetting().get(0).getName() != null) {
            name.setText(responseSettings.getSetting().get(0).getName());
        }
        if (responseSettings.getSetting().get(0).getSurname() != null) {
            middleName.setText(responseSettings.getSetting().get(0).getThirdname());
        }
        if (responseSettings.getEmail() != null) {
            email.setText(responseSettings.getEmail());
        }
        if (responseSettings.getSetting().get(0).getPhone() != null) {
            phone.setText(responseSettings.getSetting().get(0).getPhone());
        }
        getView().invalidate();
    }

    @Override
    public void error(Throwable throwable) {
        Log.e("ERROR", throwable.getMessage());
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
