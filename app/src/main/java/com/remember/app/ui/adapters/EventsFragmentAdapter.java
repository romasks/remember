package com.remember.app.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.remember.app.R;
import com.remember.app.data.models.ResponseEvents;
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

public class EventsFragmentAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private List<ResponseEvents> responseEvents = new ArrayList<>();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new EventsFragmentAdapter.EventsFragmentAdapterViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_events, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return responseEvents.size();
    }

    public void setItems(List<ResponseEvents> responseEvents) {
        this.responseEvents.addAll(responseEvents);
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void click(ResponseEvents events);

    }

    public class EventsFragmentAdapterViewHolder extends BaseViewHolder {

        @BindView(R.id.layout)
        ConstraintLayout layout;
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

        public EventsFragmentAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            layout.setOnClickListener(v -> {
                callback.click(responseEvents.get(position));
            });
            Drawable mDefaultBackground = context.getResources().getDrawable(R.drawable.darth_vader);
            try {
                if (!responseEvents.get(position).getPicture().contains("upload")){
                    Glide.with(itemView)
                            .load("http://86.57.172.88:8082/uploads/" + responseEvents.get(position).getPicture())
                            .error(mDefaultBackground)
                            .apply(RequestOptions.circleCropTransform())
                            .into(avatarImage);
                }
            } catch (Exception e){
                Glide.with(itemView)
                        .load(mDefaultBackground)
                        .error(mDefaultBackground)
                        .apply(RequestOptions.circleCropTransform())
                        .into(avatarImage);
            }

            amountDays.setVisibility(View.VISIBLE);
            String fullName = responseEvents.get(position).getName();
            name.setText(fullName);

            String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            String dateText = null;
            try {
                dateText = String.valueOf(getDifferenceDays(responseEvents.get(position).getPutdate() + "." + year));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            date.setText(responseEvents.get(position).getPutdate());
            amountDays.setText(dateText);

            comment.setText("дней осталось");
        }

        public long getDifferenceDays(String date) throws ParseException {
            @SuppressLint("SimpleDateFormat")
            Date dateResult = new SimpleDateFormat("dd.MM.yyyy").parse(date);
            Calendar past = Calendar.getInstance();
            past.setTime(dateResult);
            Calendar today = Calendar.getInstance();
            today.set(Calendar.YEAR, past.get(Calendar.YEAR));
            long diff = past.getTime().getTime() - today.getTime().getTime();
            if (diff > 0){
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            }else {
                today.set(Calendar.YEAR, past.get(Calendar.YEAR));
                past.add(Calendar.YEAR, 1);
                diff = past.getTime().getTime() - today.getTime().getTime();
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            }
        }

    }
}