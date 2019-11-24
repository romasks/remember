package com.remember.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.remember.app.R;
import com.remember.app.data.models.ResponseImagesSlider;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

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

    public class PhotoSliderAdapterHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        ImageView imageView;

        PhotoSliderAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            try {
                setGlideImage(context, BASE_SERVICE_URL + responseImagesSliders.get(position).getPicture(), imageView);
                imageView.setOnClickListener(this);
            } catch (Exception e) {
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