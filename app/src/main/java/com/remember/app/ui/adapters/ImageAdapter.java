package com.remember.app.ui.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoCenterInside;
import static com.remember.app.ui.utils.ImageUtils.setGridImage;
import static com.remember.app.ui.utils.Utils.getScreenWidth;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterHolder> {

    private Context context;
    private Callback callback;
    private List<MemoryPageModel> memoryPageModels = new ArrayList<>();

    @NonNull
    @Override
    public ImageAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageAdapterHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_grid_static, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapterHolder holder, int position) {
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

    public List<MemoryPageModel> getItems() {
        return memoryPageModels;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public interface Callback {
        void openPage(MemoryPageModel memoryPageModel);

        void showMorePages();
    }

    private MemoryPageModel getItem(int position) {
        return memoryPageModels.get(position);
    }

    public class ImageAdapterHolder extends BaseViewHolder {

        @BindView(R.id.item_grid_layout)
        ConstraintLayout layoutGridItem;

        @BindView(R.id.grid_image_layout)
        LinearLayout layoutGridImage;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.show_more_layout)
        LinearLayout layoutShowMore;
        @BindView(R.id.show_more)
        ImageView ivShowMore;

        ImageAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            MemoryPageModel item = getItem(position);

            Point size = getScreenWidth(context);
            if (size != null) {
//                layoutGridItem.setMinimumHeight(size.x / 3 + size.x / 25);
            }

            // Show More btn
            layoutShowMore.setOnClickListener(v -> callback.showMorePages());
            if (size != null) {
                layoutShowMore.setMinimumHeight(size.x / 3 + size.x / 25);
                ivShowMore.setMinimumWidth(size.x / 9);
                ivShowMore.setMaxWidth(size.x / 9);
                ivShowMore.setMinimumHeight(size.x / 9);
                ivShowMore.setMaxHeight(size.x / 9);
            }

            // Image
            imageView.setOnClickListener(v -> callback.openPage(item));
            try {
                setGridImage(item.getPicture(), imageView, size);
            } catch (NullPointerException e) {
                glideLoadIntoCenterInside(imageView.getContext(), R.drawable.darth_vader, imageView);
            }
            name.setText(item.getFullName());
            //item.setLoaded(true);

            if (item.isShowMore()) {
                layoutGridImage.setVisibility(View.GONE);
                layoutShowMore.setVisibility(View.VISIBLE);
            } else {
                layoutGridImage.setVisibility(View.VISIBLE);
                layoutShowMore.setVisibility(View.GONE);
            }
        }
    }
}