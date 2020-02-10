package com.remember.app.ui.base;

import android.os.Bundle;

import com.remember.app.R;
import com.remember.app.ui.utils.ErrorDialog;
import com.remember.app.ui.utils.MvpAppCompatActivity;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import umairayub.madialog.MaDialog;
import umairayub.madialog.MaDialogListener;

public abstract class BaseActivity extends MvpAppCompatActivity implements BaseView {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        unbinder = ButterKnife.bind(this);
    }

    protected abstract int getContentView();

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onErrorOffline() {

        new MaDialog.Builder(BaseActivity.this)
                .setTitle("Отсутствует интернет-соединение")
                .setMessage("Пожалуйста, повторите действие позже")
                .setPositiveButtonText("ok")
                .setImage(R.drawable.ic_no_internet)
                .setCancelableOnOutsideTouch(false)
                .setPositiveButtonListener(new MaDialogListener() {
                    @Override
                    public void onClick() {
                        finish();
                    }
                })
                .build();
    }

}

