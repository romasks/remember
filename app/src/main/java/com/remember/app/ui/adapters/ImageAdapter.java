package com.remember.app.ui.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.remember.app.GlideApp;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseViewHolder;
import com.remember.app.ui.utils.Utils;

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

        @BindView(R.id.show_more)
        ImageView ivShowMore;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.progress)
        ProgressBar progress;
        @BindView(R.id.show_more_layout)
        ConstraintLayout layoutShowMore;
        @BindView(R.id.grid_image_layout)
        ConstraintLayout layoutGridImage;
        @BindView(R.id.grid_layout)
        GridLayout layoutGrid;

        ImageAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            progress.setVisibility(View.GONE);

            if (position == 14) {
                layoutGrid.setBackgroundColor(context.getResources().getColor(
                        Utils.isThemeDark() ? R.color.colorBlackDark : android.R.color.white
                ));

                layoutGridImage.setVisibility(View.GONE);
                layoutShowMore.setVisibility(View.VISIBLE);

                layoutShowMore.setOnClickListener(v -> {
                    callback.showMorePages();
                });
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if (wm != null) {
                    Display display = wm.getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    ivShowMore.setMinimumWidth(size.x / 9);
                    ivShowMore.setMaxWidth(size.x / 9);
                    ivShowMore.setMinimumHeight(size.x / 9);
                    ivShowMore.setMaxHeight(size.x / 9);
                    ivShowMore.setPadding(size.x / 9, 0, size.x / 9, 0);
                }

                return;
            }

            imageView.setOnClickListener(v -> {
                callback.openPage(getItem(position));
            });
            try {
                if (getItem(position).getPicture().contains("uploads")) {
                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    if (wm != null) {
                        Display display = wm.getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
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

            String nameString = getItem(position).getName() + " " + getItem(position).getSecondName();
            name.setText(nameString);
        }
    }
}