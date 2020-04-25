package com.remember.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.data.models.ResponseUser;
import com.remember.app.ui.base.BaseViewHolder;
import com.remember.app.ui.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class EpitaphsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Callback callback;
    private List<ResponseEpitaphs> responseEpitaphs = new ArrayList<>();

    public EpitaphsAdapter() {
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

        void delete(Integer id);
    }

    public class EpitaphsAdapterViewHolder extends BaseViewHolder {

        @BindView(R.id.description)
        CustomTextView description;
        @BindView(R.id.name_user)
        CustomTextView name;
        @BindView(R.id.date)
        CustomTextView date;
        @BindView(R.id.change)
        CustomTextView change;
        @BindView(R.id.delete)
        CustomTextView delete;
        @BindView(R.id.imgDelete)
        AppCompatImageView imgDelete;
        @BindView(R.id.imgEdit)
        AppCompatImageView imgEdit;
        @BindView(R.id.avatar)
        ImageView avatar;

        EpitaphsAdapterViewHolder(View itemView) {
            super(itemView, 0);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            ResponseEpitaphs epitaph = responseEpitaphs.get(position);
            ResponseUser user = epitaph.getUser();

            try {
                setGlideImage(user.getSettings().getPicture(), avatar);
            } catch (Exception e) {
                setGlideImage(R.drawable.ic_user, avatar);
            }

            delete.setOnClickListener(v -> callback.delete(epitaph.getId()));
            change.setOnClickListener(v -> callback.change(epitaph));

            boolean isOwnEpitaph = user.getId() == Integer.parseInt(Prefs.getString(PREFS_KEY_USER_ID, "0"));
            delete.setVisibility(isOwnEpitaph ? View.VISIBLE : View.GONE);
            imgDelete.setVisibility(isOwnEpitaph ? View.VISIBLE : View.GONE);
            change.setVisibility(isOwnEpitaph ? View.VISIBLE : View.GONE);
            imgEdit.setVisibility(isOwnEpitaph ? View.VISIBLE : View.GONE);

            name.setText(user.getName().isEmpty() ? "Неизвестный" : user.getName());
            date.setText(DateUtils.convertRemoteToLocalFormat(epitaph.getUpdatedAt()));
            description.setText(epitaph.getBody());
        }
    }
}
