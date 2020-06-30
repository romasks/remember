package com.remember.app.ui.cabinet.memory_pages.events.current_event.reviewPhoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.EventSliderPhotos;
import com.remember.app.data.models.ResponseImagesSlider;
import com.remember.app.ui.adapters.SlidePagerAdapterPhoto;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.ui.utils.ImageUtils.glideLoadInto;import static com.remember.app.data.Constants.BASE_URL_FROM_PHOTO;

public class ReviewViewPager extends PagerAdapter {

    private Context context;
    private Count count;
    private ArrayList<EventSliderPhotos> responseImagesSliders = new ArrayList<>();
    private static final String TAG = "ReviewViewPager";

    public ReviewViewPager(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<EventSliderPhotos> responseImagesSliders) {
        this.responseImagesSliders.clear();
        this.responseImagesSliders.addAll(responseImagesSliders);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return responseImagesSliders.size();
    }

    public ArrayList<EventSliderPhotos> getItems(){
        return responseImagesSliders;
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
        CustomTextView textView = view.findViewById(R.id.textView15);
        textView.setVisibility(View.GONE);
//        if (responseImagesSliders.get(position).getBody() != null && responseImagesSliders.get(position).getBody().length()>2)
//            textView.setText(responseImagesSliders.get(position).getBody());

        count.getCountPage(String.valueOf(responseImagesSliders.size()));
        glideLoadInto(context, BASE_URL_FROM_PHOTO + responseImagesSliders.get(position).getPicture(), imageView);
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
