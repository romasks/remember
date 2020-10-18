package com.remember.app.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.EventResponse;
import com.remember.app.ui.base.BaseViewHolder;
import com.remember.app.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.utils.ImageUtils.setGlideImage;

public class EventsFragmentAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private List<EventResponse> responseEvents = new ArrayList<>();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new EventsFragmentAdapterViewHolder(
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
        CustomTextView name;
        @BindView(R.id.date)
        CustomTextView date;
        @BindView(R.id.personName)
        CustomTextView personName;
        @BindView(R.id.global_layout)
        LinearLayout globalLayout;

        EventsFragmentAdapterViewHolder(View itemView) {
            super(itemView, 0);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            EventResponse item = responseEvents.get(position);
            /*layout.setOnClickListener(v -> {
                callback.click(item);
            });*/
            globalLayout.setOnClickListener(v -> {
                callback.click(item);
            });
            Drawable mDefaultBackground = context.getResources().getDrawable(R.drawable.no_photo);
            try {
                if (!responseEvents.get(position).getPicture().equals(""))
                    setGlideImage(responseEvents.get(position).getPicture(), avatarImage);
                else
                    setGlideImage(itemView.getContext(), mDefaultBackground, avatarImage);
            } catch (Exception e) {
                setGlideImage(itemView.getContext(), mDefaultBackground, avatarImage);
            }

            String fullName = item.getPageName();
            if (item.getEventName().equals("День смерти")) {
                name.setText("День утраты");
            } else {
                name.setText(item.getEventName());
            }
            personName.setText(fullName);
            date.setText(DateUtils.convertRemoteToLocalFormat(item.getOriginDate()));
        }
    }
}
