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

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoSliderAdapterHolder> {

    private Context context;
    private ArrayList<EventSliderPhotos> responseImagesSliders;
    private ItemClickListener mClickListener;

    public PhotoAdapter(ArrayList<EventSliderPhotos> list){
        responseImagesSliders = list;
    }

    @NonNull
    @Override
    public PhotoSliderAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoSliderAdapterHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_photo, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoSliderAdapterHolder holder, int position) {
        holder.onBind(position, responseImagesSliders);
    }

    public ArrayList<EventSliderPhotos> getList(){
        return responseImagesSliders;
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

    public class PhotoSliderAdapterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView imageView;

        PhotoSliderAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void onBind(int position, ArrayList<EventSliderPhotos> list) {
            try {
                setGlideImage(context, BASE_SERVICE_URL + responseImagesSliders.get(position).getPicture(), imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition(),list );
                    }
                });
            } catch (Exception ignored) {
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, ArrayList<EventSliderPhotos> list);
    }
}
