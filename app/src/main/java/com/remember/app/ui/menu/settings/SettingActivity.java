package com.remember.app.ui.menu.settings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.tabs.TabLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomButton;
import com.remember.app.customView.CustomTextView;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.FragmentPager;
import com.remember.app.ui.menu.settings.changePass.ChangePassListener;
import com.remember.app.ui.menu.settings.changePass.ChangePasswordActivity;
import com.remember.app.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.utils.FileUtils.storagePermissionGranted;
import static com.remember.app.utils.FileUtils.verifyStoragePermissions;

public class SettingActivity extends BaseActivity implements SettingView, ChangePassListener, PersonalDataFragment.ActivityListener {

    private final String TAG = SettingActivity.class.getSimpleName();

    @InjectPresenter
    SettingPresenter presenter;

    @BindView(R.id.save_button)
    CustomButton saveButton;
    @BindView(R.id.back_button)
    ImageView backArrow;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.settings)
    ImageView settings;

    private ViewPager viewPager;
    private boolean isThemeChanged = false;
    public static MainActivityListener mainActivityListener;

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
        PersonalDataFragment.setListener(this);
        PersonalDataFragment.setActivityListener(this);
        viewPager = findViewById(R.id.container);
        setupViewPager(viewPager);

        saveButton.setOnClickListener(v -> {
                getFragmentInViewPager(viewPager.getCurrentItem()).onSaveClick();
                presenter.saveSettings();
            });

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont(tabLayout);
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
       // adapter.addFragment(new FontSettingsFragment(), "Шрифт");
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

    @Override
    public void openChangePassActivity() {
       startActivity(new Intent(this,  ChangePasswordActivity.class));
    }

    private void changeTabsFont(TabLayout tabBar) {
        List<String> arr = new ArrayList<>();
        arr.add("Личные данные");
        arr.add("Уведомления");
        for (int j = 0 ; j<tabBar.getTabCount(); j++) {
            View tab = (View) LayoutInflater.from(getBaseContext()).inflate(R.layout.tab_item, null);
            TextView txtLabel  = tab.findViewById(R.id.text1);
            txtLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont(txtLabel));
            txtLabel.setText(arr.get(j));
            Objects.requireNonNull(tabBar.getTabAt(j)).setCustomView(tab);
        }
    }
    private float scaleFont(TextView textView) {
        if (Prefs.getBoolean("standard", true))
            return (textView.getTextSize() / getResources().getDisplayMetrics().density - 1);
        else {
            return (textView.getTextSize() / getResources().getDisplayMetrics().density + 3);
        }
    }

    @Override
    public void recallActivity() {
        mainActivityListener.finishMainActivity();
    }

    public interface MainActivityListener {
        void finishMainActivity();
    }

    public static void setListener(MainActivityListener listener) {
        mainActivityListener = listener;
    }
}
