package com.remember.app.ui.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.textfield.TextInputLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.ResponseRegister;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.utils.MvpAppCompatActivity;
import com.remember.app.ui.utils.SuccessDialog;
import com.remember.app.ui.utils.WrongEmailDialog;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Response;

public class RegisterActivity extends MvpAppCompatActivity implements RegisterView, SuccessDialog.Callback {

    @InjectPresenter
    RegisterPresenter presenter;

    @BindView(R.id.register)
    TextView loginScreen;
    @BindView(R.id.name_value)
    AppCompatEditText nickName;
    @BindView(R.id.name_title)
    TextInputLayout nameTitle;
    @BindView(R.id.middle_name)
    AppCompatEditText email;

    private Unbinder unbinder;
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
        setContentView(R.layout.activity_register);
        unbinder = ButterKnife.bind(this);

        loginScreen.setOnClickListener(v -> {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        });
    }

    @OnClick(R.id.sign_in_btn)
    public void register() {
        if (nickName.getText().toString().equals("")) {
            Toast.makeText(this, "Введите Никнейм", Toast.LENGTH_LONG).show();
        } else if (email.getText().toString().equals("")) {
            Toast.makeText(this, "Введите Email", Toast.LENGTH_LONG).show();
        } else {
            if (checkEmail(email.getText().toString())){
                presenter.registerLogin(nickName.getText().toString(), email.getText().toString());
            } else {
                errorDialog("Некорректный тип e-mail");
            }
        }
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onRegistered(Response<ResponseRegister> responseRegisterResponse) {
        if (responseRegisterResponse.isSuccessful()){
            if (responseRegisterResponse.body().getUser() != null){
                if (responseRegisterResponse.body().getUser().equals("isset")) {
                    errorDialog("Такой email существует");
                } else {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            } else {
                Prefs.putString("EMAIL", email.getText().toString());
                Prefs.putString("NAME_USER", nickName.getText().toString());
                Prefs.putString("USER_ID", String.valueOf(responseRegisterResponse.body().getId()));
                successDialog(responseRegisterResponse.body());
            }
        }
    }

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private void successDialog(ResponseRegister responseRegister) {
        SuccessDialog successDialog = new SuccessDialog();
        successDialog.setCallback(this);
        FragmentManager manager = getSupportFragmentManager();
        successDialog.setDescription(responseRegister);
        FragmentTransaction transaction = manager.beginTransaction();
        successDialog.show(transaction, "wrongEmailDialog");
    }

    public void errorDialog(String text){
        WrongEmailDialog wrongEmailDialog = new WrongEmailDialog();
        FragmentManager manager = getSupportFragmentManager();
        wrongEmailDialog.setDescription(text);
        FragmentTransaction transaction = manager.beginTransaction();
        wrongEmailDialog.show(transaction, "wrongEmailDialog");
    }

    @Override
    public void clickOk() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
