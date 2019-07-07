package com.remember.app.ui.adapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseViewHolderPagination;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolderPagination> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private Context context;
    private ImageAdapter.Callback callback;

    private List<MemoryPageModel> memoryPageModels = new ArrayList<>();

    @Override
    public BaseViewHolderPagination onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_grid, parent, false));
            case VIEW_TYPE_LOADING:
                return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolderPagination holder, int position) {
        holder.onBind(position);
    }


    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == memoryPageModels.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return memoryPageModels == null ? 0 : memoryPageModels.size();
    }

    public void add(MemoryPageModel memoryPageModel) {
        memoryPageModels.add(memoryPageModel);
        notifyItemInserted(memoryPageModels.size() - 1);
    }

    public void addAll(List<MemoryPageModel> memoryPageModel) {
        for (MemoryPageModel response : memoryPageModel) {
            add(response);
        }
    }

    private void remove(MemoryPageModel memoryPageModel) {
        int position = memoryPageModels.indexOf(memoryPageModel);
        if (position > -1) {
            memoryPageModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new MemoryPageModel());
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = memoryPageModels.size() - 1;
        MemoryPageModel item = getItem(position);
        if (item != null) {
            memoryPageModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    MemoryPageModel getItem(int position) {
        return memoryPageModels.get(position);
    }

    public void setCallback(ImageAdapter.Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void openPage(MemoryPageModel memoryPageModel);

    }


    public class ViewHolder extends BaseViewHolderPagination {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.name)
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            imageView.setOnClickListener(v -> {
                callback.openPage(memoryPageModels.get(position));
            });
            try {
                if (memoryPageModels.get(position).getPicture().contains("uploads")) {
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
            } catch (NullPointerException e) {
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

    public class FooterHolder extends BaseViewHolderPagination {

        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;


        FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

    }

}