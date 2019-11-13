package com.remember.app.ui.menu.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.viewpager.widget.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
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

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.getInfo();

        viewPager = findViewById(R.id.container);
        setupViewPager(viewPager);

        saveButton.setOnClickListener(v -> {
            switch (viewPager.getCurrentItem()) {
                case 0: {
                    ((PersonalDataFragment) ((FragmentPager) viewPager.getAdapter()).getItem(0)).onSaveClick();
                    break;
                }
                case 1: {
                    ((NotificationFragment) ((FragmentPager) viewPager.getAdapter()).getItem(1)).onSaveClick();
                    break;
                }
                default:
            }
            presenter.saveSettings();
        });

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
        finish();
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
        Log.e(TAG, throwable.getMessage() != null ? throwable.getMessage() : "Ошибка загрузки данных");
        Snackbar.make(saveButton, "Ошибка загрузки данных", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSaved(Object o) {
        Snackbar.make(saveButton, "Данные успешно сохранены", Snackbar.LENGTH_SHORT).show();
        presenter.getInfo();
    }

    @Override
    public void onSavedImage(Object o) {
        ((PersonalDataFragment) ((FragmentPager) viewPager.getAdapter()).getItem(0)).onSavedImage(o);
    }
}
