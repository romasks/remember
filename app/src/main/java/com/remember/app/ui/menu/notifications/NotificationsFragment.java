package com.remember.app.ui.menu.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.NotificationModelNew;
import com.remember.app.ui.utils.DividerItemDecoration;
import com.remember.app.ui.utils.MvpAppCompatFragment;

import java.util.List;

public class NotificationsFragment extends MvpAppCompatFragment implements NotificationsView, NotificationListAdapter.NotificationClickListener {

    public enum Type {EVENTS, MESSAGES}

    private static final String KEY_TYPE = "key_type";

    private View rootView;

    private Type type;

    private NotificationListAdapter adapter;

    private NotificationsPresenter.NotificationFilterType filterType = NotificationsPresenter.NotificationFilterType.ALL;

    @InjectPresenter
    NotificationsPresenter presenter;

    static NotificationsFragment newInstance(Type type){
        NotificationsFragment fragment = new NotificationsFragment();

        Bundle args = new Bundle();
        args.putSerializable(KEY_TYPE, type);

        fragment.setArguments(args);

        return fragment;
    }

    private NotificationsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = (Type) getArguments().getSerializable(KEY_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        RecyclerView recyclerView=rootView.findViewById(R.id.rv);
        if (Prefs.getInt("IS_THEME",0)==2){
            recyclerView.setBackgroundColor(getResources().getColor(R.color.colorBlacDark));
        }else {
            recyclerView.setBackgroundColor(getResources().getColor(android.R.color.white));
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRV();

        if (type == Type.EVENTS)
            presenter.getEventNotification(filterType);
        else
            presenter.getEpitNotifications();
    }

    private void setupRV(){
        adapter = new NotificationListAdapter();
        adapter.setClickListener(this);

        RecyclerView recyclerView = rootView.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNotificationsLoaded(List<? extends NotificationModelNew> notifications) {
        adapter.setNotificationList(notifications);
    }

    @Override
    public void onNotificationClick(NotificationModelNew notification) {

    }

}
