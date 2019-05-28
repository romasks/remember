package com.remember.app.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EpitaphsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private List<MemoryPageModel> memoryPageModelList = new ArrayList<>();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new EpitaphsAdapter.EpitaphsAdapterViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_epitaphs, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return memoryPageModelList.size();
    }

    public void setItems(List<MemoryPageModel> memoryPageModelList) {
        this.memoryPageModelList.addAll(memoryPageModelList);
        notifyDataSetChanged();
    }

    public class EpitaphsAdapterViewHolder extends BaseViewHolder {

        @BindView(R.id.avatar_image)
        ImageView avatarImage;
        @BindView(R.id.amount_days)
        TextView amountDays;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.comment)
        TextView comment;

        public EpitaphsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
        }
    }
}