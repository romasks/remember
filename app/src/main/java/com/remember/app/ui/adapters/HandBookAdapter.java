package com.remember.app.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.base.BaseViewHolder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HandBookAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private List<ResponseHandBook> responseHandBooks = new ArrayList<>();

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
        return responseHandBooks.size();
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public interface Callback{

        void saveItem(String name);

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
            name.setText(responseHandBooks.get(position).getName());
            layout.setOnClickListener(v -> {
                callback.saveItem(responseHandBooks.get(position).getName());
            });
        }
    }
}