package com.remember.app.ui.base;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.remember.app.ui.utils.MvpAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends MvpAppCompatActivity {

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

}

