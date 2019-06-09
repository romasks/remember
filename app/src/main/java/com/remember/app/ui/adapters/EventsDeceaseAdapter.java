package com.remember.app.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsDeceaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
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

    public class EventsDeceaseAdapterViewHolder extends BaseViewHolder {

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
            Glide.with(itemView)
                    .load("https://images-na.ssl-images-amazon.com/images/I/61flr%2BuHRpL._SX425_.jpg")
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImage);
            name.setText(requestAddEvent.get(position).getName());
            try {
                String days = String.valueOf(getDifferenceDays(requestAddEvent.get(position).getDate()));
                amountDays.setText(days);
            } catch (ParseException ignored) { }
            date.setText(requestAddEvent.get(position).getDate());
            comment.setText("дней осталось");
        }

        long getDifferenceDays(String date) throws ParseException {
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

    }
}