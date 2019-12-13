package com.remember.app.ui.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.remember.app.GlideApp;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.ui.utils.ImageUtils.getBlackWhiteFilter;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoCenterInside;

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
        this.memoryPageModels = memoryPageModels;
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
        ConstraintLayout layoutGridImage;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.show_more_layout)
        ConstraintLayout layoutShowMore;
        @BindView(R.id.show_more)
        ImageView ivShowMore;

        ImageAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Point size = null;
            if (wm != null) {
                Display display = wm.getDefaultDisplay();
                size = new Point();
                display.getSize(size);

                layoutGridItem.setMinimumHeight(size.x / 3);
                layoutGridItem.setMaxHeight(size.x / 3);
            }


            // Show More btn
            layoutShowMore.setOnClickListener(v -> {
                callback.showMorePages();
            });
            if (size != null) {
                ivShowMore.setMinimumWidth(size.x / 9);
                ivShowMore.setMaxWidth(size.x / 9);
                ivShowMore.setMinimumHeight(size.x / 9);
                ivShowMore.setMaxHeight(size.x / 9);
//                ivShowMore.setPadding(size.x / 9, 0, size.x / 9, 0);
            }


            // Image
            imageView.setOnClickListener(v -> {
                callback.openPage(getItem(position));
            });
            try {
                if (getItem(position).getPicture().contains("uploads")) {
                    if (size != null) {
                        imageView.setMinimumHeight(size.x / 3);
                        imageView.setMaxHeight(size.x / 3);
                    }
                    GlideApp.with(context)
                            .load(BASE_SERVICE_URL + getItem(position).getPicture())
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .error(R.drawable.darth_vader)
                            .into(imageView);
                    imageView.setColorFilter(getBlackWhiteFilter());
                } else {
                    glideLoadIntoCenterInside(imageView.getContext(), R.drawable.darth_vader, imageView);
                }
            } catch (NullPointerException e) {
                glideLoadIntoCenterInside(imageView.getContext(), R.drawable.darth_vader, imageView);
            }
            name.setText(getItem(position).getFullName());


            if (getItem(position).isShowMore()) {
                /*layoutGrid.setBackgroundColor(context.getResources().getColor(
                        Utils.isThemeDark() ? R.color.colorBlackDark : android.R.color.white
                ));*/
                layoutGridImage.setVisibility(View.GONE);
                layoutShowMore.setVisibility(View.VISIBLE);
            } else {
                layoutGridImage.setVisibility(View.VISIBLE);
                layoutShowMore.setVisibility(View.GONE);
            }
        }
    }
}