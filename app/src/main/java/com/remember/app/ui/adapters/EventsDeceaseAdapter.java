package com.remember.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.ui.base.BaseViewHolder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.data.Constants.BASE_URL_FROM_PHOTO;
import static com.remember.app.ui.utils.DateUtils.convertRemoteToLocalFormat;
import static com.remember.app.ui.utils.DateUtils.getDifferenceDays;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class EventsDeceaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private boolean isOwnPage = true;
    private List<RequestAddEvent> requestAddEvent = new ArrayList<>();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new EventsDeceaseAdapterViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dead_event, viewGroup, false)
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
        CustomTextView amountDays;
        @BindView(R.id.name)
        CustomTextView name;
        @BindView(R.id.date)
        CustomTextView date;
        @BindView(R.id.comment)
        CustomTextView comment;

        EventsDeceaseAdapterViewHolder(View itemView) {
            super(itemView, 0);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            constraintLayout.setOnClickListener(v -> {
                callback.openEvent(requestAddEvent.get(position).getId(), requestAddEvent.get(position).getPicture());
            });
            if (requestAddEvent.get(position).getPicture() != null && !requestAddEvent.get(position).getPicture().isEmpty()) {
                setGlideImage(itemView.getContext(), BASE_URL_FROM_PHOTO + requestAddEvent.get(position).getPicture(), avatarImage);
            } else {
                setGlideImage(itemView.getContext(), R.drawable.ic_round_camera, avatarImage);
            }
            name.setText(requestAddEvent.get(position).getName());
            if (requestAddEvent.get(position).getDate() != null) {
                amountDays.setText(getAmountDays(requestAddEvent.get(position).getDate()));
                date.setText(convertRemoteToLocalFormat(requestAddEvent.get(position).getDate()));
                comment.setText("дней осталось");
            }
            else {
                amountDays.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                comment.setVisibility(View.GONE);
            }
        }

        String getAmountDays(String data) {
            long diffs;
            try {
                diffs = getDifferenceDays(data, "REMOTE");
            } catch (ParseException e) {
                try {
                    diffs = getDifferenceDays(data, "LOCAL");
                } catch (Exception ex) {
                    diffs = 0;
                }
            }
            return String.valueOf(diffs).replace("-", "");
        }
    }
}
