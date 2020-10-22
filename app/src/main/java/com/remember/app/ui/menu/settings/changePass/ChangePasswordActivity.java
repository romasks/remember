package com.remember.app.ui.menu.settings.changePass;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.customView.CustomButton;
import com.remember.app.customView.CustomEditText;
import com.remember.app.customView.CustomTextView;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.menu.settings.SettingPresenter;
import com.remember.app.ui.menu.settings.SettingView;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity implements SettingView {

    @InjectPresenter
    SettingPresenter presenter;
    @BindView(R.id.etCurPass)
    CustomEditText curPass;
    @BindView(R.id.etNewPass)
    CustomEditText newPass;
    @BindView(R.id.etRePass)
    CustomEditText newRepass;
    @BindView(R.id.submit)
    CustomButton submit;
    @BindView(R.id.tvTitle)
    CustomTextView title;
    @BindView(R.id.settings)
    ImageView settings;
    @BindView(R.id.imgEyeCur)
    AppCompatImageView eyeCur;
    @BindView(R.id.imgEyeNew)
    AppCompatImageView eyeNew;
    @BindView(R.id.imgEyeRe)
    AppCompatImageView eyeRe;
    String curPassword = "";
    String newPassword = "";
    String newRePassword = "";
    private boolean showCur = false;
    private boolean showNew = false;
    private boolean showRe = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter.getInfo();
        initUI();
    }

    private void initUI() {
        settings.setVisibility(View.GONE);
        title.setText(R.string.password);
        eyeCur.setOnClickListener(v -> onEyeClickCur(curPass, eyeCur));
        eyeNew.setOnClickListener(v -> onEyeClickNew(newPass, eyeNew));
        eyeRe.setOnClickListener(v ->   onEyeClickRe(newRepass, eyeRe));

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


    private void setTypePass(CustomEditText editText, boolean showPass) {
        if (showPass)
            editText.setTransformationMethod(null);
        else
            editText.setTransformationMethod(new PasswordTransformationMethod());
        editText.setSelection(editText.getText().toString().length());
    }

    private void onEyeClickCur(CustomEditText editText, AppCompatImageView img) {
        if (showCur) {
            showCur = false;
            img.setImageResource(R.drawable.close_eye);
        } else {
            showCur = true;
            img.setImageResource(R.drawable.open_eye);
        }
        setTypePass(editText, showCur);
    }

    private void onEyeClickNew(CustomEditText editText, AppCompatImageView img) {
        if (showNew) {
            showNew = false;
            img.setImageResource(R.drawable.close_eye);
        } else {
            showNew = true;
            img.setImageResource(R.drawable.open_eye);
        }
        setTypePass(editText, showNew);
    }

    private void onEyeClickRe(CustomEditText editText, AppCompatImageView img) {
        if (showRe) {
            showRe = false;
            img.setImageResource(R.drawable.close_eye);
        } else {
            showRe = true;
            img.setImageResource(R.drawable.open_eye);
        }
        setTypePass(editText, showRe);
    }
}
