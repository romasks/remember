package com.remember.app.ui.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.remember.app.GlideApp;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseViewHolder;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.ui.utils.ImageUtils.getBlackWhiteFilter;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoCenterInside;

public class ImagePagedAdapter extends PagedListAdapter<MemoryPageModel, ImagePagedAdapter.ImageAdapterHolder> {

    private Context context;
    private Callback callback;

    public ImagePagedAdapter() {
        super(MemoryPageModel.DIFF_MEMORY_PAGE_CALLBACK);
    }

    @NonNull
    @Override
    public ImageAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageAdapterHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_grid, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapterHolder holder, int position) {
        holder.onBind(position);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public interface Callback {

        void openPageFromPagedAdapter(MemoryPageModel memoryPageModel);

    }

    public class ImageAdapterHolder extends BaseViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.progress)
        ProgressBar progress;

        ImageAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            progress.setVisibility(View.GONE);
            imageView.setOnClickListener(v -> {
                callback.openPageFromPagedAdapter(getItem(position));
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