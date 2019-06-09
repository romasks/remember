package com.remember.app.ui.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.remember.app.R;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HandBookAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private List<ResponseHandBook> responseHandBooks = new ArrayList<>();
    private List<ResponseHandBook> filteredList = new ArrayList<>();

    public HandBookAdapter(List<ResponseHandBook> responseHandBooks) {
        this.filteredList.addAll(responseHandBooks);
        this.responseHandBooks.addAll(responseHandBooks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HandBookAdapter.HandBookAdapterViewHolder(
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

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public interface Callback{

        void saveItem(ResponseHandBook name);

    }

    public void filter(String text) {
        filteredList.clear();
        if(text.isEmpty()){
            filteredList.addAll(this.responseHandBooks);
        } else{
            text = text.toLowerCase();
            for(ResponseHandBook item: this.responseHandBooks){
                if(item.getName().toLowerCase().contains(text)){
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setItems(List<ResponseHandBook> responseHandBooks) {
        this.responseHandBooks.addAll(responseHandBooks);
        notifyDataSetChanged();
    }

    public class HandBookAdapterViewHolder extends BaseViewHolder {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.layout)
        ConstraintLayout layout;

        HandBookAdapterViewHolder(View itemView) {
            super(itemView);
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