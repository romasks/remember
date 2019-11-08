package com.remember.app.ui.menu.notifications;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.utils.Utils;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

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
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        viewPager = findViewById(R.id.viewpager);
        back = findViewById(R.id.back);
        btnFilter = findViewById(R.id.filter);
        title = findViewById(R.id.title);
        if (Utils.isThemeDark()) {
            btnFilter.setImageResource(R.drawable.ic_search2);
            back.setImageResource(R.drawable.ic_back_dark_theme);
        }
        setupViewPager();
        back.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
    }

    private void setupViewPager() {
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
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;

            case R.id.filter:
                eventsFragment.showFilterDialog();
                break;
        }
    }
}
