package com.remember.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.crashlytics.android.Crashlytics;
import com.remember.app.R;
import com.remember.app.ui.grid.GridActivity;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity implements SplashView {

    @InjectPresenter
    SplashPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, GridActivity.class));
            finish();
        }, 2000);

    }
}
