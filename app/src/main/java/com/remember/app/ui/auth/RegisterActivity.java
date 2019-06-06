package com.remember.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.ResponseRegister;
import com.remember.app.ui.cabinet.MainActivity;
import com.squareup.haha.perflib.Main;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisterActivity extends MvpAppCompatActivity implements RegisterView{

    @InjectPresenter
    RegisterPresenter presenter;

    @BindView(R.id.back_button)
    ImageView back;
    @BindView(R.id.register)
    TextView loginScreen;
    @BindView(R.id.name_value)
    TextView nickName;
    @BindView(R.id.middle_name)
    TextView email;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        unbinder = ButterKnife.bind(this);

        back.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        loginScreen.setOnClickListener(v -> {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        });
    }

    @OnClick(R.id.sign_in_btn)
    public void register() {
        if (nickName.getText().toString().equals("")){
            Snackbar.make(loginScreen, "Введите Никнейм", Snackbar.LENGTH_LONG).show();
        } else if (email.getText().toString().equals("")){
            Snackbar.make(loginScreen, "Введите Email", Snackbar.LENGTH_LONG).show();
        } else {
            presenter.registerLogin(nickName.getText().toString(), email.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onRegistered(ResponseRegister responseRegister) {
        if (responseRegister.getUser().equals("isset")){
            Snackbar.make(loginScreen, "Такой email существует", Snackbar.LENGTH_LONG).show();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    @Override
    public void error(Throwable throwable) {
        Snackbar.make(loginScreen, "Ошибка регистрации", Snackbar.LENGTH_LONG).show();
    }
}
