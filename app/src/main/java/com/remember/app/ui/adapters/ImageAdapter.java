package com.remember.app.ui.adapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private List<MemoryPageModel> memoryPageModels = new ArrayList<>();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageAdapter.ImageAdapterHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_grid, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return memoryPageModels.size();
    }

    public void setItems(List<MemoryPageModel> memoryPageModels) {
        this.memoryPageModels.clear();
        this.memoryPageModels.addAll(memoryPageModels);
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void openPage(MemoryPageModel memoryPageModel);

    }

    public class ImageAdapterHolder extends BaseViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.name)
        TextView name;

        public ImageAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            imageView.setOnClickListener(v -> {
                callback.openPage(memoryPageModels.get(position));
            });
            try {
                if (memoryPageModels.get(position).getPicture().contains("uploads")){
                    Glide.with(context)
                            .load("http://86.57.172.88:8082" + memoryPageModels.get(position).getPicture())
                            .error(R.drawable.darth_vader)
                            .into(imageView);
                } else {
                    Glide.with(context)
                            .load(R.drawable.darth_vader)
                            .centerInside()
                            .into(imageView);
                }
            } catch (NullPointerException e){
                Glide.with(context)
                        .load(R.drawable.darth_vader)
                        .centerInside()
                        .into(imageView);
            }
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            imageView.setColorFilter(filter);
            String nameString = memoryPageModels.get(position).getSecondname() + " " + memoryPageModels.get(position).getName();
            name.setText(nameString);
        }
    }
}