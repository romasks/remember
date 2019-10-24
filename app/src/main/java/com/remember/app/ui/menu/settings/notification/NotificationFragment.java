package com.remember.app.ui.menu.settings.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.widget.AppCompatRadioButton;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.ui.utils.MvpAppCompatFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NotificationFragment extends MvpAppCompatFragment implements NotificationFragmentView{

    @InjectPresenter
    NotificationFragmentPresenter presenter;

    @BindView(R.id.days_sp)
    MaterialSpinner days;
@BindView(R.id.notifications)
    Switch notif;
@BindView(R.id.one)
    AppCompatRadioButton but_1;
    @BindView(R.id.two)
    AppCompatRadioButton but_2;
    @BindView(R.id.three)
    AppCompatRadioButton but_3;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings_notifications, container, false);
        unbinder = ButterKnife.bind(this, v);
        String [] daysArr = {"","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        days.setItems(daysArr);
        if (Prefs.getInt("IS_THEME",0)==0||Prefs.getInt("IS_THEME",0)==1){
            notif.setTextColor(getResources().getColor(R.color.gray));
            but_1.setTextColor(getResources().getColor(R.color.gray));
            but_2.setTextColor(getResources().getColor(R.color.gray));
            but_3.setTextColor(getResources().getColor(R.color.gray));
            days.setTextColor(getResources().getColor(R.color.gray));
            days.setHintTextColor(getResources().getColor(R.color.gray));
        }else if (Prefs.getInt("IS_THEME",0)==2){
            notif.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            but_1.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            but_2.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            but_3.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            days.setHintTextColor(getResources().getColor(R.color.colorWhiteDark));
            days.setTextColor(getResources().getColor(R.color.colorWhiteDark));


        }
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}