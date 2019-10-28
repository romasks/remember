package com.remember.app.ui.menu.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.remember.app.R;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.ui.utils.MvpAppCompatFragment;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.Unbinder;

public class NotificationFragment extends MvpAppCompatFragment implements SettingView {

    @BindView(R.id.notifications)
    Switch notifications;
    @BindView(R.id.id_notice)
    RadioGroup idNotice;
    @BindView(R.id.id_notice_one)
    RadioButton idNoticeOne;
    @BindView(R.id.id_notice_two)
    RadioButton idNoticeTwo;
    @BindView(R.id.id_notice_three)
    RadioButton idNoticeThree;
    @BindView(R.id.days)
    MaterialSpinner days;

    private SettingPresenter presenter;
    private Unbinder unbinder;
    private String[] daysArr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    NotificationFragment(@NotNull SettingPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.settingsLiveData.observeForever(this::onReceivedInfo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_notifications, container, false);
        unbinder = ButterKnife.bind(this, view);
        days.setItems(daysArr);

        ((SettingActivity) getActivity()).setSaveButtonClickListener(v -> {
            presenter.getRequestSettings()
                    .enableNotifications(notifications.isChecked())
                    .commemorationDays(getIdNotice())
                    .amountDays(getAmountDays());
            presenter.saveSettings();
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        Log.d("NotificationFragment", "onDestroy");
    }

    @OnCheckedChanged(R.id.notifications)
    void switchNotifications() {
        idNotice.setEnabled(notifications.isChecked());
        idNoticeOne.setEnabled(notifications.isChecked());
        idNoticeTwo.setEnabled(notifications.isChecked());
        idNoticeThree.setEnabled(notifications.isChecked());
        days.setEnabled(notifications.isChecked());
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