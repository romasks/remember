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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.remember.app.R;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.cabinet.epitaphs.EpitaphsActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
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

public class ShowPageActivity extends MvpAppCompatActivity implements PopupMap.Callback {

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
    @BindView(R.id.grave)
    TextView grave;
    @BindView(R.id.events)
    ImageButton events;
    @BindView(R.id.epitButton)
    ImageButton epitaphyButton;

    private Unbinder unbinder;
    private boolean isList = false;
    private boolean isShow = false;
    private AddPageModel person;
    private MemoryPageModel memoryPageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        unbinder = ButterKnife.bind(this);
        Intent i = getIntent();
        isList = i.getBooleanExtra("IS_LIST", false);
        isShow = i.getBooleanExtra("SHOW", false);
        if (isShow) {
            settings.setClickable(false);
        }
        if (!isList) {
            person = i.getParcelableExtra("PERSON");
            if (person != null) {
                initTextName(person);
                initDate(person);
                initInfo(person);
            }
        } else {
            memoryPageModel = i.getParcelableExtra("PERSON");
            if (memoryPageModel != null) {
                Glide.with(this)
                        .load("http://86.57.172.88:8082" + memoryPageModel.getPicture())
                        .error(R.drawable.darth_vader)
                        .into(image);
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                image.setColorFilter(filter);
                initTextName(memoryPageModel);
                initDate(memoryPageModel);
                initInfo(memoryPageModel);
            }
        }
        epitaphyButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EpitaphsActivity.class);
            if (isShow) {
                intent.putExtra("SHOW", true);
            }
            if (isList) {
                intent.putExtra("ID_PAGE", memoryPageModel.getId());
            } else {
                intent.putExtra("ID_PAGE", person.getId());
            }
            startActivity(intent);
        });
        events.setOnClickListener(v -> {
            Intent intent = new Intent(this, EventsActivity.class);
            if (isShow) {
                intent.putExtra("SHOW", true);
            }
            intent.putExtra("NAME", name.getText().toString());
            if (isList) {
                intent.putExtra("ID_PAGE", memoryPageModel.getId());
            } else {
                intent.putExtra("ID_PAGE", person.getId());
            }
            startActivity(intent);
        });
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
    }

    private void initInfo(AddPageModel person) {
        city.setText(person.getCity());
        crypt.setText(person.getCemeteryName());
        sector.setText(person.getSpotId());
        grave.setText(person.getGraveId());
    }

    private void initDate(AddPageModel person) {
        String textDate = person.getBirthDate() + " - " + person.getDeathDate();
        date.setText(textDate);
    }

    private void initTextName(AddPageModel person) {
        String textName = person.getSecondName() + " " + person.getName() + " " + person.getThirdName();
        name.setText(textName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (!isShow) {
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
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
}
