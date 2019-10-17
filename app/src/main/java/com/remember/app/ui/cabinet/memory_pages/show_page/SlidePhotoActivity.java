package com.remember.app.ui.cabinet.memory_pages.show_page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseImagesSlider;
import com.remember.app.ui.adapters.PhotoSliderAdapter;
import com.remember.app.ui.adapters.SlidePagerAdapterPhoto;
import com.remember.app.ui.utils.MvpAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SlidePhotoActivity extends MvpAppCompatActivity implements ShowPageView,SlidePagerAdapterPhoto.Count,PhotoSliderAdapter.ItemClickListener{

    @InjectPresenter
    ShowPagePresenter presenter;
    private int id = 0;
  @BindView(R.id.vp)
    ViewPager viewPager;
@BindView(R.id.imageView3)
    ImageView im_back;
    @BindView(R.id.slide_rv)
    RecyclerView recyclerSlider;
    @BindView(R.id.textView5)
    TextView textCount_1;
    @BindView(R.id.textView8)
    TextView textCount_2;
    private PhotoSliderAdapter photoSliderAdapter;
    private Unbinder unbinder;
    private SlidePagerAdapterPhoto slidePagerAdapterPhoto;
    private final static String TAG="SlidePhotoActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_photo);
        unbinder = ButterKnife.bind(this);
        slidePagerAdapterPhoto=new SlidePagerAdapterPhoto(this);
        slidePagerAdapterPhoto.setCount(this);
        viewPager.setAdapter(slidePagerAdapterPhoto);
        viewPager.addOnPageChangeListener(changeListener);
        Intent i = getIntent();
        id = i.getIntExtra("ID", 0);
        presenter.getImagesSlider(id);
        presenter.getImageAfterSave(id);
        recyclerSlider.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        photoSliderAdapter = new PhotoSliderAdapter();
        photoSliderAdapter.setClickListener(this);
        recyclerSlider.setAdapter(photoSliderAdapter);
        im_back.setOnClickListener(v->onBackPressed());
    }

    @Override
    public void onReceivedImage(MemoryPageModel memoryPageModel) {


    }

    @Override
    public void error(Throwable throwable) {

    }

    @Override
    public void onSavedImage(Object o) {

    }

    @Override
    public void onImagesSlider(List<ResponseImagesSlider> responseImagesSliders) {
        photoSliderAdapter.setItems(responseImagesSliders);
        slidePagerAdapterPhoto.setItems(responseImagesSliders);
    }

    ViewPager.OnPageChangeListener changeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           textCount_1.setText(String.valueOf(position+1));
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    @Override
    public void getCountPage(String i1) {
        textCount_2.setText(i1);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i(TAG,"item= "+position);
        viewPager.setCurrentItem(position);
    }
}
