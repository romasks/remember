package com.remember.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.remember.app.R;
import com.remember.app.data.models.ResponseImagesSlider;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.ui.utils.ImageUtils.glideLoadInto;

public class SlidePagerAdapterPhoto extends PagerAdapter {

    private Context context;
    private Count count;
    private List<ResponseImagesSlider> responseImagesSliders = new ArrayList<>();
    private static final String TAG = "SlidePagerAdapterPhoto";

    public SlidePagerAdapterPhoto(Context context) {
        this.context = context;
    }

    public void setItems(List<ResponseImagesSlider> responseImagesSliders) {
        this.responseImagesSliders.clear();
        this.responseImagesSliders.addAll(responseImagesSliders);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return responseImagesSliders.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View container, @NonNull Object object) {
        return container == object;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout_photo, container, false);

        ImageView imageView = view.findViewById(R.id.imageView2);
        TextView textView = view.findViewById(R.id.textView15);
        textView.setText(responseImagesSliders.get(position).getBody());

        count.getCountPage(String.valueOf(responseImagesSliders.size()));
        glideLoadInto(context, BASE_SERVICE_URL + responseImagesSliders.get(position).getPicture(), imageView);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    public void setCount(Count count) {
        this.count = count;
    }

    public interface Count {
        void getCountPage(String i1);
    }
}
