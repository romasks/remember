package com.remember.app.ui.menu.settings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.tabs.TabLayout;
import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.FragmentPager;
import com.remember.app.ui.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.ui.utils.FileUtils.storagePermissionGranted;
import static com.remember.app.ui.utils.FileUtils.verifyStoragePermissions;

public class SettingActivity extends BaseActivity implements SettingView {

    private final String TAG = SettingActivity.class.getSimpleName();

    @InjectPresenter
    SettingPresenter presenter;

    @BindView(R.id.save_button)
    Button saveButton;
    @BindView(R.id.back_button)
    ImageView backArrow;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.settings)
    ImageView settings;

    private ViewPager viewPager;
    private boolean isThemeChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);

        super.onCreate(savedInstanceState);

        title.setText(R.string.settings_header_text);
        settings.setVisibility(View.GONE);

        if (Utils.isThemeDark()) {
            backArrow.setImageResource(R.drawable.ic_back_dark_theme);
        } else {
            backArrow.setImageResource(R.drawable.ic_back);
        }

        if (storagePermissionGranted(this) || Build.VERSION.SDK_INT < 23) {
            setUp();
        } else {
            verifyStoragePermissions(this);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!isThemeChanged) {
            presenter.getInfo();
        } else {
            isThemeChanged = false;
        }
    }

    private void setUp() {
//        presenter.getInfo();

        viewPager = findViewById(R.id.container);
        setupViewPager(viewPager);

        saveButton.setOnClickListener(v -> {
            if (getFragmentInViewPager(viewPager.getCurrentItem()) instanceof NotificationFragment) {
                getFragmentInViewPager(viewPager.getCurrentItem()).onSaveClick();
                presenter.saveSettings();
            } else if (getFragmentInViewPager(viewPager.getCurrentItem()) instanceof PersonalDataFragment) {
                if (presenter.getRequestSettings().getPhone().equals("") || presenter.getRequestSettings().getPhone().length()>10) {
                    getFragmentInViewPager(viewPager.getCurrentItem()).onSaveClick();
                    presenter.saveSettings();
                } else
                    Toast.makeText(this, "Введите коректный номер", Toast.LENGTH_LONG).show();
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.back_button)
    public void back() {
        onBackPressed();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            setUp();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        if (isThemeChanged) return;
        Log.d("CHECK", "SettingActivity setupViewPager");
        FragmentPager adapter = new FragmentPager(getSupportFragmentManager());
        adapter.addFragment(new PersonalDataFragment(presenter), "Личные данные");
        adapter.addFragment(new NotificationFragment(presenter), "Уведомления");
        viewPager.setAdapter(adapter);
    }

    private SettingsBaseFragment getFragmentInViewPager(int position) {
        return (SettingsBaseFragment) ((FragmentPager) viewPager.getAdapter()).getItem(position);
    }

    @Override
    public void error(Throwable throwable) {
        Log.e(TAG, throwable.getMessage() != null ? throwable.getMessage() : "Ошибка загрузки данных");
        Utils.showSnack(saveButton, "Ошибка загрузки данных");
    }

    @Override
    public void onSaved(Object o) {
        Utils.showSnack(saveButton, "Данные успешно сохранены");
        presenter.getInfo();
    }

    @Override
    public void onSavedImage(Object o) {
        ((PersonalDataFragment) ((FragmentPager) viewPager.getAdapter()).getItem(0)).onSavedImage(o);
    }
}
