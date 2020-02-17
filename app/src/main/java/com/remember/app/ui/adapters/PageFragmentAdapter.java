package com.remember.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseViewHolder;
import com.remember.app.ui.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class PageFragmentAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Callback callback;
    private boolean isMainPages = false;
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
        if (!isMainPages) this.memoryPageModelList.clear();
        this.memoryPageModelList.addAll(memoryPageModelList);
        notifyDataSetChanged();
    }

    public void setItemsSearched(List<MemoryPageModel> memoryPageModelList) {
        this.memoryPageModelList = memoryPageModelList;
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setIsMainPages(boolean isMainPages) {
        this.isMainPages = isMainPages;
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
        @BindView(R.id.surname)
        TextView surname;

        PageFragmentAdapterViewHolder(View itemView) {
            super(itemView, 0);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            layout.setOnClickListener(v -> {
                callback.sendItem(memoryPageModelList.get(position));
            });
            try {
                if (memoryPageModelList.get(position).getPicture().contains("uploads")) {
                    setGlideImage(memoryPageModelList.get(position).getPicture(), avatarImage);
                } else {
                    setGlideImage(R.drawable.darth_vader, avatarImage);
                }
            } catch (NullPointerException e) {
                setGlideImage(R.drawable.darth_vader, avatarImage);
            }

            String secondName = memoryPageModelList.get(position).getSecondName();
            String fullName = memoryPageModelList.get(position).getName() + " " + memoryPageModelList.get(position).getThirdName();
            surname.setText(secondName);
            name.setText(fullName);

            String dateBirth = DateUtils.convertRemoteToLocalFormat(memoryPageModelList.get(position).getDateBirth());
            String dateDeath = DateUtils.convertRemoteToLocalFormat(memoryPageModelList.get(position).getDateDeath());
            String textDate = dateBirth + " - " + dateDeath;
            date.setText(textDate);
        }
    }
}
