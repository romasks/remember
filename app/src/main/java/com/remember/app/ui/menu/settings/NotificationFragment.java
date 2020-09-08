package com.remember.app.ui.menu.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.remember.app.R;
import com.remember.app.customView.CustomRadioButton;
import com.remember.app.customView.CustomSwitch;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.ui.utils.Utils;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class NotificationFragment extends SettingsBaseFragment implements SettingView {

    @BindView(R.id.notifications)
    CustomSwitch notifications;
    @BindView(R.id.id_notice)
    RadioGroup idNotice;
    @BindView(R.id.id_notice_one)
    CustomRadioButton idNoticeOne;
    @BindView(R.id.id_notice_two)
    CustomRadioButton idNoticeTwo;
    @BindView(R.id.id_notice_three)
    CustomRadioButton idNoticeThree;
    @BindView(R.id.days_sp)
    MaterialSpinner days;

    private SettingPresenter presenter;
    private String[] daysArr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    public NotificationFragment() {
    }

    NotificationFragment(@NotNull SettingPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_settings_notifications;
    }

    @Override
    protected void setUp() {
        days.setItems(daysArr);
        days.setSelectedIndex(0);

        int textColor = Utils.isThemeDark()
                ? getResources().getColor(R.color.colorWhiteDark)
                : getResources().getColor(R.color.gray);

        notifications.setTextColor(textColor);
        idNoticeOne.setTextColor(textColor);
        idNoticeTwo.setTextColor(textColor);
        idNoticeThree.setTextColor(textColor);
        days.setHintTextColor(textColor);
        days.setTextColor(textColor);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (presenter != null) {
            presenter.getSettingsLiveData().observeForever(this::onReceivedInfo);
        }
    }

    void onSaveClick() {
        if (notifications != null)
            presenter.getRequestSettings()
                    .enableNotifications(notifications.isChecked())
                    .commemorationDays(getIdNotice())
                    .amountDays(getAmountDays());
    }

    @OnCheckedChanged(R.id.notifications)
    void switchNotifications() {
        idNotice.setEnabled(notifications.isChecked());
        idNoticeOne.setEnabled(notifications.isChecked());
        idNoticeTwo.setEnabled(notifications.isChecked());
        idNoticeThree.setEnabled(notifications.isChecked());
        days.setEnabled(notifications.isChecked());
    }

    @OnClick(R.id.id_notice_one)
    void setNoticeOne() {
        idNotice.check(R.id.id_notice_one);
        presenter.getRequestSettings().commemorationDays(getIdNotice());
    }

    @OnClick(R.id.id_notice_two)
    void setNoticeTwo() {
        idNotice.check(R.id.id_notice_two);
        presenter.getRequestSettings().commemorationDays(getIdNotice());
    }

    @OnClick(R.id.id_notice_three)
    void setNoticeThree() {
        idNotice.check(R.id.id_notice_three);
        presenter.getRequestSettings().commemorationDays(getIdNotice());
    }

    private int getIdNotice() {
        switch (idNotice.getCheckedRadioButtonId()) {
            case R.id.id_notice_two:
                return 1;
            case R.id.id_notice_three:
                return 2;
            case R.id.id_notice_one:
            default:
                return 3;
        }
    }

    private void setIdNotice(Integer idNoticeValue) {
        switch (idNoticeValue) {
            case 1: {
                idNotice.check(R.id.id_notice_two);
                break;
            }
            case 2: {
                idNotice.check(R.id.id_notice_three);
                break;
            }
            case 3: {
                idNotice.check(R.id.id_notice_one);
                break;
            }
            default:
        }
    }

    private Integer getAmountDays() {
        return Integer.valueOf(daysArr[days.getSelectedIndex()]);
    }

    private void onReceivedInfo(ResponseSettings responseSettings) {
        notifications.setChecked(responseSettings.getNotificationsEnabled() == 1);
        setIdNotice(responseSettings.getIdNotice());
        days.setSelectedIndex(responseSettings.getAmountDays() - 1);
        switchNotifications();
    }

    @Override
    public void error(Throwable throwable) {
        // placeholder
    }

    @Override
    public void onSaved(Object o) {
        // placeholder
    }

    @Override
    public void onSavedImage(Object o) {
        // placeholder
    }
}