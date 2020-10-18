package com.remember.app.ui.menu.notifications;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.NotificationModelNew;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnPageChange;

import static butterknife.OnPageChange.Callback.PAGE_SELECTED;

public class NotificationsActivity extends BaseActivity implements NotificationsView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.filter)
    ImageView btnFilter; //temporarily comment
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private NotificationsFragment eventsFragment = NotificationsFragment.newInstance(NotificationsFragment.Type.EVENTS);

    @Override
    protected int getContentView() {
        return R.layout.activity_notifications;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);

        if (Utils.isThemeDark()) {
            btnFilter.setImageResource(R.drawable.ic_search2); //temporarily comment
            back.setImageResource(R.drawable.ic_back_dark_theme);
        }
        setupViewPager();
    }

    private void setupViewPager() {
        NotificationsPagerAdapter adapter = new NotificationsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(eventsFragment, "События");
        adapter.addFragment(NotificationsFragment.newInstance(NotificationsFragment.Type.MESSAGES), "Сообщения");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont(tabLayout);
        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });*/
    }

    @OnPageChange(value = R.id.viewpager, callback = PAGE_SELECTED)
    public void onPageSelected(int position) {
        btnFilter.setVisibility(position == 0 ? View.VISIBLE : View.GONE); //temporarily comment
    }

    @OnClick(R.id.back)
    public void onBackArrowClick() {
        onBackPressed();
    }

    @OnClick(R.id.filter)
    public void onFilterButtonClick() {
        eventsFragment.showFilterDialog();
    } //temporarily comment

    @Override
    public void onNotificationsLoaded(List<? extends NotificationModelNew> notifications) {
        // not implemented
    }

    @Override
    public void onError(Throwable throwable) {
        // not implemented
    }

    private void changeTabsFont(TabLayout tabBar) {
        List<String> arr = new ArrayList<>();
        arr.add("События");
        arr.add("Сообщения");
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
            return (textView.getTextSize() / getResources().getDisplayMetrics().density - 2);
        else {
            return (textView.getTextSize() / getResources().getDisplayMetrics().density + 1);
        }
    }
}
