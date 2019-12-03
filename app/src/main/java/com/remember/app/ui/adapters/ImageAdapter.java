package com.remember.app.ui.adapters;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.remember.app.GlideApp;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.ui.utils.ImageUtils.getBlackWhiteFilter;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoCenterInside;

public class ImageAdapter extends PagedListAdapter<MemoryPageModel, ImageAdapter.ImageAdapterHolder> {

    private Context context;
    private Callback callback;

    public ImageAdapter() {
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

    public void setItemsSearch(List<MemoryPageModel> memoryPageModels) {
        //this.memoryPageModels.clear();
        //this.memoryPageModels.addAll(memoryPageModels);
        //notifyDataSetChanged();
    }

    public interface Callback {

        void openPage(MemoryPageModel memoryPageModel);

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
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progress.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progress.setVisibility(View.GONE);
                                    return false;
                                }
                            })
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