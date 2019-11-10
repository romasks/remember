package com.remember.app.ui.menu.settings;

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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.MvpAppCompatFragment;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.PREFS_KEY_AVATAR;
import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;
import static com.remember.app.data.Constants.PREFS_KEY_IS_THEME;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;
import static com.remember.app.data.Constants.THEME_DARK;
import static com.remember.app.data.Constants.THEME_LIGHT;
import static com.remember.app.ui.utils.FileUtils.saveBitmap;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class PersonalDataFragment extends MvpAppCompatFragment implements SettingView {

    private final String TAG = PersonalDataFragment.class.getSimpleName();

    private SettingPresenter presenter;

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.surname)
    AppCompatEditText surname;
    @BindView(R.id.name)
    AutoCompleteTextView name;
    @BindView(R.id.middleName)
    AutoCompleteTextView middleName;
    @BindView(R.id.nickname)
    AutoCompleteTextView nickname;
    @BindView(R.id.location)
    AutoCompleteTextView location;
    @BindView(R.id.email)
    AutoCompleteTextView email;
    @BindView(R.id.phone)
    AutoCompleteTextView phone;

    @BindView(R.id.rg_theme)
    RadioGroup rgTheme;
    @BindView(R.id.cb_theme_light)
    AppCompatRadioButton lightTheme;
    @BindView(R.id.cb_theme_dark)
    AppCompatRadioButton darkTheme;

    private Unbinder unbinder;
    private File imageFile;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;

    public PersonalDataFragment() {
    }

    PersonalDataFragment(@NotNull SettingPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.settingsLiveData.observeForever(this::onReceivedInfo);

        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setings_lk, container, false);
        unbinder = ButterKnife.bind(this, view);
        email.setText(Prefs.getString(PREFS_KEY_EMAIL, ""));

        if (Prefs.getInt(PREFS_KEY_IS_THEME, 0) == 0 || Prefs.getInt(PREFS_KEY_IS_THEME, 0) == THEME_LIGHT) {
            rgTheme.check(R.id.cb_theme_light);
        } else if (Prefs.getInt(PREFS_KEY_IS_THEME, 0) == THEME_DARK) {
            rgTheme.check(R.id.cb_theme_dark);
            lightTheme.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            darkTheme.setTextColor(getResources().getColor(R.color.colorWhiteDark));

        }

        getActivity().findViewById(R.id.save_button).setOnClickListener(v -> {
            presenter.getRequestSettings()
                    .name(name).surname(surname).middleName(middleName)
                    .nickname(nickname).location(location).phone(phone);
            presenter.saveSettings();
            getActivity().onBackPressed();
        });

        /*((SettingActivity) getActivity()).setSaveButtonClickListener(v -> {
            presenter.getRequestSettings()
                    .name(name).surname(surname).middleName(middleName)
                    .nickname(nickname).location(location).phone(phone);
            presenter.saveSettings();
            getActivity().onBackPressed();
        });*/



        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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

    @OnClick(R.id.add_new_photo)
    void addNewPhoto() {
        CropImage.activity()
                .setCropShape(CropImageView.CropShape.OVAL)
                .setFixAspectRatio(true)
                .start(getContext(), this);
    }

    @OnClick(R.id.cb_theme_light)
    void setLightTheme() {
        rgTheme.check(R.id.cb_theme_light);
        Prefs.putInt(PREFS_KEY_IS_THEME, THEME_LIGHT);
    }

    @OnClick(R.id.cb_theme_dark)
    void setDarkTheme() {
        rgTheme.check(R.id.cb_theme_dark);
        Prefs.putInt(PREFS_KEY_IS_THEME, THEME_DARK);
    }

    private void onReceivedInfo(ResponseSettings responseSettings) {
        if (responseSettings.getPicture() != null) {
            Prefs.putString(PREFS_KEY_AVATAR, BASE_SERVICE_URL + responseSettings.getPicture());
            Prefs.putString(PREFS_KEY_NAME_USER, responseSettings.getName() + " " + responseSettings.getSurname());
            setGlideImage(getContext(), BASE_SERVICE_URL + responseSettings.getPicture(), avatar);
        } else {
            setGlideImage(getContext(), R.drawable.ic_unknown, avatar);
        }

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        avatar.setColorFilter(filter);

        if (responseSettings.getSurname() != null) {
            surname.setText(responseSettings.getSurname());
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
        if (responseSettings.getNickname() != null) {
            nickname.setText(responseSettings.getNickname());
        }
        if (responseSettings.getLocation() != null && !responseSettings.getLocation().equals("null")) {
            location.setText(responseSettings.getLocation());
        }
        getView().invalidate();
    }

    @Override
    public void error(Throwable throwable) {

    }

    @Override
    public void onSaved(Object o) {

    }

    @Override
    public void onSavedImage(Object o) {
        Snackbar.make(avatar, "Фото успешно сохранено", Snackbar.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }
}
