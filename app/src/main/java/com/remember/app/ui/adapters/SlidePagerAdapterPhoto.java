package com.remember.app.ui.adapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.remember.app.R;
import com.remember.app.data.models.ResponseImagesSlider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Body;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;

public class SlidePagerAdapterPhoto extends PagerAdapter {


    Body body;
    Count count;
    Context context;
    LayoutInflater layoutInflater;
    private List<ResponseImagesSlider> responseImagesSliders = new ArrayList<>();
    private int slide=0;
    private static final String TAG="SlidePagerAdapterPhoto";



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
        return container== object;
    }



    @Override

    public Object instantiateItem(ViewGroup container,int position){
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout_photo,container,false);
        ImageView imageView=view.findViewById(R.id.imageView2);
        TextView textView=view.findViewById(R.id.textView15);
        textView.setText(responseImagesSliders.get(position).getBody());
        count.getCountPage(String.valueOf(responseImagesSliders.size()));
        Glide.with(context)
                .load(BASE_SERVICE_URL + responseImagesSliders.get(position).getPicture())
                .into(imageView);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(filter);
        container.addView(view);


        return view;


    }

    @Override
    public void destroyItem(ViewGroup conteiner, int position, Object object){
        ViewPager vp=(ViewPager)conteiner;
        View view=(View)object;
        vp.removeView(view);



    }



    public void setCount(Count count){
        this.count=count;
    }
    public interface Count{
        void getCountPage(String i1);
    }


}
