package com.remember.app.ui.adapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.remember.app.R;
import com.remember.app.ui.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventStuffAdapter extends RecyclerView.Adapter<EventStuffAdapter.ImageViewHolder> {

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_stuff_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

    }

    public class ImageViewHolder extends BaseViewHolder {
        Context context;

        @BindView(R.id.item_image)
        ImageView itemImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            Glide.with(itemView)
                    .load(R.drawable.darth_vader)
                    .apply(RequestOptions.circleCropTransform())
                    .into(itemImage);

            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            itemImage.setColorFilter(filter);
        }
    }
}
