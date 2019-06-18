package com.remember.app.ui.cabinet.memory_pages.show_page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.remember.app.R;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.cabinet.epitaphs.EpitaphsActivity;
import com.remember.app.ui.cabinet.memory_pages.add_page.NewMemoryPageActivity;
import com.remember.app.ui.cabinet.memory_pages.events.EventsActivity;
import com.remember.app.ui.utils.MvpAppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShowPageActivity extends MvpAppCompatActivity implements PopupMap.Callback, ShowPageView {

    @InjectPresenter
    ShowPagePresenter presenter;

    @BindView(R.id.fio)
    TextView name;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.settings)
    ImageView settings;
    @BindView(R.id.dates)
    TextView date;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.crypt)
    TextView crypt;
    @BindView(R.id.sector)
    TextView sector;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.description_title)
    TextView descriptionTitle;
    @BindView(R.id.grave)
    TextView grave;
    @BindView(R.id.events)
    ImageButton events;
    @BindView(R.id.epitButton)
    ImageButton epitaphyButton;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    private Unbinder unbinder;
    private boolean isList = false;
    private boolean isShow = false;
    private boolean afterSave = false;
    private int id = 0;
    private MemoryPageModel memoryPageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        unbinder = ButterKnife.bind(this);
        Intent i = getIntent();

        isList = i.getBooleanExtra("IS_LIST", false);
        afterSave = i.getBooleanExtra("AFTER_SAVE", false);
        isShow = i.getBooleanExtra("SHOW", false);

        if (isShow) {
            memoryPageModel = i.getParcelableExtra("PERSON");
            settings.setClickable(false);
            initAll();
        } else {
            id = i.getIntExtra("ID", 0);
            presenter.getImageAfterSave(id);
        }

        epitaphyButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EpitaphsActivity.class);
            if (isShow) {
                intent.putExtra("SHOW", true);
            }
            intent.putExtra("ID_PAGE", memoryPageModel.getId());
            startActivity(intent);
        });

        events.setOnClickListener(v -> {
            Intent intent = new Intent(this, EventsActivity.class);
            if (isShow) {
                intent.putExtra("SHOW", true);
            }
            intent.putExtra("NAME", name.getText().toString());
            intent.putExtra("ID_PAGE", memoryPageModel.getId());
            startActivity(intent);
        });

    }

    private void initAll() {
        if (memoryPageModel != null) {
            if (!afterSave){
                Glide.with(this)
                        .load("http://86.57.172.88:8082" + memoryPageModel.getPicture())
                        .error(R.drawable.darth_vader)
                        .into(image);
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                image.setColorFilter(filter);
            }
            initTextName(memoryPageModel);
            initDate(memoryPageModel);
            initInfo(memoryPageModel);
        }
    }

    @OnClick(R.id.description_title)
    public void description(){
        if (description.getVisibility() == View.VISIBLE){
            description.setVisibility(View.GONE);
        } else {
            description.setVisibility(View.VISIBLE);
            scrollView.scrollTo(0, scrollView.getBottom() + 1500);
        }
    }

    @OnClick(R.id.back_button)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.map_button)
    public void showMap() {
        Log.d("LOGS", memoryPageModel.getCoords());
        if (memoryPageModel.getCoords().equals("")) {
            Toast.makeText(this, "Координаты неизвестны", Toast.LENGTH_LONG).show();
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

    private void initInfo(MemoryPageModel memoryPageModel) {
        city.setText(memoryPageModel.getGorod());
        crypt.setText(memoryPageModel.getNazvaklad());
        sector.setText(memoryPageModel.getUchastok());
        grave.setText(memoryPageModel.getNummogil());
    }

    @SuppressLint("SimpleDateFormat")
    private void initDate(MemoryPageModel memoryPageModel) {
        try {
            Date dateBegin = new SimpleDateFormat("yyyy-MM-dd").parse(memoryPageModel.getDatarod());
            Date dateEnd = new SimpleDateFormat("yyyy-MM-dd").parse(memoryPageModel.getDatasmert());
            DateFormat first = new SimpleDateFormat("dd.MM.yyyy");
            DateFormat second = new SimpleDateFormat("dd.MM.yyyy");
            String textDate = first.format(dateBegin) + " - " + second.format(dateEnd);
            date.setText(textDate);
        } catch (ParseException e) {
            String textDate = memoryPageModel.getDatarod() + " - " + memoryPageModel.getDatasmert();
            date.setText(textDate);
        }
    }

    private void initTextName(MemoryPageModel memoryPageModel) {
        String textName = memoryPageModel.getSecondname() + " " + memoryPageModel.getName() + " " + memoryPageModel.getThirtname();
        name.setText(textName);
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
        startActivity(intent);
    }

    @Override
    public void setCoordinates(double latitude, double longitude) {

    }

    @Override
    public void onReceivedImage(MemoryPageModel memoryPageModel) {
        this.memoryPageModel = memoryPageModel;
        initAll();
        Glide.with(this)
                .load("http://86.57.172.88:8082" + memoryPageModel.getPicture())
                .error(R.drawable.darth_vader)
                .into(image);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        image.setColorFilter(filter);
    }

    @Override
    public void error(Throwable throwable) {
        Snackbar.make(image, "Ошибка загрузки изображения", Snackbar.LENGTH_LONG).show();
    }
}
