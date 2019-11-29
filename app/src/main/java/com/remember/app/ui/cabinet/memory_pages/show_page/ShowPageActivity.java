package com.remember.app.ui.cabinet.memory_pages.show_page;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jaychang.sa.utils.StringUtils;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseImagesSlider;
import com.remember.app.ui.adapters.PhotoSliderAdapter;
import com.remember.app.ui.cabinet.epitaphs.EpitaphsActivity;
import com.remember.app.ui.cabinet.memory_pages.add_page.NewMemoryPageActivity;
import com.remember.app.ui.cabinet.memory_pages.events.EventsActivity;
import com.remember.app.ui.utils.DateUtils;
import com.remember.app.ui.utils.MvpAppCompatActivity;
import com.remember.app.ui.utils.PhotoDialog;
import com.remember.app.ui.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;
import com.vk.sdk.dialogs.VKShareDialog;
import com.vk.sdk.dialogs.VKShareDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.BURIAL_PLACE_CEMETERY;
import static com.remember.app.data.Constants.BURIAL_PLACE_CITY;
import static com.remember.app.data.Constants.BURIAL_PLACE_COORDS;
import static com.remember.app.data.Constants.BURIAL_PLACE_GRAVE;
import static com.remember.app.data.Constants.BURIAL_PLACE_LINE;
import static com.remember.app.data.Constants.BURIAL_PLACE_SECTOR;
import static com.remember.app.data.Constants.INTENT_EXTRA_AFTER_SAVE;
import static com.remember.app.data.Constants.INTENT_EXTRA_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_IS_LIST;
import static com.remember.app.data.Constants.INTENT_EXTRA_NAME;
import static com.remember.app.data.Constants.INTENT_EXTRA_PAGE_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON;
import static com.remember.app.data.Constants.INTENT_EXTRA_SHOW;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoWithError;

public class ShowPageActivity extends MvpAppCompatActivity implements PopupMap.Callback, ShowPageView, PhotoDialog.Callback, PhotoSliderAdapter.ItemClickListener {

    private final String TAG = ShowPageActivity.class.getSimpleName();

    @InjectPresenter
    ShowPagePresenter presenter;

