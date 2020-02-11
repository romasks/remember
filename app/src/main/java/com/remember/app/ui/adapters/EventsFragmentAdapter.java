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

import com.remember.app.R;
import com.remember.app.data.models.EventResponse;
import com.remember.app.ui.base.BaseViewHolder;
import com.remember.app.ui.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class EventsFragmentAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private List<EventResponse> responseEvents = new ArrayList<>();

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

    public void setItems(List<EventResponse> responseEvents) {
        this.responseEvents.clear();
        this.responseEvents.addAll(responseEvents);
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void click(EventResponse events);

    }

    public class EventsFragmentAdapterViewHolder extends BaseViewHolder {

        @BindView(R.id.layout)
        ConstraintLayout layout;
        @BindView(R.id.avatar_image)
        ImageView avatarImage;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.personName)
        TextView personName;

        EventsFragmentAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            EventResponse item = responseEvents.get(position);
            layout.setOnClickListener(v -> {
                callback.click(item);
            });
            Drawable mDefaultBackground = context.getResources().getDrawable(R.drawable.darth_vader);
            try {
                setGlideImage(responseEvents.get(position).getPicture(), avatarImage);
            } catch (Exception e) {
                setGlideImage(itemView.getContext(), mDefaultBackground, avatarImage);
            }

            String fullName = item.getPageName();
            if (item.getEventName().equals("День смерти")){
                name.setText("День утраты");
            } else {
                name.setText(item.getEventName());
            }
            personName.setText(fullName);
            date.setText(DateUtils.convertRemoteToLocalFormat(item.getOriginDate()));
        }

        long getDifferenceDays(String date) throws ParseException {
            @SuppressLint("SimpleDateFormat")
            Date dateResult = new SimpleDateFormat("dd.MM.yyyy").parse(date);
            Calendar past = Calendar.getInstance();
            past.setTime(dateResult);
            Calendar today = Calendar.getInstance();
            today.set(Calendar.YEAR, past.get(Calendar.YEAR));
            long diff = past.getTime().getTime() - today.getTime().getTime();
            if (diff > 0) {
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            } else {
                today.set(Calendar.YEAR, past.get(Calendar.YEAR));
                past.add(Calendar.YEAR, 1);
                diff = past.getTime().getTime() - today.getTime().getTime();
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            }
        }

    }
}
