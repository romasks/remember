package com.remember.app.ui.adapters;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.remember.app.R;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

import static com.remember.app.ui.utils.ImageUtils.setBlackWhite;

public class HomeGridAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HomeGridAdapter.HomeGridAdapterViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_grid, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public void setItems(List<ResponseHandBook> responseHandBooks) {

        notifyDataSetChanged();
    }

    public class HomeGridAdapterViewHolder extends BaseViewHolder {

        HomeGridAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void onBind(int position) {

        }

        private void loadImage(View view, ImageView imageView, String data) {
            if (!data.equals("")) {
                String base64Image = data.split(",")[1];

                setBlackWhite(imageView);

                byte[] decodedString;
                try {
                    decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                } catch (IllegalArgumentException e) {
                    decodedString = new byte[0];
                }
//                Glide.with(context)
//                        .load("https://vignette.wikia.nocookie.net/battlefront/images/0/0a/Battlefront_Vader.jpg/revision/latest?cb=20151022170631")
//                        .centerCrop()
//                        .placeholder(R.mipmap.logo)
//                        .into(imageView);
            } else {
//                Glide.with(context)
//                        .load(R.mipmap.logo)
//                        .fitCenter()
//                        .into(imageView);
            }
        }
    }
}
