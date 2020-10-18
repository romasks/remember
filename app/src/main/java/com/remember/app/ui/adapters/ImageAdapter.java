package com.remember.app.ui.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.utils.ImageUtils.setGridImage;
import static com.remember.app.utils.Utils.getScreenWidth;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterHolder> {

    private Callback callback;
    private List<MemoryPageModel> memoryPageModels = new ArrayList<>();
    private Point screenSize;

    public ImageAdapter(Context context) {
        screenSize = getScreenWidth(context);
    }

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
        RelativeLayout layoutGridImage;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.name)
        CustomTextView name;

        @BindView(R.id.show_more_layout)
        LinearLayout layoutShowMore;
        @BindView(R.id.show_more)
        ImageView ivShowMore;

        ImageAdapterHolder(View itemView) {
            super(itemView, 0);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            MemoryPageModel item = getItem(position);

            // Show More btn
            layoutShowMore.setOnClickListener(v -> callback.showMorePages());
            if (screenSize != null) {
                layoutShowMore.setMinimumHeight(screenSize.x / 3 + screenSize.x / 25);
                ivShowMore.setMinimumWidth(screenSize.x / 9);
                ivShowMore.setMaxWidth(screenSize.x / 9);
                ivShowMore.setMinimumHeight(screenSize.x / 9);
                ivShowMore.setMaxHeight(screenSize.x / 9);
            }

            // Image
            imageView.setOnClickListener(v -> callback.openPage(item));
            setGridImage(item.getPicture(), imageView, screenSize);
            String[] parts = item.getFullName().split(" ");
           // name.setText(parts[0] + "\n"  +parts[1]);
            name.setText(item.getFullName());

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
