package com.remember.app.ui.menu.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomAutoCompleteEditText;
import com.remember.app.customView.CustomAutoCompleteTextView;
import com.remember.app.customView.CustomRadioButton;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.ui.menu.settings.changePass.ChangePassListener;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;
import static com.remember.app.data.Constants.PREFS_KEY_AVATAR;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;
import static com.remember.app.ui.utils.FileUtils.saveBitmap;
import static com.remember.app.ui.utils.ImageUtils.cropImage;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class PersonalDataFragment extends SettingsBaseFragment implements SettingView {

    private final String TAG = PersonalDataFragment.class.getSimpleName();

    private SettingPresenter presenter;

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.surname)
    CustomAutoCompleteEditText surname;
    @BindView(R.id.name)
    CustomAutoCompleteTextView name;
    @BindView(R.id.middleName)
    CustomAutoCompleteTextView middleName;
    @BindView(R.id.nickname)
    CustomAutoCompleteTextView nickname;
    @BindView(R.id.location)
    CustomAutoCompleteTextView location;
    @BindView(R.id.email)
    CustomAutoCompleteTextView email;
    @BindView(R.id.phone)
    CustomAutoCompleteTextView phone;
    @BindView(R.id.phoneLayout)
    TextInputLayout phoneLayout;

    @BindView(R.id.rg_theme)
    RadioGroup rgTheme;
    @BindView(R.id.cb_theme_light)
    CustomRadioButton lightTheme;
    @BindView(R.id.cb_theme_dark)
    CustomRadioButton darkTheme;
    public static ChangePassListener changePassListener;
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
            v();
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

    private void v (){
        phone.setOnFocusChangeListener((v, hasFocus) -> {
            phoneLayout.setHint("Телефон");
            if (!hasFocus && (phone.getText().length()==0)){
                phone.setText(null);
            }
        });
    }

    @OnClick(R.id.changePass)
    void openChangePassFragment (){
        changePassListener.openChangePassActivity();
    }

    static void setListener(ChangePassListener listener) {
        changePassListener = listener;
    }
}
