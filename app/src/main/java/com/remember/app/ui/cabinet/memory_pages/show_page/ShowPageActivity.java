package com.remember.app.ui.cabinet.memory_pages.show_page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.jaychang.sa.AuthCallback;
import com.jaychang.sa.SocialUser;
import com.jaychang.sa.facebook.FacebookAuthActivity;
import com.jaychang.sa.utils.StringUtils;
import com.nostra13.socialsharing.common.AuthListener;
import com.nostra13.socialsharing.facebook.FacebookFacade;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.Constants;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseImagesSlider;
import com.remember.app.ui.adapters.PhotoSliderAdapter;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.epitaphs.EpitaphsActivity;
import com.remember.app.ui.cabinet.memory_pages.add_page.NewMemoryPageActivity;
import com.remember.app.ui.cabinet.memory_pages.events.EventsActivity;
import com.remember.app.ui.utils.DateUtils;
import com.remember.app.ui.utils.PhotoDialog;
import com.remember.app.ui.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;
import com.vk.sdk.dialogs.VKShareDialog;
import com.vk.sdk.dialogs.VKShareDialogBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.ok.android.sdk.OKRestHelper;
import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkAuthListener;
import ru.ok.android.sdk.OkListener;
import ru.ok.android.sdk.util.OkAuthType;
import ru.ok.android.sdk.util.OkScope;

import static android.provider.UserDictionary.Words.APP_ID;
import static com.remember.app.data.Constants.BIRTH_DATE;
import static com.remember.app.data.Constants.BURIAL_PLACE_CEMETERY;
import static com.remember.app.data.Constants.BURIAL_PLACE_CITY;
import static com.remember.app.data.Constants.BURIAL_PLACE_COORDS;
import static com.remember.app.data.Constants.BURIAL_PLACE_GRAVE;
import static com.remember.app.data.Constants.BURIAL_PLACE_LINE;
import static com.remember.app.data.Constants.BURIAL_PLACE_SECTOR;
import static com.remember.app.data.Constants.FACEBOOK_APP_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_AFTER_SAVE;
import static com.remember.app.data.Constants.INTENT_EXTRA_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_IS_LIST;
import static com.remember.app.data.Constants.INTENT_EXTRA_NAME;
import static com.remember.app.data.Constants.INTENT_EXTRA_PAGE_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON;
import static com.remember.app.data.Constants.INTENT_EXTRA_SHOW;
import static com.remember.app.data.Constants.PLAY_MARKET_LINK;
import static com.remember.app.data.Constants.PREFS_KEY_ACCESS_TOKEN;
import static com.remember.app.data.Constants.PREFS_KEY_AVATAR;
import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;

import static com.remember.app.ui.utils.ImageUtils.createBitmapFromView;
import static com.remember.app.ui.utils.ImageUtils.cropImage;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoWithError;
import static com.remember.app.ui.utils.StringUtils.getStringFromField;

public class ShowPageActivity extends BaseActivity implements PopupMap.Callback, ShowPageView, PhotoDialog.Callback, PhotoSliderAdapter.ItemClickListener {

    private final String TAG = ShowPageActivity.class.getSimpleName();

