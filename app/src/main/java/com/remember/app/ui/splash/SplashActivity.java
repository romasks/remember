package com.remember.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.ui.grid.GridActivity;
import com.remember.app.ui.utils.Utils;

public class SplashActivity extends AppCompatActivity implements SplashView {

    private final String TAG = "SplashActivity";

    @InjectPresenter
    SplashPresenter presenter;

//    @Inject
//    ImagesRepositoryPagedListConfig imagesRepositoryPagedListConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //presenter.loadImages();
//        presenter.setImagesRepositoryConfig(imagesRepositoryPagedListConfig);
//        presenter.getMemoryPageModel();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, GridActivity.class));
            finish();
        }, 2000);

        Log.i(TAG, "Theme " + Prefs.getInt("IS_THEME", 0));
    }
}