    @BindView(R.id.fio)
    TextView name;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.settings)
    ImageView settings;
    @BindView(R.id.dates)
    TextView date;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.crypt)
    TextView crypt;
    @BindView(R.id.sector_value)
    TextView sector;
    @BindView(R.id.line_value)
    TextView line;
    @BindView(R.id.grave_value)
    TextView grave;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.description_title)
    TextView descriptionTitle;
    @BindView(R.id.events)
    ImageButton events;
    @BindView(R.id.epitButton)
    ImageButton epitaphButton;
    @BindView(R.id.imageButton)
    ImageButton imageButton;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.recycler_slider)
    RecyclerView recyclerSlider;
    @BindView(R.id.shareVk)
    ImageView but_vk;
    @BindView(R.id.lv_add)
    LinearLayout imadd;

    @BindView(R.id.back_button)
    ImageView backImg;
    @BindView(R.id.panel)
    LinearLayout panel;

    private Unbinder unbinder;
    private boolean isList = false;
    private boolean isShow = false;
    private boolean afterSave = false;
    private PhotoDialog photoDialog;
    private int id = 0;
    private MemoryPageModel memoryPageModel;
    private PhotoSliderAdapter photoSliderAdapter;
    private int sharing = 0;
    private boolean isSlider = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        unbinder = ButterKnife.bind(this);

        if (Utils.isThemeDark()) {
            backImg.setImageResource(R.drawable.ic_back_dark_theme);
            settings.setImageResource(R.drawable.setting_white);
            panel.setBackground(getResources().getDrawable(R.drawable.panel_dark));
//            view.setBackground(getResources().getDrawable(R.drawable.gradient_dark));
        }

        if (Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID)) {
            imadd.setVisibility(View.GONE);
            but_vk.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
        }

        title.setText(R.string.memory_page_header_text);

        Intent i = getIntent();
        isList = i.getBooleanExtra(INTENT_EXTRA_IS_LIST, false);
        afterSave = i.getBooleanExtra(INTENT_EXTRA_AFTER_SAVE, false);
        isShow = i.getBooleanExtra(INTENT_EXTRA_SHOW, false);

        if (isShow) {
            memoryPageModel = i.getParcelableExtra(INTENT_EXTRA_PERSON);
            presenter.getImagesSlider(memoryPageModel.getId());
            id = memoryPageModel.getId();
            settings.setClickable(false);
            settings.setVisibility(View.GONE);
            imageButton.setClickable(false);
            initAll();
        } else {
            id = i.getIntExtra(INTENT_EXTRA_ID, 0);
            presenter.getImagesSlider(id);
            presenter.getImageAfterSave(id);
        }

        epitaphButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EpitaphsActivity.class);
            intent.putExtra(INTENT_EXTRA_SHOW, isShow);
            intent.putExtra(INTENT_EXTRA_PAGE_ID, memoryPageModel.getId());
            startActivity(intent);
        });

        events.setOnClickListener(v -> {
            Intent intent = new Intent(this, EventsActivity.class);
            intent.putExtra(INTENT_EXTRA_SHOW, isShow);
            intent.putExtra(INTENT_EXTRA_NAME, name.getText().toString());
            intent.putExtra(INTENT_EXTRA_PAGE_ID, memoryPageModel.getId());
            startActivity(intent);
        });
        image.setOnClickListener(v -> {
            if (isSlider) {
                startActivity(new Intent(ShowPageActivity.this, SlidePhotoActivity.class)
                        .putExtra(INTENT_EXTRA_ID, id));
            }
        });

        recyclerSlider.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        photoSliderAdapter = new PhotoSliderAdapter();
        photoSliderAdapter.setClickListener(this);
        recyclerSlider.setAdapter(photoSliderAdapter);
    }

    private String getNameTitle(MemoryPageModel memoryPageModel) {
        String result = "Памятная страница."
                + " " + StringUtils.capitalize(memoryPageModel.getSecondName())
                + " " + StringUtils.capitalize(memoryPageModel.getName())
                + " " + StringUtils.capitalize(memoryPageModel.getThirdName());
        String textDate = DateUtils.convertRemoteToLocalFormat(memoryPageModel.getDateBirth())
                + " - " + DateUtils.convertRemoteToLocalFormat(memoryPageModel.getDateDeath());
        return result + ". " + textDate;
    }

    @OnClick(R.id.imageButton)
    public void pickImage() {
        photoDialog = new PhotoDialog();
        photoDialog.setCallback(this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        photoDialog.show(transaction, "photoDialog");
    }

    private void initAll() {
        if (memoryPageModel != null) {
            if (!afterSave) {
                glideLoadIntoWithError(this, BASE_SERVICE_URL + memoryPageModel.getPicture(), image);
            }
            initTextName(memoryPageModel);
            initDate(memoryPageModel);
            initInfo(memoryPageModel);
        }
    }

    @OnClick(R.id.description_title)
    public void description() {
        if (description.getVisibility() == View.VISIBLE) {
            description.setVisibility(View.GONE);
            descriptionTitle.setText(R.string.memory_page_show_description_text);
        } else {
            description.setVisibility(View.VISIBLE);
            descriptionTitle.setText(R.string.memory_page_hide_description_text);
            scrollView.scrollTo(0, scrollView.getBottom() + 1500);
        }
    }

    @OnClick(R.id.back_button)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.map_button)
    public void showMap() {
        Log.d(TAG, memoryPageModel.getCoords());
        if (memoryPageModel.getCoords().isEmpty()) {
            Utils.showSnack(image, "Координаты неизвестны");
        } else {
            View popupView = getLayoutInflater().inflate(R.layout.popup_google_map, null);
            PopupMap popupWindow = new PopupMap(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setCallback(this);
            popupWindow.setUp(image, getSupportFragmentManager(), memoryPageModel.getCoords());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == Activity.RESULT_OK) {
            assert result != null;
            photoDialog.setUri(result.getUri());
            Log.i(TAG, "RESULT_OK");
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            assert result != null;
            Log.e(TAG, result.getError().getMessage());
            Log.i(TAG, "CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE");
        } else {
            Log.i(TAG, "HZ");
        }
    }

    private void initInfo(MemoryPageModel memoryPageModel) {
        city.setText(memoryPageModel.getGorod().isEmpty() ? "-" : memoryPageModel.getGorod());
        crypt.setText(memoryPageModel.getNazvaklad().isEmpty() ? "-" : memoryPageModel.getNazvaklad());
        sector.setText(memoryPageModel.getSector().isEmpty() ? "-" : memoryPageModel.getSector());
        line.setText(memoryPageModel.getUchastok().isEmpty() ? "-" : memoryPageModel.getUchastok());
        grave.setText(memoryPageModel.getNummogil().isEmpty() ? "-" : memoryPageModel.getNummogil());
    }

    private void initDate(MemoryPageModel memoryPageModel) {
        String textDate = DateUtils.convertRemoteToLocalFormat(memoryPageModel.getDateBirth())
                + " - " + DateUtils.convertRemoteToLocalFormat(memoryPageModel.getDateDeath());
        date.setText(textDate);
    }

    private void initTextName(MemoryPageModel memoryPageModel) {
        String result = StringUtils.capitalize(memoryPageModel.getSecondName())
                + " " + StringUtils.capitalize(memoryPageModel.getName())
                + " " + StringUtils.capitalize(memoryPageModel.getThirdName());
        name.setText(result);
        description.setText(memoryPageModel.getComment());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.settings)
    public void editPage() {
        Intent intent = new Intent(this, NewMemoryPageActivity.class);
        intent.putExtra("PERSON", memoryPageModel);
        intent.putExtra("LIST", isList);
        intent.putExtra("EDIT", true);
        intent.putExtra(BURIAL_PLACE_COORDS, memoryPageModel.getCoords());
        intent.putExtra(BURIAL_PLACE_CITY, memoryPageModel.getGorod());
        intent.putExtra(BURIAL_PLACE_CEMETERY, memoryPageModel.getNazvaklad());
        intent.putExtra(BURIAL_PLACE_SECTOR, memoryPageModel.getSector());
        intent.putExtra(BURIAL_PLACE_LINE, memoryPageModel.getUchastok());
        intent.putExtra(BURIAL_PLACE_GRAVE, memoryPageModel.getNummogil());
        startActivity(intent);
    }

    @Override
    public void setCoordinates(double latitude, double longitude) {

    }

    @Override
    public void onReceivedImage(MemoryPageModel memoryPageModel) {
        this.memoryPageModel = memoryPageModel;
        initAll();
        glideLoadIntoWithError(this, BASE_SERVICE_URL + memoryPageModel.getPicture(), image);
    }

    @Override
    public void error(Throwable throwable) {
        Log.i(TAG, "throwable= " + throwable.toString());
        Utils.showSnack(image, "Ошибка загрузки изображения");
    }

    @Override
    public void onSavedImage(Object o) {
        photoDialog.dismiss();
        Utils.showSnack(image, "Успешно");
        presenter.getImagesSlider(memoryPageModel.getId());

    }

    @Override
    public void onImagesSlider(List<ResponseImagesSlider> responseImagesSliders) {
        photoSliderAdapter.setItems(responseImagesSliders);
        if (responseImagesSliders.size() > 0) {
            isSlider = true;
        }
    }

    @Override
    public void showPhoto() {
        CropImage.activity()
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    @Override
    public void sendPhoto(File imageFile, String string) {
        if (memoryPageModel.getId() != null) {
            Log.i(TAG, "!= null" + imageFile.toString() + "  string= " + string + "  " + memoryPageModel.getId());
            presenter.savePhoto(imageFile, string, memoryPageModel.getId());
        } else {
            Log.i(TAG, "== null");
            presenter.savePhoto(imageFile, string, id);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(ShowPageActivity.this, SlidePhotoActivity.class)
                .putExtra("ID", id));
    }

    @OnClick(R.id.shareVk)
    public void shareVk() {
        sharing = 1;
        VKAccessToken token = VKAccessToken.currentToken();
        if (token == null) {
            VKSdk.login(this, VKScope.FRIENDS, VKScope.WALL, VKScope.PHOTOS);
            Utils.showSnack(image, "Необходимо авторизоваться через ВКонтакте");
        } else {
            new sendPostSocial().execute(memoryPageModel.getPicture());
        }
    }

    private class sendPostSocial extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                String u = BASE_SERVICE_URL + strings[0];
                URL url = new URL(u);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                try {
                    input.reset();
                } catch (IOException e) {
                    return null;
                }
                Log.i(TAG, "Ok");
                return myBitmap;
            } catch (Exception e) {
                Log.i(TAG, "Exception");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (sharing == 1) {
                VKShareDialogBuilder builder = new VKShareDialogBuilder();
                builder.setText(getNameTitle(memoryPageModel));
                builder.setAttachmentImages(new VKUploadImage[]{
                        new VKUploadImage(result, VKImageParameters.pngImage())
                });
                builder.setAttachmentLink("Эта запись сделана спомощью приложения Помню ", "https://play.google.com/store/apps/details?id=com.remember.app");
                builder.setShareDialogListener(new VKShareDialog.VKShareDialogListener() {
                    @Override
                    public void onVkShareComplete(int postId) {
                        // recycle bitmap if need
                        Utils.showSnack(image, "Запись опубликована");
                    }

                    @Override
                    public void onVkShareCancel() {
                        // recycle bitmap if need
                        Log.i(TAG, "shareVk error2");
                    }

                    @Override
                    public void onVkShareError(VKError error) {
                        // recycle bitmap if need
                        Utils.showSnack(image, "Ошибка публикации");
                    }
                });
                builder.show(ShowPageActivity.this.getSupportFragmentManager(), "VK_SHARE_DIALOG");
            }
        }
    }
}