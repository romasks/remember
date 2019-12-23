package com.remember.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.grid.GridActivity;
import com.remember.app.ui.utils.Utils;

public class SplashActivity extends BaseActivity implements SplashView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, GridActivity.class));
            finish();
        }, 500);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }
}
