package com.remember.app.ui.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.textfield.TextInputLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomAutoCompleteEditText;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.ResponseRegister;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.utils.ErrorDialog;
import com.remember.app.ui.utils.SuccessDialog;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

import static com.remember.app.data.Constants.PREFS_KEY_AVATAR;
import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;
import static com.remember.app.data.Constants.PREFS_KEY_TOKEN;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;

public class RegisterActivity extends BaseActivity implements RegisterView, SuccessDialog.Callback {

    @InjectPresenter
    RegisterPresenter presenter;

    @BindView(R.id.register)
    CustomTextView loginScreen;
    @BindView(R.id.name_value)
    CustomAutoCompleteEditText nickName;
    @BindView(R.id.name_title)
    TextInputLayout nameTitle;
    @BindView(R.id.middle_name)
    CustomAutoCompleteEditText email;
    @BindView(R.id.checkboxTerms)
    CheckBox checkBox;
    @BindView(R.id.tvTerms)
    CustomTextView tvTerms;
    boolean isCheck = true;

    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginScreen.setOnClickListener(v -> {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        });
        tvTerms.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://xn--l1acbd2f.xn--p1acf/agreement")));
        });
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isCheck = isChecked;
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.sign_in_btn)
    public void register() {
        if (isCheck) {
            if (nickName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Введите Никнейм", Toast.LENGTH_LONG).show();
            } else if (email.getText().toString().isEmpty()) {
                Toast.makeText(this, "Введите Email", Toast.LENGTH_LONG).show();
            } else {
                if (checkEmail(email.getText().toString())) {
                    presenter.registerLogin(nickName.getText().toString(), email.getText().toString());
                } else {
                    errorDialog("Некорректный тип e-mail");
                }
            }
        } else
            Toast.makeText(this, "Согласитесь с правилами использования", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
        finish();
    }

    @Override
    public void onRegistered(Response<ResponseRegister> responseRegisterResponse) {
        if (responseRegisterResponse.isSuccessful()) {
            if (responseRegisterResponse.body().getUser() != null) {
                if (responseRegisterResponse.body().getUser().equals("isset")) {
                    errorDialog("Такой E-mail существует");
                } else {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            } else {
                Prefs.putString(PREFS_KEY_EMAIL, email.getText().toString());
                Prefs.putString(PREFS_KEY_NAME_USER, nickName.getText().toString());
                Prefs.putString(PREFS_KEY_USER_ID, String.valueOf(responseRegisterResponse.body().getId()));
                Prefs.putString(PREFS_KEY_TOKEN, responseRegisterResponse.body().getToken());
                Prefs.putString(PREFS_KEY_AVATAR, "");
                successDialog(responseRegisterResponse.body());
            }
        } else {
//            errorDialog(responseRegisterResponse.errorBody());
            errorDialog("Неверный E-mail либо такой E-mail уже существует");
        }
    }

    @Override
    public void onError(Throwable throwable) {
        errorDialog(throwable.getMessage());
    }

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private void successDialog(ResponseRegister responseRegister) {
        SuccessDialog successDialog = new SuccessDialog();
        successDialog.setCallback(this);
        successDialog.setDescription(responseRegister);
        successDialog.show(getSupportFragmentManager().beginTransaction(), "successDialog");
    }

    public void errorDialog(String text) {
        new ErrorDialog(getSupportFragmentManager(), text, "errorDialog").show();
    }

    @Override
    public void clickOk() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}
