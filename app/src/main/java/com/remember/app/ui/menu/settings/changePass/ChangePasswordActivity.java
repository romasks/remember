package com.remember.app.ui.menu.settings.changePass;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.ErrorResponse;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.menu.settings.SettingPresenter;
import com.remember.app.ui.menu.settings.SettingView;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity implements SettingView {

    @InjectPresenter
    SettingPresenter presenter;
    @BindView(R.id.etCurPass)
    EditText curPass;
    @BindView(R.id.etNewPass)
    EditText newPass;
    @BindView(R.id.etRePass)
    EditText newRepass;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.settings)
    ImageView settings;
    String curPassword = "";
    String newPassword = "";
    String newRePassword = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter.getInfo();
        initUI();
    }

    private void initUI() {
        settings.setVisibility(View.GONE);
        title.setText(R.string.password);

    }

    private boolean validation() {
        boolean isValid = false;
        curPassword = curPass.getText().toString().trim();
        newPassword = newPass.getText().toString().trim();
        newRePassword = newRepass.getText().toString().trim();
        if (curPassword.equals("") || newPassword.equals("") || newRePassword.equals(""))
            Toast.makeText(getBaseContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
        else if (curPassword.length() < 6 || newPassword.length() < 6 || newRePassword.length() < 6) {
            Toast.makeText(getBaseContext(), "Пароль должен содержать минимум 6 символов!", Toast.LENGTH_SHORT).show();
        } else {
                if(!newPassword.equals(newRePassword)){
                    Toast.makeText(getBaseContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            }
                else isValid = true;
        }
        return isValid;
    }

    @OnClick(R.id.back_button)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.submit)
    public void changePassword() {
        if (validation())
            presenter.changePassword(curPassword, newPassword);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_change_pass;
    }

    @Override
    public void error(Throwable throwable) {
//        ErrorResponse error = presenter.parseError(throwable);
//        String s = error.getError();
//        String d = s;
        Toast.makeText(getBaseContext(), "Введите правильный прежний пароль", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaved(Object o) {
        Log.d("GGGGG", o.toString());
        Toast.makeText(getBaseContext(), "Пароль изменён", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onSavedImage(Object o) {
    }
}
