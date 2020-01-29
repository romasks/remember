package com.remember.app.ui.cabinet.memory_pages.place;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.utils.Utils;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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

    @BindView(R.id.pick)
    Button pick;

    @BindView(R.id.coordinates_value)
    AutoCompleteTextView coordinates;
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

    private ResponseHandBook responseHandBook;
    private boolean isEdit;
    private MemoryPageModel memoryPageModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        responseHandBook = new ResponseHandBook();

        isEdit = getIntent().getBooleanExtra("EDIT", false);

        coordinates.setText(getIntent().getStringExtra(BURIAL_PLACE_COORDS));
        city.setText(getIntent().getStringExtra(BURIAL_PLACE_CITY));
        cemetery.setText(getIntent().getStringExtra(BURIAL_PLACE_CEMETERY));
        sector.setText(getIntent().getStringExtra(BURIAL_PLACE_SECTOR));
        line.setText(getIntent().getStringExtra(BURIAL_PLACE_LINE));
        grave.setText(getIntent().getStringExtra(BURIAL_PLACE_GRAVE));

        /*if (isEdit) {
            memoryPageModel = getIntent().getParcelableExtra("MODEL");
            initEdit();
        } else {
            coordinates.setText("");
            city.setText("");
            cemetery.setText("");
            sector.setText("");
            line.setText("");
            grave.setText("");
        }*/

        city.setOnClickListener(v -> presenter.getCities());
        cemetery.setOnClickListener(v -> {
            if (!city.getText().toString().isEmpty()) {
                presenter.getCemetery(responseHandBook.getId());
            } else {
                Utils.showSnack(city, "Введите город");
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_burial_place;
    }

    private void initEdit() {
        coordinates.setText(memoryPageModel.getCoords());
        city.setText(memoryPageModel.getGorod());
        cemetery.setText(memoryPageModel.getNazvaklad());
        sector.setText(memoryPageModel.getSector());
        line.setText(memoryPageModel.getUchastok());
        grave.setText(memoryPageModel.getNummogil());
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
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
        PopupMap popupWindow = null;
        try {
            View popupView = getLayoutInflater().inflate(R.layout.popup_google_map, null);
            popupWindow = new PopupMap(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setCallback(this);
            popupWindow.setUp(pick, getSupportFragmentManager());
        } catch (Exception e) {

        } finally {
        }
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
//        if (responseCemeteries == null) {
            cemetery.setFocusableInTouchMode(true);
            cemetery.requestFocus();
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            Toast.makeText(this, "Введите значение вручную", Toast.LENGTH_LONG).show();
        } else {
            View popupView = getLayoutInflater().inflate(R.layout.popup_city, null);
            PopupCemetery popupWindow = new PopupCemetery(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setCallback(this);
//            ArrayList<ResponseCemetery> list = new ArrayList<>();
//            list.add(responseCemeteries);
            popupWindow.setUp(pick, responseCemeteries);
//            popupWindow.setUp(pick, list);
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
}
