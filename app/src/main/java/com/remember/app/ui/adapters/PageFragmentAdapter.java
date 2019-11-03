package com.remember.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.ui.utils.ImageUtils.setBlackWhite;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

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
        this.memoryPageModelList.clear();
        this.memoryPageModelList.addAll(memoryPageModelList);
        notifyDataSetChanged();
    }

    public void setItemsSearched(List<MemoryPageModel> memoryPageModelList) {
        this.memoryPageModelList.clear();
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
        @BindView(R.id.surname)
        TextView surname;

        PageFragmentAdapterViewHolder(View itemView) {
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
                if (memoryPageModelList.get(position).getPicture().contains("uploads")) {
                    setGlideImage(itemView.getContext(), BASE_SERVICE_URL + memoryPageModelList.get(position).getPicture(), avatarImage);
                } else {
                    setGlideImage(context, R.drawable.darth_vader, avatarImage);
                }
            } catch (NullPointerException e) {
                setGlideImage(context, R.drawable.darth_vader, avatarImage);
            }
            setBlackWhite(avatarImage);

            String secondName = memoryPageModelList.get(position).getSecondname();
            String fullName = memoryPageModelList.get(position).getName() + " " + memoryPageModelList.get(position).getThirtname();
            surname.setText(secondName);
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
