package com.remember.app.ui.cabinet.memory_pages.show_page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.Constants;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseImagesSlider;
import com.remember.app.ui.adapters.PhotoSliderAdapter;
import com.remember.app.ui.adapters.SlidePagerAdapterPhoto;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.menu.page.PageActivityMenu;
import com.remember.app.ui.utils.DeleteSlidingPhotoDialog;
import com.remember.app.ui.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SlidePhotoActivity extends BaseActivity
        implements ShowPageView, SlidePagerAdapterPhoto.Count, PhotoSliderAdapter.ItemClickListener, DeleteSlidingPhotoDialog.Callback {

    private final static String TAG = "SlidePhotoActivity";

    @InjectPresenter
    ShowPagePresenter presenter;

    @BindView(R.id.vp)
    ViewPager viewPager;
    @BindView(R.id.slide_rv)
    RecyclerView recyclerSlider;
    @BindView(R.id.photo_number)
    CustomTextView photoNumber;
    @BindView(R.id.photo_count)
    CustomTextView photoCount;
    @BindView(R.id.back_button)
    ImageView back;
    @BindView(R.id.eventHeaderName)
    CustomTextView title;


    private PhotoSliderAdapter photoSliderAdapter;
    private SlidePagerAdapterPhoto slidePagerAdapterPhoto;
    private int id = 0;
    private int position;
    private int currentPosition;
    static DeleteCallBack listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        back.setImageResource(Utils.isThemeDark() ? R.drawable.ic_back_dark_theme : R.drawable.ic_back);

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
        title.setText("Фото "+ (position+1) + " из " + responseImagesSliders.size());
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onDeleteSliderPhoto(Object o) {
        Toast.makeText(getApplicationContext(), "Фото удалено", Toast.LENGTH_SHORT).show();
        //  finish();
        listener.deletePhoto(currentPosition);
        onBackPressed();
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onDeletePhoto() {
        int v = slidePagerAdapterPhoto.getItems().get(currentPosition).getId();
        presenter.deleteSliderPhoto(v);
    }

    @Override
    public void onDeleteSliderPhotoError(Throwable throwable) {
        Utils.showSnack(photoCount, "Ошибка удаления, попробуйте позже");
    }

    public void showDeleteDialog() {
        DeleteSlidingPhotoDialog myDialogFragment = new DeleteSlidingPhotoDialog();
        myDialogFragment.setCallback(this);
        myDialogFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }


    @OnClick(R.id.back_button)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.settings)
    public void deletePage() {
        showDeleteDialog();
    }

    @Override
    public void getCountPage(String i1) {
        photoCount.setText(i1);
       // title.setText("Фото "+ i1 + " из " + slidePagerAdapterPhoto.getItems().size());
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i(TAG, "item= " + position);
        viewPager.setCurrentItem(position);
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            photoNumber.setText(String.valueOf(position + 1));
            title.setText("Фото "+ (position+1) + " из " + slidePagerAdapterPhoto.getItems().size());
        }

        @Override
        public void onPageSelected(int position) {
            Log.d("t", String.valueOf(position));
            currentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    interface DeleteCallBack{
        void deletePhoto(int position);
    }
    public static void setCallback(DeleteCallBack callback) {
        listener = callback;
    }
}
