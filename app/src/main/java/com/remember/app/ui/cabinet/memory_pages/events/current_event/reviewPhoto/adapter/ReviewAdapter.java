package com.remember.app.ui.cabinet.memory_pages.events.current_event.reviewPhoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.remember.app.R;
import com.remember.app.data.models.EventSliderPhotos;
import com.remember.app.data.models.ResponseImagesSlider;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.data.Constants.BASE_URL_FROM_PHOTO;
import static com.remember.app.utils.ImageUtils.setGlideImage;

public class ReviewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private ArrayList<EventSliderPhotos> responseImagesSliders = new ArrayList<>();
    private ItemClickListener mClickListener;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewAdapterHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_photo, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return responseImagesSliders.size();
    }

    public ArrayList<EventSliderPhotos> getItems() {
        return responseImagesSliders;
    }

    public void setItems(ArrayList<EventSliderPhotos> responseImagesSliders) {
        this.responseImagesSliders.clear();
        this.responseImagesSliders.addAll(responseImagesSliders);
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void openPage(ResponseImagesSlider responseImagesSlider);

    }

    public class ReviewAdapterHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        ImageView imageView;

        ReviewAdapterHolder(View itemView) {
            super(itemView, 0);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            try {
                setGlideImage(context, BASE_URL_FROM_PHOTO + responseImagesSliders.get(position).getPicture(), imageView);
                imageView.setOnClickListener(this);
            } catch (Exception ignored) {
            }
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

