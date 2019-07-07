package com.remember.app.ui.adapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.ui.base.BaseViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EpitaphsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private Callback callback;
    private boolean isShow;
    private List<ResponseEpitaphs> responseEpitaphs = new ArrayList<>();

    public EpitaphsAdapter(boolean isShow) {
        this.isShow = isShow;
    }

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
        return responseEpitaphs.size();
    }

    public void setItems(List<ResponseEpitaphs> responseEpitaphs) {
        this.responseEpitaphs.clear();
        this.responseEpitaphs.addAll(responseEpitaphs);
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void change(ResponseEpitaphs responseEpitaphs);

        void delete();
    }

    public class EpitaphsAdapterViewHolder extends BaseViewHolder {

        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.name_user)
        TextView name;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.change)
        TextView change;
        @BindView(R.id.delete)
        TextView delete;
        @BindView(R.id.avatar)
        ImageView avatar;

        public EpitaphsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {
            if (!responseEpitaphs.get(position).getUser().getName().equals("")) {
                name.setText(responseEpitaphs.get(position).getUser().getName());
            } else {
                name.setText("Неизвестный");
            }
            if (responseEpitaphs.get(position).getUser().getSettings().get(0).getPicture() != null) {
                Glide.with(itemView)
                        .load("http://86.57.172.88:8082" + responseEpitaphs.get(position).getUser().getSettings().get(0).getPicture())
                        .apply(RequestOptions.circleCropTransform())
                        .into(avatar);
            }
            if (!isShow){
                if (responseEpitaphs.get(position).getUser().getId() == Integer.parseInt(Prefs.getString("USER_ID", "0"))) {
                    delete.setVisibility(View.VISIBLE);
                    change.setVisibility(View.VISIBLE);
                    if (!Prefs.getString("AVATAR", "").equals("")) {
                        Glide.with(itemView)
                                .load(Prefs.getString("AVATAR", ""))
                                .apply(RequestOptions.circleCropTransform())
                                .into(avatar);
                    } else {
                        Glide.with(itemView)
                                .load(R.drawable.ic_user)
                                .apply(RequestOptions.circleCropTransform())
                                .into(avatar);
                    }
                    delete.setOnClickListener(v -> {
                        callback.delete();
                    });
                    change.setOnClickListener(v -> {
                        callback.change(responseEpitaphs.get(position));
                    });
                }
            } else {
                Glide.with(itemView)
                        .load(R.drawable.ic_user)
                        .apply(RequestOptions.circleCropTransform())
                        .into(avatar);
            }
            try {
                date.setText(getDate(responseEpitaphs.get(position).getUpdatedAt()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            description.setText(responseEpitaphs.get(position).getBody());
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            avatar.setColorFilter(filter);
        }
    }

    private String getDate(String updatedAt) throws ParseException {
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = originalFormat.parse(updatedAt);
        return targetFormat.format(date);
    }
}