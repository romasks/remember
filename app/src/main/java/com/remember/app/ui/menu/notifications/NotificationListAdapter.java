package com.remember.app.ui.menu.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.NotificationModelNew;

import java.util.ArrayList;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    private NotificationsFragment.Type type;

    public interface NotificationClickListener {
        void onNotificationClick(NotificationModelNew notification);
    }

    private List<? extends NotificationModelNew> notificationList = new ArrayList<>();

    private NotificationClickListener clickListener;

    void setNotificationList(List<? extends NotificationModelNew> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    void setClickListener(NotificationClickListener clickListener) {
        this.clickListener = clickListener;
    }

    void setType(NotificationsFragment.Type type) {
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CustomTextView title;
        private CustomTextView date;

        ViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.text_event_title);
            date = v.findViewById(R.id.text_event_date);

            v.findViewById(R.id.text_show).setVisibility(type == NotificationsFragment.Type.EVENTS ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.text_show).setOnClickListener(view -> {
                if (clickListener != null)
                    clickListener.onNotificationClick(notificationList.get(getAdapterPosition()));
            });
        }

        void bind(int position) {
            NotificationModelNew notification = notificationList.get(position);

            title.setText(notification.getDisplayedText());
            date.setText(notification.getDisplayedDate());
        }

    }

}
