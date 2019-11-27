package com.remember.app.ui.menu.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.EventNotificationModel;
import com.remember.app.data.models.NotificationModelNew;
import com.remember.app.ui.cabinet.events.EventFullActivity;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.CurrentEvent;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.utils.DividerItemDecoration;
import com.remember.app.ui.utils.MvpAppCompatFragment;
import com.remember.app.ui.utils.Utils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_FROM_NOTIF;
import static com.remember.app.data.Constants.INTENT_EXTRA_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_IS_LIST;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON;
import static com.remember.app.data.Constants.NOTIF_EVENT_TYPE_BIRTH;
import static com.remember.app.data.Constants.NOTIF_EVENT_TYPE_DEAD;
import static com.remember.app.data.Constants.NOTIF_EVENT_TYPE_DEAD_EVENT;
import static com.remember.app.data.Constants.NOTIF_EVENT_TYPE_EVENT;

public class NotificationsFragment extends MvpAppCompatFragment implements NotificationsView, NotificationListAdapter.NotificationClickListener {

    public enum Type {EVENTS, MESSAGES}

    private static final String KEY_TYPE = "key_type";
    private static final String TAG = NotificationsFragment.class.getSimpleName();

    private Type type;

    private RecyclerView recyclerView;
    private TextView textEmptyState;
    private NotificationListAdapter adapter;

    private NotificationsPresenter.NotificationFilterType filterType = NotificationsPresenter.NotificationFilterType.ALL;

    @InjectPresenter
    NotificationsPresenter presenter;

    static NotificationsFragment newInstance(Type type) {
        NotificationsFragment fragment = new NotificationsFragment();

        Bundle args = new Bundle();
        args.putSerializable(KEY_TYPE, type);

        fragment.setArguments(args);

        return fragment;
    }

    private NotificationsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = (Type) getArguments().getSerializable(KEY_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.rv_notifications);
        recyclerView.setBackgroundColor(getResources().getColor(
                Utils.isThemeDark() ? R.color.colorBlackDark : android.R.color.white
        ));

        textEmptyState = view.findViewById(R.id.text_empty_state);
        textEmptyState.setVisibility(View.GONE);
        textEmptyState.setText(type == Type.EVENTS ? "Событий нет" : "Сообщений нет");

        return view;
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

    void showFilterDialog() {
        NotificationFilterDialog.show(getFragmentManager(), filterType.ordinal()).setClickListener(filterType -> {
            NotificationsFragment.this.filterType = filterType;
            presenter.getEventNotification(filterType);

        });
    }

    private void setupRV() {
        adapter = new NotificationListAdapter();
        adapter.setClickListener(this);
        adapter.setType(Type.EVENTS);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNotificationsLoaded(List<? extends NotificationModelNew> notifications) {
//        if (Prefs.getBoolean(PREFS_KEY_SETTINGS_SHOW_NOTIFICATIONS, false)) {
        adapter.setNotificationList(notifications);
//        }
        updateEmptyState(notifications.isEmpty());
    }

    @Override
    public void onError(Throwable throwable) {
        updateEmptyState(true);
        throwable.printStackTrace();
    }

    @Override
    public void onNotificationClick(NotificationModelNew notification) {
        Utils.showSnack(recyclerView, ((EventNotificationModel) notification).getType());
        switch (((EventNotificationModel) notification).getType()) {
            case NOTIF_EVENT_TYPE_EVENT: {
                Intent intent = new Intent(getContext(), EventFullActivity.class);
                intent.putExtra(INTENT_EXTRA_EVENT_ID, ((EventNotificationModel) notification).getEventId());
                intent.putExtra(INTENT_EXTRA_FROM_NOTIF, true);
                startActivity(intent);
                break;
            }
            case NOTIF_EVENT_TYPE_BIRTH:
            case NOTIF_EVENT_TYPE_DEAD: {
                Intent intent = new Intent(getContext(), ShowPageActivity.class);
                intent.putExtra(INTENT_EXTRA_ID, ((EventNotificationModel) notification).getPageId());
                intent.putExtra(INTENT_EXTRA_PERSON, ((EventNotificationModel) notification).getPageName());
                intent.putExtra(INTENT_EXTRA_IS_LIST, true);
                startActivity(intent);
                break;
            }
            case NOTIF_EVENT_TYPE_DEAD_EVENT: {
                Intent intent = new Intent(getActivity(), CurrentEvent.class);
                intent.putExtra(INTENT_EXTRA_EVENT_ID, ((EventNotificationModel) notification).getEventId());
                intent.putExtra(INTENT_EXTRA_PERSON, ((EventNotificationModel) notification).getPageName());
                intent.putExtra(INTENT_EXTRA_FROM_NOTIF, true);
                startActivity(intent);
                break;
            }
        }
    }

    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            recyclerView.setVisibility(View.GONE);
            textEmptyState.setVisibility(View.VISIBLE);
        } else {
            textEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}
