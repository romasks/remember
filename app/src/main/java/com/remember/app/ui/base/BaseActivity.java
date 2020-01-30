package com.remember.app.ui.base;

import android.os.Bundle;

import com.remember.app.ui.utils.ErrorDialog;
import com.remember.app.ui.utils.MvpAppCompatActivity;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
        new ErrorDialog(
                getSupportFragmentManager(),
                "Проверьте подключение к сети",
                "errorOfflineDialog"
        ).show();
    }

}

