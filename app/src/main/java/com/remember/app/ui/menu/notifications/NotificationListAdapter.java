package com.remember.app.ui.menu.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.remember.app.R;
import com.remember.app.data.models.NotificationModelNew;

import java.util.ArrayList;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

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

        private TextView title;
        private TextView date;

        ViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.text_event_title);
            date = v.findViewById(R.id.text_event_date);

            v.findViewById(R.id.item_tile).setOnClickListener(view -> {
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
