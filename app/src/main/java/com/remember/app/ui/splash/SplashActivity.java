package com.remember.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.grid.GridActivity;
import com.remember.app.ui.utils.Utils;

import androidx.annotation.Nullable;

public class SplashActivity extends BaseActivity implements SplashView {

    private final String TAG = "SplashActivity";

    @InjectPresenter
    SplashPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);

        presenter.initLoadImages();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, GridActivity.class));
            finish();
        }, 2000);

        Log.i(TAG, "Theme " + Prefs.getInt("IS_THEME", 0));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }
}
