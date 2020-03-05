package com.remember.app.ui.menu.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.google.android.material.textfield.TextInputLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy;
import com.remember.app.R;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.ui.utils.LoadingPopupUtils;
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
import static com.remember.app.ui.utils.FileUtils.saveBitmap;
import static com.remember.app.ui.utils.ImageUtils.cropImage;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class PersonalDataFragment extends SettingsBaseFragment implements SettingView {

    private final String TAG = PersonalDataFragment.class.getSimpleName();
    String formattedNumber = "";

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
    @BindView(R.id.phoneLayout)
    TextInputLayout phoneLayout;
    @BindView(R.id.rg_theme)
    RadioGroup rgTheme;
    @BindView(R.id.cb_theme_light)
    AppCompatRadioButton lightTheme;
    @BindView(R.id.cb_theme_dark)
    AppCompatRadioButton darkTheme;

    private ProgressDialog progressDialog;

    public PersonalDataFragment() {
    }

    PersonalDataFragment(@NotNull SettingPresenter presenter) {
        this.presenter = presenter;
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
            presenter.getInfo();
            presenter.getSettingsLiveData().observeForever(this::onReceivedInfo);
        }
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
            String phone = "+7" + formattedNumber;
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
        if (responseSettings.getPhone() != null && !responseSettings.getPhone().equals(""))
             phone.setText(responseSettings.getPhone());
        nickname.setText(responseSettings.getNickname());
        location.setText(responseSettings.getLocation());
        email.setText(responseSettings.getEmail());
        setupPrefixSample();
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

    private void setupPrefixSample() {
        final List<String> affineFormats = new ArrayList<>();
        affineFormats.add("7 ([000]) [000]-[00]-[00]");

        final MaskedTextChangedListener listener = MaskedTextChangedListener.Companion.installOn(
                phone,
                "+7 ([000]) [000]-[00]-[00]",
                affineFormats,
                AffinityCalculationStrategy.PREFIX,
                (maskFilled, extractedValue, formattedText) -> {
                    formattedNumber = extractedValue.replace("(", "").replace(")", "").trim();

                } );

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                phoneLayout.setHint("Телефон");
                if (!hasFocus && formattedNumber.equals("")){
                    phone.setText(null);
                }
            }
        });
    }
}
