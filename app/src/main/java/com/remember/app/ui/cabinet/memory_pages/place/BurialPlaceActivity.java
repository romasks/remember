package com.remember.app.ui.cabinet.memory_pages.place;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.utils.KeyboardUtils;
import com.remember.app.ui.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.BURIAL_PLACE_CEMETERY;
import static com.remember.app.data.Constants.BURIAL_PLACE_CITY;
import static com.remember.app.data.Constants.BURIAL_PLACE_COORDS;
import static com.remember.app.data.Constants.BURIAL_PLACE_GRAVE;
import static com.remember.app.data.Constants.BURIAL_PLACE_LINE;
import static com.remember.app.data.Constants.BURIAL_PLACE_SECTOR;

public class BurialPlaceActivity extends BaseActivity implements PopupMap.Callback,
        PlaceView, PopupCity.Callback, PopupCemetery.Callback {

    @InjectPresenter
    PlacePresenter presenter;

    @BindView(R.id.back_button)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.settings)
    ImageView settingsBtn;

    @BindView(R.id.city_value)
    AutoCompleteTextView city;
    @BindView(R.id.cemetery_value)
    AutoCompleteTextView cemetery;
    @BindView(R.id.sector_value)
    AutoCompleteTextView sector;
    @BindView(R.id.line_value)
    AutoCompleteTextView line;
    @BindView(R.id.grave_value)
    AutoCompleteTextView grave;
    @BindView(R.id.coordinates_value)
    AutoCompleteTextView coordinates;

    @BindView(R.id.pick)
    Button pick;

    private ResponseHandBook responseHandBook;

    @Override
    protected int getContentView() {
        return R.layout.activity_burial_place;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        responseHandBook = new ResponseHandBook();
        initUI();
    }

    private void initUI() {
        title.setText(getString(R.string.burial_place_header_text));
        settingsBtn.setVisibility(View.GONE);
        if (getIntent().getBooleanExtra("EDIT", false)) {
            MemoryPageModel model = getData();
            if (model != null)
                initEdit(model);
        } else {
            coordinates.setText("");
            city.setText("");
            cemetery.setText("");
            sector.setText("");
            line.setText("");
            grave.setText("");
        }
    }

    private MemoryPageModel getData() {
        return getIntent().getExtras().getParcelable("MODEL");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(BURIAL_PLACE_COORDS, coordinates.getText().toString());
        intent.putExtra(BURIAL_PLACE_CITY, city.getText().toString());
        intent.putExtra(BURIAL_PLACE_CEMETERY, cemetery.getText().toString());
        intent.putExtra(BURIAL_PLACE_SECTOR, sector.getText().toString());
        intent.putExtra(BURIAL_PLACE_LINE, line.getText().toString());
        intent.putExtra(BURIAL_PLACE_GRAVE, grave.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
        finish();
    }

    @Override
    public void setCoordinates(double latitude, double longitude) {
        String result = latitude + " " + "," + " " + longitude;
        coordinates.setText(result);
    }

    @Override
    public void onUpdatedCities(List<ResponseHandBook> responseHandBooks) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_city, null);
        PopupCity popupWindow = new PopupCity(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setCallback(this);
        popupWindow.setUp(pick, responseHandBooks);
    }

    @Override
    public void onUpdatedCemetery(List<ResponseCemetery> responseCemeteries) {
        if (responseCemeteries.isEmpty()) {
            cemetery.setFocusableInTouchMode(true);
            cemetery.requestFocus();
            KeyboardUtils.showKeyboard(this);
            Toast.makeText(this, "Введите значение вручную", Toast.LENGTH_LONG).show();
        } else {
            View popupView = getLayoutInflater().inflate(R.layout.popup_city, null);
            PopupCemetery popupWindow = new PopupCemetery(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setCallback(this);
            popupWindow.setUp(pick, responseCemeteries);
        }
    }

    @Override
    public void saveItem(ResponseHandBook responseHandBook) {
        this.responseHandBook.setId(responseHandBook.getId());
        this.responseHandBook.setRegionId(responseHandBook.getRegionId());
        this.responseHandBook.setName(responseHandBook.getName());
        city.setText(responseHandBook.getName());
    }

    @Override
    public void saveItem(ResponseCemetery responseCemetery) {
        cemetery.setText(responseCemetery.getName());
    }

    @OnClick(R.id.back_button)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.city_value)
    public void onCityClick() {
        presenter.getCities();
    }

    @OnClick(R.id.cemetery_value)
    public void onCemeteryClick() {
        if (!city.getText().toString().isEmpty()) {
            presenter.getCemetery(responseHandBook.getId());
        } else {
            Utils.showSnack(city, "Введите город");
        }
    }

    @OnClick(R.id.submit)
    public void submit() {
        if (city.getText().toString().isEmpty()) {
            Utils.showSnack(city, "Введите город");
        } else if (cemetery.getText().toString().isEmpty()) {
            Utils.showSnack(city, "Введите название кладбища");
        } else {
            onBackPressed();
        }
    }

    @OnClick(R.id.pick)
    public void openMap() {
        PopupMap popupWindow;
        try {
            View popupView = getLayoutInflater().inflate(R.layout.popup_google_map, null);
            popupWindow = new PopupMap(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setCallback(this);
            popupWindow.setUp(pick, getSupportFragmentManager());
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void setViewsInDarkTheme() {
        back.setImageResource(R.drawable.ic_back_dark_theme);
        int textColorDark = getResources().getColor(R.color.colorWhiteDark);
        city.setTextColor(textColorDark);
        cemetery.setTextColor(textColorDark);
        sector.setTextColor(textColorDark);
        line.setTextColor(textColorDark);
        grave.setTextColor(textColorDark);
        coordinates.setTextColor(textColorDark);
    }

    private void initEdit(MemoryPageModel memoryPageModel) {

        if (!memoryPageModel.getSector().equals("") || !memoryPageModel.getNummogil().equals("") || !memoryPageModel.getUchastok().equals("")) {
            city.setText(memoryPageModel.getGorod());
            cemetery.setText(memoryPageModel.getNazvaklad());
            sector.setText(memoryPageModel.getSector());
            line.setText(memoryPageModel.getUchastok());
            grave.setText(memoryPageModel.getNummogil());
            coordinates.setText(memoryPageModel.getCoords());
        } else {
            city.setText(getIntent().getStringExtra(BURIAL_PLACE_CITY));
            cemetery.setText(getIntent().getStringExtra(BURIAL_PLACE_CEMETERY));
            sector.setText(getIntent().getStringExtra(BURIAL_PLACE_SECTOR));
            line.setText(getIntent().getStringExtra(BURIAL_PLACE_LINE));
            grave.setText(getIntent().getStringExtra(BURIAL_PLACE_GRAVE));
            coordinates.setText(getIntent().getStringExtra(BURIAL_PLACE_COORDS));
        }
    }
}
