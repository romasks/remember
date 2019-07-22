package com.remember.app.ui.menu.settings.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.remember.app.R;
import com.remember.app.ui.utils.MvpAppCompatFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NotificationFragment extends MvpAppCompatFragment implements NotificationFragmentView{

    @InjectPresenter
    NotificationFragmentPresenter presenter;

    @BindView(R.id.days)
    MaterialSpinner days;

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
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}