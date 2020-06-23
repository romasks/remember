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
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseViewHolder;
import com.remember.app.ui.utils.DateUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class PageFragmentAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Callback callback;
    private boolean isMainPages = false;
    private LinkedList<MemoryPageModel> memoryPageModelList = new LinkedList<>();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PageFragmentAdapterViewHolder(
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

    public void setItems(LinkedList<MemoryPageModel> memoryPageModelList) {
       // if (!isMainPages)
            this.memoryPageModelList.clear();
        this.memoryPageModelList.addAll(memoryPageModelList);
        notifyDataSetChanged();
    }

    public void setItemsSearched(LinkedList<MemoryPageModel> memoryPageModelList) {
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
        CustomTextView name;
        @BindView(R.id.date)
        CustomTextView date;
        @BindView(R.id.comment)
        CustomTextView comment;
        @BindView(R.id.layout)
        ConstraintLayout layout;
        @BindView(R.id.surname)
        CustomTextView surname;

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
                    setGlideImage(R.drawable.no_photo, avatarImage);
                }
            } catch (NullPointerException e) {
                setGlideImage(R.drawable.no_photo, avatarImage);
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
