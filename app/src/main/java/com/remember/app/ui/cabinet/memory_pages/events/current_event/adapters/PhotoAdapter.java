package com.remember.app.ui.cabinet.memory_pages.events.current_event.adapters;

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
import com.remember.app.ui.adapters.PhotoSliderAdapter;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class PhotoAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private PhotoSliderAdapter.Callback callback;
    private ArrayList<EventSliderPhotos> responseImagesSliders = new ArrayList<>();
    private PhotoSliderAdapter.ItemClickListener mClickListener;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoAdapter.PhotoSliderAdapterHolder(
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

    public ArrayList<EventSliderPhotos> getItems(){
        return responseImagesSliders;
    }

    public void setItems(ArrayList<EventSliderPhotos> responseImagesSliders) {
        this.responseImagesSliders.clear();
        this.responseImagesSliders.addAll(responseImagesSliders);
        notifyDataSetChanged();
    }

    public void setCallback(PhotoSliderAdapter.Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void openPage(ResponseImagesSlider responseImagesSlider);

    }

    public class PhotoSliderAdapterHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        ImageView imageView;

        PhotoSliderAdapterHolder(View itemView) {
            super(itemView, 0);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            try {
                setGlideImage(context, BASE_SERVICE_URL + responseImagesSliders.get(position).getPicture(), imageView);
                imageView.setOnClickListener(this);
            } catch (Exception ignored) {
            }
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void setClickListener(PhotoSliderAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
