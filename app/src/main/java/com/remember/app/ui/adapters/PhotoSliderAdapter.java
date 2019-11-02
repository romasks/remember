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
import com.remember.app.R;
import com.remember.app.data.models.ResponseImagesSlider;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;

public class PhotoSliderAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private List<ResponseImagesSlider> responseImagesSliders = new ArrayList<>();
    private ItemClickListener mClickListener;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoSliderAdapter.PhotoSliderAdapterHolder(
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

    public void setItems(List<ResponseImagesSlider> responseImagesSliders) {
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

    public class PhotoSliderAdapterHolder extends BaseViewHolder implements View.OnClickListener{

        @BindView(R.id.image)
        ImageView imageView;

        public PhotoSliderAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            try {
                Glide.with(context)
                        .load(BASE_SERVICE_URL + responseImagesSliders.get(position).getPicture())
                        .circleCrop()
                        .into(imageView);
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                imageView.setColorFilter(filter);
                imageView.setOnClickListener(this);
            } catch (Exception e){}
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());        }
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}