    @InjectPresenter
    ShowPagePresenter presenter;
    @BindView(R.id.share_LinLayout)     //Необходимы для сокрытия кнопок решаринга при закрытом или необоренной анкете.
    LinearLayout share_LinLayout;       //
    @BindView(R.id.mainLinLayout)       //
    LinearLayout mainLinLayout;         //
    @BindView(R.id.fio)
    TextView name;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.sharedImage)
    ImageView sharedImage;
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
    @BindView(R.id.eventsButton)
    ImageButton eventsBtn;
    @BindView(R.id.epitButton)
    ImageButton epitaphBtn;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.recycler_slider)
    RecyclerView recyclerSlider;
    @BindView(R.id.shareVk)
    ImageView but_vk;
    @BindView(R.id.addPhotoToSliderBtn_layout)
    LinearLayout addPhotoToSliderBtn_layout;
    @BindView(R.id.map_button)
    Button mapButton;


    @BindView(R.id.back_button)
    ImageView backImg;
    @BindView(R.id.panel)
    LinearLayout panel;

    @BindView(R.id.shareFb)
    AppCompatImageView shareFbButton;



    private PhotoDialog photoDialog;
    private MemoryPageModel memoryPageModel;
    private PhotoSliderAdapter photoSliderAdapter;

    private boolean isList = false;
    private boolean isShow = false;
    private boolean afterSave = false;
    private int id = 0;
    private int sharing = 0;

    private static final String APP_ID = "512000155578";
    private static final String APP_KEY = "CLLQFHJGDIHBABABA";
    private static final String REDIRECT_URL = "okauth://ok512000155578";

    @Override
    protected int getContentView() {
        return R.layout.activity_page;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);

        if (Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID)) {
            addPhotoToSliderBtn_layout.setVisibility(View.GONE);
            share_LinLayout.setVisibility(View.GONE);
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
            addPhotoToSliderBtn_layout.setVisibility(View.GONE);
            initAll();
        } else {
            id = i.getIntExtra(INTENT_EXTRA_ID, 0);
            presenter.getImagesSlider(id);
            presenter.getImageAfterSave(id);
        }

        photoSliderAdapter = new PhotoSliderAdapter();
        photoSliderAdapter.setClickListener(this);

        recyclerSlider.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerSlider.setAdapter(photoSliderAdapter);

}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == Activity.RESULT_OK) {
            //assert result != null;
            if(result != null) {
                photoDialog.setUri(result.getUri());
            }else
                Log.e(TAG,"RESULT IS NULL!!!");

            Log.i(TAG, "RESULT_OK");
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            assert result != null;
            Log.e(TAG, result.getError().getMessage());
            Log.i(TAG, "CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE");
        } else {
            Log.i(TAG, "HZ");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void setCoordinates(double latitude, double longitude) {

    }
//
    @Override
    public void onReceivedImage(MemoryPageModel memoryPageModel) {
        this.memoryPageModel = memoryPageModel;
        initAll();
        glideLoadIntoWithError(memoryPageModel.getPicture(), image);
        glideLoadIntoWithError(memoryPageModel.getPicture(), sharedImage);
    }

    @Override
    public void error(Throwable throwable) {
        if(image == null)
        Utils.showSnack(image, "Ошибка загрузки изображения");
        share_LinLayout.setVisibility(View.GONE);

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
    }

    @Override
    public void showPhoto() {
        cropImage(this);
    }

    @Override
    public void sendPhoto(File imageFile, String string) {
        if (memoryPageModel.getId() != null) {
            presenter.savePhoto(imageFile, string, memoryPageModel.getId());
        } else {
            presenter.savePhoto(imageFile, string, id);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(
                new Intent(ShowPageActivity.this, SlidePhotoActivity.class)
                        .putExtra(Constants.INTENT_EXTRA_ID, id)
                        .putExtra(Constants.INTENT_EXTRA_POSITION_IN_SLIDER, position)
        );
    }

    @Override
    protected void setViewsInDarkTheme() {
        backImg.setImageResource(R.drawable.ic_back_dark_theme);
        settings.setImageResource(R.drawable.setting_white);
        panel.setBackground(getResources().getDrawable(R.drawable.panel_dark));
    }

    @OnClick(R.id.addPhotoToSliderBtn)
    public void pickImage() {
        photoDialog = new PhotoDialog();
        photoDialog.setCallback(this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        photoDialog.show(transaction, "photoDialog");
    }

    @OnClick(R.id.image)
    public void onMainImageClick() {
        /*if (isSlider) {
            startActivity(new Intent(ShowPageActivity.this, SlidePhotoActivity.class)
                    .putExtra(INTENT_EXTRA_ID, id));
        }*///temporarily block onImageClick
    }

    @OnClick(R.id.epitButton)
    public void onEpitaphButtonClick() {
        Intent intent = new Intent(this, EpitaphsActivity.class);
        intent.putExtra(INTENT_EXTRA_SHOW, isShow);
        intent.putExtra(INTENT_EXTRA_PAGE_ID, memoryPageModel.getId());
        startActivity(intent);
    }

    @OnClick(R.id.eventsButton)
    public void onEventButtonClick() {
        Log.d("myLog", "data1 = " + memoryPageModel.getDateBirth());
        Intent intent = new Intent(this, EventsActivity.class);
        intent.putExtra(INTENT_EXTRA_SHOW, isShow);
        intent.putExtra(INTENT_EXTRA_NAME, name.getText().toString());
        intent.putExtra(INTENT_EXTRA_PAGE_ID, memoryPageModel.getId());
        intent.putExtra(BIRTH_DATE, memoryPageModel.getDateBirth());
        startActivity(intent);
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

    @OnClick(R.id.shareVk)
    public void shareVk() {
        sharing = 1;
        VKAccessToken token = VKAccessToken.currentToken();
        if (token == null) {
            VKSdk.login(this, VKScope.FRIENDS, VKScope.WALL, VKScope.PHOTOS);
            Utils.showSnack(image, "Необходимо авторизоваться через ВКонтакте");
            sharePageToVk();
        } else {
            sharePageToVk();
        }
    }

    @OnClick(R.id.shareFb)
    public void shareFb() {
        final String generatedByIDLink = "https://pomnyu.ru/public/page/"+memoryPageModel.getId().toString();// Генерация ссылки, для поста (через константу неправильно форматируется ссылка)

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(generatedByIDLink))
                .build();
        ShareDialog.show(this,content);
    }



    private void sharePageToVk(){

        if (sharing == 1) {
            VKShareDialogBuilder builder = new VKShareDialogBuilder();
            builder.setText("ᅠ ");
            //builder.setAttachmentImages(new VKUploadImage[]{new VKUploadImage(createBitmapFromView(sharedImage), VKImageParameters.pngImage())});
            final String generatedByIDLink = "https://pomnyu.ru/public/page/"+memoryPageModel.getId().toString();// Генерация ссылки, для поста (через константу неправильно форматируется ссылка)

            builder.setAttachmentLink("Эта запись сделана спомощью приложения Помню", generatedByIDLink);

             builder.setShareDialogListener(new VKShareDialog.VKShareDialogListener() {
                @Override
                public void onVkShareComplete(int postId) {
                    Utils.showSnack(image, "Запись опубликована");
                }

                @Override
                public void onVkShareCancel() {
                    Log.i(TAG, "shareVk error2");
                }

                @Override
                public void onVkShareError(VKError error) {
                    Utils.showSnack(image, "Ошибка публикации");
                }
            });
            builder.show(ShowPageActivity.this.getSupportFragmentManager(), "VK_SHARE_DIALOG");
        }
    }

    /*private String getNameTitle(MemoryPageModel memoryPageModel) {
        String result = "Памятная страница."
                + " " + StringUtils.capitalize(memoryPageModel.getSecondName())
                + " " + StringUtils.capitalize(memoryPageModel.getName())
                + " " + StringUtils.capitalize(memoryPageModel.getThirdName());
        String textDate = DateUtils.convertRemoteToLocalFormat(memoryPageModel.getDateBirth())
                + " - " + DateUtils.convertRemoteToLocalFormat(memoryPageModel.getDateDeath());
        return result + ". " + textDate;
    }
*/
    private void initAll() {
        if (memoryPageModel != null) {



            if (!afterSave) {
                glideLoadIntoWithError(memoryPageModel.getPicture(), image);
                glideLoadIntoWithError(memoryPageModel.getPicture(), sharedImage);
            }
            initTextName(memoryPageModel);
            initDate(memoryPageModel);
            initInfo(memoryPageModel);
            mapButton.setVisibility(memoryPageModel.getCoords().isEmpty() ? View.GONE : View.VISIBLE);


            Log.d(TAG, "initAll: memoryPageModel.getStatus() "+memoryPageModel.getStatus());
            Log.d(TAG, "initAll: memoryPageModel.getFlag()  "+memoryPageModel.getFlag());


            if(memoryPageModel.getStatus() == null || memoryPageModel.getFlag() == null )
                share_LinLayout.setVisibility(View.GONE);
            else{
            if (!(memoryPageModel.getFlag().equals("true") && memoryPageModel.getStatus().equals("Одобрено"))){
                share_LinLayout.setVisibility(View.GONE);
                Log.e(TAG, "initAll: DELETED");
            }}


        }
    }

    private void initInfo(MemoryPageModel memoryPageModel) {
        city.setText(getStringFromField(memoryPageModel.getGorod()));
        crypt.setText(getStringFromField(memoryPageModel.getNazvaklad()));
        sector.setText(getStringFromField(memoryPageModel.getSector()));
        line.setText(getStringFromField(memoryPageModel.getUchastok()));
        grave.setText(getStringFromField(memoryPageModel.getNummogil()));
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
        Log.d(TAG, "initTextName: " + memoryPageModel.getComment());
        if (memoryPageModel.getComment().equals(""))
            descriptionTitle.setVisibility(View.GONE);
        description.setText(memoryPageModel.getComment());
    }
}