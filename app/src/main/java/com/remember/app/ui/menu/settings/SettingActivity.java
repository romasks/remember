package com.remember.app.ui.menu.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.FragmentPager;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements SettingView {

    private final String TAG = SettingActivity.class.getSimpleName();

    @InjectPresenter
    SettingPresenter presenter;

    @BindView(R.id.save_button)
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.getInfo();

        ViewPager viewPager = findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.back)
    public void back(){
        onBackPressed();
        finish();
    }

    public void setSaveButtonClickListener(View.OnClickListener listener) {
        saveButton.setOnClickListener(listener);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentPager adapter = new FragmentPager(getSupportFragmentManager());
        adapter.addFragment(new PersonalDataFragment(presenter), "Личные данные");
        adapter.addFragment(new NotificationFragment(presenter), "Уведомления");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void error(Throwable throwable) {
        Log.e(TAG, throwable.getMessage());
        Snackbar.make(saveButton, "Ошибка загрузки данных", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSaved(Object o) {
        Snackbar.make(saveButton, "Данные успешно сохранены", Snackbar.LENGTH_SHORT).show();
        presenter.getInfo();
    }

    @Override
    public void onSavedImage(Object o) {

    }
}
