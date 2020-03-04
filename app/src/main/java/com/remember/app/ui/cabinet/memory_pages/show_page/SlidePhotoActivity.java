package com.remember.app.ui.cabinet.memory_pages.show_page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.Constants;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseImagesSlider;
import com.remember.app.ui.adapters.PhotoSliderAdapter;
import com.remember.app.ui.adapters.SlidePagerAdapterPhoto;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.utils.Utils;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class SlidePhotoActivity extends BaseActivity
        implements ShowPageView, SlidePagerAdapterPhoto.Count, PhotoSliderAdapter.ItemClickListener {

    private final static String TAG = "SlidePhotoActivity";

    @InjectPresenter
    ShowPagePresenter presenter;

    @BindView(R.id.vp)
    ViewPager viewPager;
    @BindView(R.id.back_button)
    ImageView im_back;
    @BindView(R.id.slide_rv)
    RecyclerView recyclerSlider;
    @BindView(R.id.photo_number)
    TextView photoNumber;
    @BindView(R.id.photo_count)
    TextView photoCount;

    private PhotoSliderAdapter photoSliderAdapter;
    private SlidePagerAdapterPhoto slidePagerAdapterPhoto;
    private int id = 0;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        im_back.setImageResource(Utils.isThemeDark() ? R.drawable.ic_back_dark_theme : R.drawable.ic_back);

        Intent i = getIntent();
        id = i.getIntExtra(Constants.INTENT_EXTRA_ID, 0);
        position = i.getIntExtra(Constants.INTENT_EXTRA_POSITION_IN_SLIDER, 0);

        slidePagerAdapterPhoto = new SlidePagerAdapterPhoto(this);
        slidePagerAdapterPhoto.setCount(this);

        viewPager.setAdapter(slidePagerAdapterPhoto);
        viewPager.addOnPageChangeListener(changeListener);

        presenter.getImagesSlider(id);
        presenter.getImageAfterSave(id);

        photoSliderAdapter = new PhotoSliderAdapter();
        photoSliderAdapter.setClickListener(this);

        recyclerSlider.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerSlider.setAdapter(photoSliderAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_slide_photo;
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
        viewPager.setCurrentItem(position);
    }


    @Override
    public void getCountPage(String i1) {
        photoCount.setText(i1);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i(TAG, "item= " + position);
        viewPager.setCurrentItem(position);
    }

    @OnClick(R.id.back_button)
    public void onBackClick() {
        onBackPressed();
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            photoNumber.setText(String.valueOf(position + 1));
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
