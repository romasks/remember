package com.remember.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.ui.cabinet.home.HomeActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.grid.GridActivity;

public class SplashActivity extends AppCompatActivity implements SplashView{

    @InjectPresenter
    SplashPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            if (!Prefs.getString("USER_ID", "").equals("")){
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(this, GridActivity.class));
                finish();
            }
        }, 2000);

    }
}
