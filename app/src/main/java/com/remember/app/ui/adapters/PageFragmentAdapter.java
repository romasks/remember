package com.remember.app.ui.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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

public class PageFragmentAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private List<MemoryPageModel> memoryPageModelList = new ArrayList<>();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PageFragmentAdapter.PageFragmentAdapterViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_page_fragment, viewGroup, false)
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

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void sendItem(MemoryPageModel person);

    }

    public class PageFragmentAdapterViewHolder extends BaseViewHolder {

        @BindView(R.id.avatar_image)
        ImageView avatarImage;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.comment)
        TextView comment;
        @BindView(R.id.layout)
        ConstraintLayout layout;

        public PageFragmentAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            layout.setOnClickListener(v -> {
                callback.sendItem(memoryPageModelList.get(position));
            });
            Glide.with(itemView)
                    .load("http://86.57.172.88:8082" + memoryPageModelList.get(position).getPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImage);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            avatarImage.setColorFilter(filter);
            String fullName = memoryPageModelList.get(position).getSecondname() + " " +
                     memoryPageModelList.get(position).getName() + " " + memoryPageModelList.get(position).getThirtname();
            name.setText(fullName);

            String dateText = memoryPageModelList.get(position).getDatarod() + " - " + memoryPageModelList.get(position).getDatasmert();
            date.setText(dateText);
        }
    }
}
