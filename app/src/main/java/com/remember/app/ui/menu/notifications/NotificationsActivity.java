package com.remember.app.ui.menu.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;

public class NotificationsActivity extends BaseActivity implements View.OnClickListener {

    private View btnFilter;

    @Override
    protected int getContentView() {
        return R.layout.activity_notifications;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViewPager();

        btnFilter = findViewById(R.id.filter);

        findViewById(R.id.back).setOnClickListener(this);
        btnFilter.setOnClickListener(this);
    }

    private void setupViewPager(){
        NotificationsPagerAdapter adapter = new NotificationsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(NotificationsFragment.newInstance(NotificationsFragment.Type.EVENTS), "События");
        adapter.addFragment(NotificationsFragment.newInstance(NotificationsFragment.Type.MESSAGES), "Сообщения");

        ViewPager viewPager = findViewById(R.id.viewpager);
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
                break;
        }
    }
}
