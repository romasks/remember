package com.remember.app.ui.menu.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;

import butterknife.BindView;

public class NotificationsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView btnFilter;
    private ViewPager viewPager;
    private ImageView back;
    private TextView title;



    private NotificationsFragment eventsFragment = NotificationsFragment.newInstance(NotificationsFragment.Type.EVENTS);

    @Override
    protected int getContentView() {
        return R.layout.activity_notifications;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Prefs.getInt("IS_THEME",0)==2) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.AppTheme_Dark);
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        viewPager = findViewById(R.id.viewpager);
        back=findViewById(R.id.back);
        btnFilter = findViewById(R.id.filter);
        title=findViewById(R.id.title);
        if (Prefs.getInt("IS_THEME",0)==2){
            btnFilter.setImageResource(R.drawable.ic_search2);
            back.setImageResource(R.drawable.ic_back_dark_theme);
        }
        setupViewPager();
        back.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
    }

    private void setupViewPager(){
        NotificationsPagerAdapter adapter = new NotificationsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(eventsFragment, "События");
        adapter.addFragment(NotificationsFragment.newInstance(NotificationsFragment.Type.MESSAGES), "Сообщения");
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                btnFilter.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                onBackPressed();
                break;

            case R.id.filter:
                eventsFragment.showFilterDialog();
                break;
        }
    }
}
