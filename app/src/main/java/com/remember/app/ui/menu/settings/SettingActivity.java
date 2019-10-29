package com.remember.app.ui.menu.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.FragmentPager;
import com.remember.app.ui.menu.settings.data.PersonalDataFragment;
import com.remember.app.ui.menu.settings.notification.NotificationFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Prefs.getInt("IS_THEME",0)==2){
                setTheme(R.style.AppTheme_Dark);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            setTheme(R.style.AppTheme);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        if (Prefs.getInt("IS_THEME",0)==2) {
            back.setImageResource(R.drawable.ic_back_dark_theme);
        }else {
            back.setImageResource(R.drawable.ic_back);
        }
        ViewPager viewPager = findViewById(R.id.cont);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.back)
    public void back(){
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
        adapter.addFragment(new PersonalDataFragment(), "Личные данные");
        adapter.addFragment(new NotificationFragment(), "Уведомления");
        viewPager.setAdapter(adapter);
    }
}
