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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            try {
                if (memoryPageModelList.get(position).getPicture().contains("uploads")){
                    Glide.with(itemView)
                            .load("http://86.57.172.88:8082" + memoryPageModelList.get(position).getPicture())
                            .apply(RequestOptions.circleCropTransform())
                            .into(avatarImage);
                } else {
                    Glide.with(context)
                            .load(R.drawable.darth_vader)
                            .apply(RequestOptions.circleCropTransform())
                            .into(avatarImage);
                }
            } catch (NullPointerException e){
                Glide.with(context)
                        .load(R.drawable.darth_vader)
                        .apply(RequestOptions.circleCropTransform())
                        .into(avatarImage);
            }

            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            avatarImage.setColorFilter(filter);
            String fullName = memoryPageModelList.get(position).getSecondname() + " " +
                     memoryPageModelList.get(position).getName() + " " + memoryPageModelList.get(position).getThirtname();
            name.setText(fullName);

            try {
                Date dateBegin = new SimpleDateFormat("yyyy-MM-dd").parse(memoryPageModelList.get(position).getDatarod());
                Date dateEnd = new SimpleDateFormat("yyyy-MM-dd").parse(memoryPageModelList.get(position).getDatasmert());
                DateFormat first = new SimpleDateFormat("dd.MM.yyyy");
                DateFormat second = new SimpleDateFormat("dd.MM.yyyy");
                String textDate = first.format(dateBegin) + " - " + second.format(dateEnd);
                date.setText(textDate);
            } catch (ParseException e) {
                String textDate = memoryPageModelList.get(position).getDatarod() + " - " + memoryPageModelList.get(position).getDatasmert();
                date.setText(textDate);
            }
        }
    }
}
