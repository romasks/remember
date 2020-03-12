package com.remember.app.ui.menu.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.google.android.material.textfield.TextInputLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy;
import com.remember.app.R;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.ui.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.remember.app.data.Constants.PREFS_KEY_AVATAR;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;
import static com.remember.app.ui.utils.FileUtils.getBitmap;
import static com.remember.app.ui.utils.FileUtils.saveBitmap;
import static com.remember.app.ui.utils.ImageUtils.cropImage;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class PersonalDataFragment extends SettingsBaseFragment {

    private final String TAG = PersonalDataFragment.class.getSimpleName();

    private SettingPresenter presenter;
    private ProgressDialog progressDialog;

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
    @BindView(R.id.phoneLayout)
    TextInputLayout phoneLayout;

    @BindView(R.id.rg_theme)
    RadioGroup rgTheme;
    @BindView(R.id.cb_theme_light)
    AppCompatRadioButton lightTheme;
    @BindView(R.id.cb_theme_dark)
    AppCompatRadioButton darkTheme;

    public PersonalDataFragment() {
    }

    PersonalDataFragment(@NotNull SettingPresenter presenter, @NotNull ProgressDialog progressDialog) {
        this.presenter = presenter;
        this.progressDialog = progressDialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_setings_lk;
    }

    @Override
    protected void setUp() {
        setTheme();
        rgTheme.check(Utils.isThemeDark() ? R.id.cb_theme_dark : R.id.cb_theme_light);
    }

    private void setTheme() {
        ColorStateList textColor = Utils.isThemeDark()
                ? getResources().getColorStateList(R.color.abc_dark)
                : getResources().getColorStateList(R.color.abc_light);

        surname.setTextColor(textColor);
        name.setTextColor(textColor);
        middleName.setTextColor(textColor);
        nickname.setTextColor(textColor);
        email.setTextColor(textColor);
        location.setTextColor(textColor);
        phone.setTextColor(textColor);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (presenter != null) {
            presenter.getSettingsLiveData().observeForever(this::onReceivedInfo);
            v();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && getContext() != null) {
                Uri resultUri = result.getUri();
                setGlideImage(getContext(), resultUri, avatar);
                try {
                    File imageFile = saveBitmap(getBitmap(getContext(), resultUri));
                    presenter.saveImageSetting(imageFile);
                    progressDialog.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE || getContext() == null) {
                Utils.showSnack(avatar, "Ошибка загрузки фото");
            }
        }
    }

    @OnClick(R.id.add_new_photo)
    void addNewPhoto() {
        if (getContext() != null) {
            cropImage(this, CropImageView.CropShape.OVAL);
        } else {
            Utils.showSnack(avatar, "Внутренняя ошибка приложения");
        }
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
        email.setText(responseSettings.getEmail());

        if (getView() != null) {
            getView().invalidate();
        } else {
            Utils.showSnack(avatar, "Внутренняя ошибка приложения");
        }
    }

    private void v() {
        phone.setOnFocusChangeListener((v, hasFocus) -> {
            phoneLayout.setHint("Телефон");
            if (!hasFocus && (phone.getText().length() == 0)) {
                phone.setText(null);
            }
        });
    }
}
