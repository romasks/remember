package com.remember.app.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.remember.app.R;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.ui.base.BaseViewHolder;

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

public class EventsDeceaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
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
        this.requestAddEvent.addAll(requestAddEvent);
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void openEvent(Integer pageId);

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

        public EventsDeceaseAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            constraintLayout.setOnClickListener(v -> {
                callback.openEvent(requestAddEvent.get(position).getId());
            });
            if (!requestAddEvent.get(position).getPicture().isEmpty()) {
                Glide.with(itemView)
                        .load(BASE_SERVICE_URL + requestAddEvent.get(position).getPicture())
                        .apply(RequestOptions.circleCropTransform())
                        .into(avatarImage);
            } else {
                Glide.with(itemView)
                        .load(R.drawable.ic_round_camera)
                        .apply(RequestOptions.circleCropTransform())
                        .into(avatarImage);
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
            date.setText(requestAddEvent.get(position).getDate());
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