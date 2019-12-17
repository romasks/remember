package com.remember.app.ui.menu.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.MvpAppCompatFragment;
import com.remember.app.ui.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
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
    private ProgressDialog progressDialog;

    public PersonalDataFragment() {
    }

    PersonalDataFragment(@NotNull SettingPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (presenter != null) {
            presenter.getSettingsLiveData().observeForever(this::onReceivedInfo);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setings_lk, container, false);
        unbinder = ButterKnife.bind(this, view);
        email.setText(Prefs.getString(PREFS_KEY_EMAIL, ""));

        ColorStateList textColor = Utils.isThemeDark()
                ? getResources().getColorStateList(R.color.abc_dark)
                : getResources().getColorStateList(R.color.abc_light);

        rgTheme.check(Utils.isThemeDark() ? R.id.cb_theme_dark : R.id.cb_theme_light);

        surname.setTextColor(textColor);
        name.setTextColor(textColor);
        middleName.setTextColor(textColor);
        nickname.setTextColor(textColor);
        email.setTextColor(textColor);
        location.setTextColor(textColor);
        phone.setTextColor(textColor);

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
                    setGlideImage(getContext(), resultUri, avatar);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getUri());
                        File imageFile = saveBitmap(bitmap);
                        presenter.saveImageSetting(imageFile);
                        progressDialog = LoadingPopupUtils.showLoadingDialog(getContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(TAG, "Ошибка загрузки фото", error);
                Utils.showSnack(avatar, "Ошибка загрузки фото");
            }
        }
    }

    @OnClick(R.id.add_new_photo)
    void addNewPhoto() {
        if (getContext() != null) {
            CropImage.activity()
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setFixAspectRatio(true)
                    .start(getContext(), this);
        } else {
            Utils.showSnack(avatar, "Внутренняя ошибка приложения");
        }
    }

    @OnClick(R.id.cb_theme_light)
    void setLightTheme() {
        rgTheme.check(R.id.cb_theme_light);
        Prefs.putInt(PREFS_KEY_IS_THEME, THEME_LIGHT);
        getActivity().onBackPressed();
    }

    @OnClick(R.id.cb_theme_dark)
    void setDarkTheme() {
        rgTheme.check(R.id.cb_theme_dark);
        Prefs.putInt(PREFS_KEY_IS_THEME, THEME_DARK);
        getActivity().onBackPressed();
    }

    void onSaveClick() {
        presenter.getRequestSettings()
                .name(name).surname(surname).middleName(middleName)
                .nickname(nickname).location(location).phone(phone);
    }

    private void onReceivedInfo(ResponseSettings responseSettings) {
        if (!responseSettings.getPicture().isEmpty()) {
            Prefs.putString(PREFS_KEY_AVATAR, responseSettings.getPicture());
            Prefs.putString(PREFS_KEY_NAME_USER, responseSettings.getName() + " " + responseSettings.getSurname());
            setGlideImage(getContext(), responseSettings.getPicture(), avatar);
        } else {
            setGlideImage(getContext(), R.drawable.ic_unknown, avatar);
        }

        surname.setText(responseSettings.getSurname());
        name.setText(responseSettings.getName());
        middleName.setText(responseSettings.getThirdname());
        phone.setText(responseSettings.getPhone());
        nickname.setText(responseSettings.getNickname());
        location.setText(responseSettings.getLocation());

        if (getView() != null) {
            getView().invalidate();
        } else {
            Utils.showSnack(avatar, "Внутренняя ошибка приложения");
        }
    }

    @Override
    public void error(Throwable throwable) {
        // placeholder
    }

    @Override
    public void onSaved(Object o) {
        // placeholder
    }

    @Override
    public void onSavedImage(Object o) {
        Utils.showSnack(avatar, "Фото успешно сохранено");
        progressDialog.dismiss();
    }
}
