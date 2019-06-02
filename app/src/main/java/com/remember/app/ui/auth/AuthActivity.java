package com.remember.app.ui.auth;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.remember.app.R;

public class AuthActivity extends MvpAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }
}
