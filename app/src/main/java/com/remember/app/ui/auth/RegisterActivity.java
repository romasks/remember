package com.remember.app.ui.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.textfield.TextInputLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.ResponseRegister;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.utils.MvpAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Response;

public class RegisterActivity extends MvpAppCompatActivity implements RegisterView {

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
            presenter.registerLogin(nickName.getText().toString(), email.getText().toString());
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
                    Toast.makeText(this, "Такой email существует", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            } else {
                Prefs.putString("EMAIL", email.getText().toString());
                Toast.makeText(this, "Ваш пароль " + responseRegisterResponse.body().getRealPassword(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }
}
