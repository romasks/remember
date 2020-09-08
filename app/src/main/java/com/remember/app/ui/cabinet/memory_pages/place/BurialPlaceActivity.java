package com.remember.app.ui.cabinet.memory_pages.place;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomAutoCompleteTextView;
import com.remember.app.customView.CustomButton;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.base.BaseActivity;
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
import static com.remember.app.data.Constants.INTENT_EXTRA_AFTER_SAVE;

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
    CustomAutoCompleteTextView city;
    @BindView(R.id.cemetery_value)
    CustomAutoCompleteTextView cemetery;
    @BindView(R.id.sector_value)
    CustomAutoCompleteTextView sector;
    @BindView(R.id.line_value)
    CustomAutoCompleteTextView line;
    @BindView(R.id.grave_value)
    CustomAutoCompleteTextView grave;
    @BindView(R.id.coordinates_value)
    CustomAutoCompleteTextView coordinates;

    @BindView(R.id.pick)
    CustomButton pick;
    boolean isSave = false;

    private ResponseHandBook responseHandBook;

    @Override
    protected int getContentView() {
        return R.layout.activity_burial_place;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);

        responseHandBook = new ResponseHandBook();
        initUI();
    }

    private void initUI() {
        title.setText(getString(R.string.burial_place_header_text));
        settingsBtn.setVisibility(View.GONE);
        if (getIntent().getBooleanExtra("EDIT", false) || (Prefs.getBoolean(INTENT_EXTRA_AFTER_SAVE, false))) {
            MemoryPageModel model = getData();
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
        if (isSave) {
            Intent intent = new Intent();
            intent.putExtra(BURIAL_PLACE_COORDS, coordinates.getText().toString());
            intent.putExtra(BURIAL_PLACE_CITY, city.getText().toString());
            intent.putExtra(BURIAL_PLACE_CEMETERY, cemetery.getText().toString());
            intent.putExtra(BURIAL_PLACE_SECTOR, sector.getText().toString());
            intent.putExtra(BURIAL_PLACE_LINE, line.getText().toString());
            intent.putExtra(BURIAL_PLACE_GRAVE, grave.getText().toString());
            intent.putExtra(INTENT_EXTRA_AFTER_SAVE, true);
            setResult(Activity.RESULT_OK, intent);
        }
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
            // cemetery.requestFocus();
            //KeyboardUtils.showKeyboard(this);
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
    public void saveTown(String town) {
        this.responseHandBook.setId(-999);
        city.setText(town);
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
        if (!city.getText().toString().isEmpty() || (!city.getText().toString().isEmpty() && responseHandBook.getId() != -999)) {
            presenter.getCemetery(responseHandBook.getId());
        } else {
            Utils.showSnack(city, "Выберите город");
        }
    }

    @OnClick(R.id.submit)
    public void submit() {
        if (city.getText().toString().isEmpty()) {
            Utils.showSnack(city, "Введите город");
        } else if (cemetery.getText().toString().isEmpty()) {
            Utils.showSnack(city, "Введите название кладбища");
        } else {
            isSave = true;
            onBackPressed();
        }
    }

    public void openMap() {
        try {
            View popupView = getLayoutInflater().inflate(R.layout.popup_google_map, null);
            PopupMap popupWindow = new PopupMap(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setCallback(this);
            popupWindow.setUp(pick, getSupportFragmentManager());
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
        } catch (Exception ignored) {

        }
    }

    @OnClick(R.id.pick)
    public void clickMap() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            openMap();
        } else
            Toast.makeText(getBaseContext(), "Для выбора города на карте разрешите доступ к геопозиции.", Toast.LENGTH_LONG).show();
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
        if (memoryPageModel != null) {
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
