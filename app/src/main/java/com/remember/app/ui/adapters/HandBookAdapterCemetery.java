package com.remember.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.remember.app.R;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HandBookAdapterCemetery extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private List<ResponseCemetery> responseHandBooks = new ArrayList<>();
    private List<ResponseCemetery> filteredList = new ArrayList<>();

    public HandBookAdapterCemetery(List<ResponseCemetery> responseHandBooks) {
        this.filteredList.addAll(responseHandBooks);
        this.responseHandBooks.addAll(responseHandBooks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HandBookAdapterCemetery.HandBookAdapterCemeteryViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hand_book, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void saveItem(ResponseCemetery name);

    }

    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(this.responseHandBooks);
        } else {
            text = text.toLowerCase();
            for (ResponseCemetery item : this.responseHandBooks) {
                if (item.getName().toLowerCase().contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setItems(List<ResponseCemetery> responseHandBooks) {
        this.responseHandBooks.addAll(responseHandBooks);
        notifyDataSetChanged();
    }

    public class HandBookAdapterCemeteryViewHolder extends BaseViewHolder {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.layout)
        ConstraintLayout layout;

        HandBookAdapterCemeteryViewHolder(View itemView) {
            super(itemView, 0);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            name.setText(filteredList.get(position).getName());
            layout.setOnClickListener(v -> {
                callback.saveItem(filteredList.get(position));
            });
        }
    }
}
