package com.remember.app.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.remember.app.R;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.ui.base.BaseViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.ui.utils.ImageUtils.setBlackWhite;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class EventsDeceaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private boolean isOwnPage = true;
    private List<RequestAddEvent> requestAddEvent = new ArrayList<>();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new EventsDeceaseAdapter.EventsDeceaseAdapterViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_events, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return requestAddEvent.size();
    }

    public void setItems(List<RequestAddEvent> requestAddEvent) {
        this.requestAddEvent.clear();
        if (isOwnPage) {
            this.requestAddEvent.addAll(requestAddEvent);
        } else {
            for (RequestAddEvent event : requestAddEvent) {
                if (event.getFlag() == 1) this.requestAddEvent.add(event);
            }
        }
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setIsOwnPage(boolean isOwnPage) {
        this.isOwnPage = isOwnPage;
    }

    public interface Callback {

        void openEvent(Integer pageId, String imageUrl);

    }

    public class EventsDeceaseAdapterViewHolder extends BaseViewHolder {

        @BindView(R.id.layout)
        ConstraintLayout constraintLayout;
        @BindView(R.id.avatar_image)
        ImageView avatarImage;
        @BindView(R.id.amount_days)
        TextView amountDays;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.comment)
        TextView comment;

        EventsDeceaseAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            constraintLayout.setOnClickListener(v -> {
                callback.openEvent(requestAddEvent.get(position).getId(), requestAddEvent.get(position).getPicture());
            });
            if (!requestAddEvent.get(position).getPicture().isEmpty()) {
                setGlideImage(itemView.getContext(), BASE_SERVICE_URL + requestAddEvent.get(position).getPicture(), avatarImage);
                setBlackWhite(avatarImage);
            } else {
                setGlideImage(itemView.getContext(), R.drawable.ic_round_camera, avatarImage);
            }
            name.setText(requestAddEvent.get(position).getName());
            try {
                String days = String.valueOf(getDifferenceDays(requestAddEvent.get(position).getDate()));
                amountDays.setText(days.replace("-", ""));
            } catch (ParseException ignored) {
                try {
                    String days = String.valueOf(getDifferenceDaysOtherDate(requestAddEvent.get(position).getDate()));
                    amountDays.setText(days.replace("-", ""));
                } catch (Exception e) {
                }
            }

            DateFormat dfLocal = new SimpleDateFormat("dd.MM.yyyy");
            DateFormat dfRemote = new SimpleDateFormat("yyyy-MM-dd");

            Date serverDate = null;
            try {
                serverDate = dfRemote.parse(requestAddEvent.get(position).getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (serverDate != null) {
                date.setText(dfLocal.format(serverDate));
            }

//            date.setText(requestAddEvent.get(position).getDate());
            comment.setText("дней осталось");
        }

        long getDifferenceDaysOtherDate(String date) throws ParseException {
            @SuppressLint("SimpleDateFormat")
            Date dateResult = new SimpleDateFormat("dd.MM.yyyy").parse(date);
            Calendar past = Calendar.getInstance();
            past.setTime(dateResult);
            Calendar today = Calendar.getInstance();
            today.set(Calendar.YEAR, past.get(Calendar.YEAR));
            if (!today.after(past)) {
                long diff = today.getTime().getTime() - past.getTime().getTime();
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            } else {
                today.set(Calendar.YEAR, past.get(Calendar.YEAR) - 1);
                long diff = past.getTime().getTime() - today.getTime().getTime();
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            }
        }

        long getDifferenceDays(String date) throws ParseException {
            @SuppressLint("SimpleDateFormat")
            Date dateResult = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            Calendar past = Calendar.getInstance();
            past.setTime(dateResult);
            Calendar today = Calendar.getInstance();
            today.set(Calendar.YEAR, past.get(Calendar.YEAR));
            if (!today.after(past)) {
                long diff = today.getTime().getTime() - past.getTime().getTime();
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            } else {
                today.set(Calendar.YEAR, past.get(Calendar.YEAR) - 1);
                long diff = past.getTime().getTime() - today.getTime().getTime();
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            }
        }

    }
}