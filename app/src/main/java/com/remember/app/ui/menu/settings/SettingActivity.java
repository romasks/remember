package com.remember.app.ui.menu.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.FragmentPager;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.PREFS_KEY_IS_THEME;

public class SettingActivity extends BaseActivity implements SettingView {

    private final String TAG = SettingActivity.class.getSimpleName();

    @InjectPresenter
    SettingPresenter presenter;

    @BindView(R.id.save_button)
    Button saveButton;
    @BindView(R.id.back)
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Prefs.getInt(PREFS_KEY_IS_THEME, 0) == 2) {
            setTheme(R.style.AppTheme_Dark);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            setTheme(R.style.AppTheme);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        presenter.getInfo();

        if (Prefs.getInt(PREFS_KEY_IS_THEME, 0) == 2) {
            backArrow.setImageResource(R.drawable.ic_back_dark_theme);
        } else {
            backArrow.setImageResource(R.drawable.ic_back);
        }

        ViewPager viewPager = findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
        finish();
    }

    /*public void setSaveButtonClickListener(View.OnClickListener listener) {
        saveButton.setOnClickListener(listener);
    }*/

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
